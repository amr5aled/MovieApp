package com.example.android.project_1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by amr5aled on 3/5/2018.
 */

public interface api {


        public static final String BASE_URL = "http://api.themoviedb.org/3/";

        @GET("movie/popular")
        Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
        @GET("movie/{movie_id}/videos")
        Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);
        @GET("movie/{movie_id}/reviews")
        Call<ReviewResponse> getMovieReview(@Path("movie_id") int id, @Query("api_key") String apiKey);


}