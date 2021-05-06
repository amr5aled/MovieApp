package com.example.android.project_1;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    public static final String TRAILER_KEY = "trailer_key";
    private static final String SCROLL_POS = "scroll_pos";
    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView,favoriteImageView;
    private RecyclerView recyclerView;
    private TrailerAdapter adapter;
    private ArrayList<Trailer> trailerList;
    movies movie;
    String thumbnail, movieName, synopsis, rating, dateOfRelease;
    int movie_id;
    private boolean isFavorite ;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRotated;
    private ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView = (ImageView) findViewById(R.id.thumbnail);
        nameOfMovie = (TextView) findViewById(R.id.title);
        plotSynopsis = (TextView) findViewById(R.id.plotsynopsis);
        userRating = (TextView) findViewById(R.id.userrating);
        releaseDate = (TextView) findViewById(R.id.releasedate);
        favoriteImageView = findViewById(R.id.favorite_button);

        linearLayoutManager = new LinearLayoutManager(this);
        scrollView = findViewById(R.id.scrollView);
        movie = getIntent().getParcelableExtra("movies");
        if(savedInstanceState == null)
            trailerList = new ArrayList<>();
        else{
            isRotated = true;
            linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable("recycler"));
            trailerList = savedInstanceState.getParcelableArrayList(TRAILER_KEY);
            final int[] position = savedInstanceState.getIntArray(SCROLL_POS);
            if(position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }

        thumbnail = movie.getPosterPath();
        movieName = movie.getOriginalTitle();
        synopsis = movie.getOverview();
        rating = Double.toString(movie.getVoteAverage());
        dateOfRelease = movie.getReleaseDate();
        movie_id = movie.getId();
        SharedPreferences sharedPreferences =getSharedPreferences("DetailActivity",MODE_PRIVATE);
        isFavorite = sharedPreferences.getBoolean(""+movie_id,false);
        addFavoriteImage();
        String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

        Glide.with(this)
                    .load(poster).into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite){
                    SharedPreferences.Editor editor = getSharedPreferences("DetailActivity", MODE_PRIVATE).edit();
                    editor.putBoolean(""+movie_id, true);
                    editor.commit();
                    saveFavorite();
                    Snackbar.make(v, "Added to Favorite",
                            Snackbar.LENGTH_SHORT).show();
                    isFavorite  = true;
                    addFavoriteImage();
                }else{
                    getContentResolver().delete(FavoriteContract.FavoriteEntry.CONTENT_URI,"movieid=?",new String[]{""+movie_id});
                    SharedPreferences.Editor editor = getSharedPreferences("DetailActivity", MODE_PRIVATE).edit();
                    editor.putBoolean(""+movie_id, false);
                    editor.commit();
                    Snackbar.make(v, "Removed from Favorite",
                            Snackbar.LENGTH_SHORT).show();
                    isFavorite = false;
                    addFavoriteImage();
                }

            }
        });

        initViews();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TRAILER_KEY,trailerList);
        outState.putParcelable("recycler",linearLayoutManager.onSaveInstanceState());
        outState.putIntArray(SCROLL_POS,new int[]{scrollView.getScrollX(),scrollView.getScrollY()});
    }

    private void addFavoriteImage(){
        if(isFavorite)
            favoriteImageView.setImageResource(R.drawable.remove_alread_added);
        else
            favoriteImageView.setImageResource(R.drawable.add_to_favorite);
    }


    private void initViews(){

        adapter = new TrailerAdapter(this, trailerList);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!isRotated)
            loadJSON();

    }

    private void loadJSON(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api ap=retrofit.create(api.class);
            Call<TrailerResponse> call = ap.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    trailerList = (ArrayList<Trailer>) response.body().getResults();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailerList));
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailsActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFavorite(){

        Double rate = movie.getVoteAverage();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie_id);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE,movieName);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, rate);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,thumbnail);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, synopsis);

        getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI,values);
    }


    public void openReviewAct(View view) {
        Intent intent = new Intent(this,ReviewActivity.class);
        intent.putExtra("movie_id",movie_id);
        startActivity(intent);
    }
}





