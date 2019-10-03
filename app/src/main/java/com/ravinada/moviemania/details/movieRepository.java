package com.ravinada.moviemania.details;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravinada.moviemania.Data.Movie;
import com.ravinada.moviemania.Data.MovieDao;
import com.ravinada.moviemania.Data.MovieResponse;
import com.ravinada.moviemania.Data.Review;
import com.ravinada.moviemania.Data.ReviewData;
import com.ravinada.moviemania.Data.Trailer;
import com.ravinada.moviemania.Data.TrailerData;
import com.ravinada.moviemania.Data.MovieDatabase;
import com.ravinada.moviemania.api.DataLoadingApi;
import com.ravinada.moviemania.api.TMDBApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class movieRepository {
    private static movieRepository repository;
    private MovieDao movieDao;
    private LiveData<Movie> movieLiveData;
    private LiveData<List<Movie>> moviesListLiveData;
    private MovieDatabase movieDatabase;
    private List<Movie> popularMovies = new ArrayList<>();
    private List<Movie> topRatedMovies = new ArrayList<>();
    private com.ravinada.moviemania.api.TMDBApi TMDBApi;

    static movieRepository getInstance() {
        if (repository == null) {
            repository = new movieRepository();
        }
        return repository;
    }

    private movieRepository() {
        TMDBApi = DataLoadingApi.createService(TMDBApi.class);
    }

    movieRepository(Application application) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        movieDao = movieDatabase.MovieDao();
        moviesListLiveData = movieDao.getMovies();
    }

    LiveData<List<Movie>> getAllMovies() {
        return moviesListLiveData;
    }

    void insert(Movie movie) {
        new insertAsyncTask(movieDao).execute(movie);
    }

    void delete(int movieId) {
        new deleteAsyncTask(movieDao).execute(movieId);
    }

    Movie getMovieById(int movieId) {
        Movie movie = null;
        try {
            movie = new getMovieByIdAsyncTask(movieDao).execute(movieId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private static class getMovieByIdAsyncTask extends AsyncTask<Integer, Void, Movie> {
        private MovieDao mAsyncTaskDao;
        getMovieByIdAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Movie doInBackground(Integer... integers) {
            return mAsyncTaskDao.getMovieById(integers[0]);
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {
        private MovieDao mAsyncTaskDao;
        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteMovie(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;
        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insertMovie(params[0]);
            return null;
        }
    }

    MutableLiveData<List<Movie>> getPopularMovies(String apiKey, String language, int page) {
        final MutableLiveData<List<Movie>> movieData = new MutableLiveData<>();
        TMDBApi.getPopularMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NotNull Call<MovieResponse> call,
                                   @NotNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    popularMovies.addAll(response.body().getMovies());
                    movieData.setValue(popularMovies);
                }
            }
            @Override
            public void onFailure(@NotNull Call<MovieResponse> call, Throwable t) {
                movieData.setValue(null);
            }
        });
        return movieData;
    }

    MutableLiveData<List<Movie>> getTopRatedMovies(String apiKey, String language, int page) {
        final MutableLiveData<List<Movie>> movieData = new MutableLiveData<>();
        TMDBApi.getTopRatedMovies(apiKey, language, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NotNull Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    topRatedMovies.addAll(response.body().getMovies());
                    movieData.setValue(topRatedMovies);
                }
            }

            @Override
            public void onFailure(@NotNull Call<MovieResponse> call, @NotNull Throwable t) {
                movieData.setValue(null);
            }
        });
        return movieData;
    }

    MutableLiveData<Movie> getMovieDetails(String apiKey, int movieId) {
        final MutableLiveData<Movie> movieData = new MutableLiveData<>();
        TMDBApi.getMovieDetails(movieId, apiKey).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NotNull Call<Movie> call, @NotNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movieData.setValue(null);
            }
        });
        return movieData;
    }

    MutableLiveData<List<TrailerData>> getTrailers(String apiKey, int movieId) {
        final MutableLiveData<List<TrailerData>> trailerData = new MutableLiveData<>();
        TMDBApi.getMovieTrailers(movieId, apiKey).enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(@NotNull Call<Trailer> call, @NotNull Response<Trailer> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getTrailerData() != null) {
                    List<TrailerData> trailers = new ArrayList<>();
                    for (TrailerData trailer : response.body().getTrailerData()) {
                        if (trailer.getType().toLowerCase().equals("trailer")
                                || trailer.getType().toLowerCase().equals("teaser")) {
                            trailers.add(trailer);
                        }
                    }
                    trailerData.setValue(trailers);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Trailer> call, Throwable t) {
                trailerData.setValue(null);
            }
        });
        return trailerData;
    }

    MutableLiveData<List<ReviewData>> getReviews(String apiKey, int movieId) {
        final MutableLiveData<List<ReviewData>> reviewData = new MutableLiveData<>();
        TMDBApi.getMovieReviews(movieId, apiKey).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, @NotNull Response<Review> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    reviewData.setValue(response.body().getReviewData());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Review> call, Throwable t) {
                reviewData.setValue(null);
            }
        });
        return reviewData;
    }
}
