package com.example.android.project_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActvity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<movies> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private FavoriteDbHelper favoriteDbHelper;
    private AppCompatActivity activity = HomeActvity.this;
    public static final String LOG_TAG = MovieAdapter.class.getName();
    public static final String RECYCLER_STATE = "list_state";
    public static final String MOVIES_KEY = "movies_key";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private String type = "";

    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actvity);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this,2);
        } else {
            gridLayoutManager = new GridLayoutManager(this,4);
        }
        if(savedInstanceState != null) {
            Parcelable recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE);
            gridLayoutManager.onRestoreInstanceState(recyclerState);
            movieList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            type = savedInstanceState.getString("type");
        } else {
            movieList = new ArrayList<>();
        }
        checkSortOrder();
        initViews();//function to sortorder//

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, (ArrayList<? extends Parcelable>) movieList);
        Parcelable recyclerState = gridLayoutManager.onSaveInstanceState();
        outState.putString("type",type);
        outState.putParcelable(RECYCLER_STATE,recyclerState);
    }

    private void initViews() {

        adapter = new MovieAdapter(this, movieList);
        //orientation  device//
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(activity);//object db//


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(HomeActvity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        checkSortOrder();
    }

    private void initViews2() {//function to getAllFavorite //

        adapter = new MovieAdapter(this, movieList);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        favoriteDbHelper = new FavoriteDbHelper(activity);

        getAllFavorite();
    }

    private void loadJSON() {//function to getPopularMovies//

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            //use library  Retrofit to call api//
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api ap = retrofit.create(api.class);

            Call<MoviesResponse> call = ap.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    movieList = response.body().getResults();

                    recyclerView.setAdapter(new MovieAdapter(HomeActvity.this, movieList));
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(HomeActvity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadJSON1() {//function to getTopRatedMovies//

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API Key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api ap = retrofit.create(api.class);

            Call<MoviesResponse> call = ap.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    movieList = response.body().getResults();

                    recyclerView.setAdapter(new MovieAdapter(HomeActvity.this, movieList));
                    if (swipeContainer.isRefreshing()) {
                        swipeContainer.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(HomeActvity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });


        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, Setting.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // use SharedPreferences to know user select sortorder//
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "Preferences updated");
        type = s;
        checkSortOrder();
    }

    private void checkSortOrder() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        preferences.registerOnSharedPreferenceChangeListener(this);
        if(sortOrder.equals(type))
            return;
        if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
            Log.d(LOG_TAG, "Sorting by most popular");
            type = this.getString(R.string.pref_most_popular);
            loadJSON();
        } else if (sortOrder.equals(this.getString(R.string.favorite))) {
            Log.d(LOG_TAG, "Sorting by favorite");
            type = this.getString(R.string.favorite);
            initViews2();
        } else {
            Log.d(LOG_TAG, "Sorting by vote average");
            type = this.getString(R.string.pref_highest_rated);
            loadJSON1();
        }
    }

    //save movieFavorite to db//
    private void getAllFavorite() {
        movieList.clear();
        
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,null,null,null,null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    movies movs = new movies();
                    movs.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                    movs.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                    movs.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))));
                    movs.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                    movs.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));
                    movieList.add(movs);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        else Toast.makeText(activity, "Cursor NULL", Toast.LENGTH_SHORT).show();
    }
}