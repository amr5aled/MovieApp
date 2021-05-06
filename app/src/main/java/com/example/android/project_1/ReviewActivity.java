package com.example.android.project_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView reviewRecylerView;
    private ArrayList<Review> reviews;
    private ProgressBar loadReviews;
    public static final String REVIEW_KEY = "review_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        int movie_id = getIntent().getIntExtra("movie_id",0);
        reviewRecylerView = findViewById(R.id.recycler_review);
        reviewRecylerView.setHasFixedSize(true);
        reviewRecylerView.setLayoutManager(new LinearLayoutManager(this));
        loadReviews = findViewById(R.id.progress_load_reviews);
        if(savedInstanceState == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(api.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            api ap = retrofit.create(api.class);

            Call<ReviewResponse> call = ap.getMovieReview(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    reviews = (ArrayList<Review>) response.body().getResults();
                    reviewRecylerView.setAdapter(new ReviewAdapter(reviews));
                    reviewRecylerView.smoothScrollToPosition(0);
                    loadReviews.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        } else {
            reviews = savedInstanceState.getParcelableArrayList(REVIEW_KEY);
            reviewRecylerView.setAdapter(new ReviewAdapter(reviews));
            loadReviews.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(REVIEW_KEY, reviews);
    }
}
