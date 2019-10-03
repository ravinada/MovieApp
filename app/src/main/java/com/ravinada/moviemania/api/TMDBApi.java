package com.ravinada.moviemania.api;

import com.ravinada.moviemania.Data.Movie;
import com.ravinada.moviemania.Data.MovieResponse;
import com.ravinada.moviemania.Data.Review;
import com.ravinada.moviemania.Data.Trailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBApi {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/videos")
    Call<Trailer> getMovieTrailers(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/reviews")
    Call<Review> getMovieReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );

}
