package com.ravinada.moviemania.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ravinada.moviemania.Data.Movie;
import com.ravinada.moviemania.Data.ReviewData;
import com.ravinada.moviemania.Data.TrailerData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private static final String TMDB_API_KEY = "008a13dea7f48ee34232d0578591c622";
    private static final String LANGUAGE = "en-US";
    private MutableLiveData<List<Movie>> popularMovieData;
    private MutableLiveData<List<Movie>> topRatedMovieData;
    private movieRepository repository;
    private movieRepository repositoryRoom;
    private LiveData<List<Movie>> mListMovies;

    public ViewModel(Application application){
        super(application);
        repositoryRoom = new movieRepository(application);
        mListMovies = repositoryRoom.getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return mListMovies;
    }

    public Movie getMovieThroughId(int movieId) {
        return repositoryRoom.getMovieById(movieId);
    }

    public void insert(Movie movie) {
        repositoryRoom.insert(movie);
    }

    public void delete(int movieId) {
        repositoryRoom.delete(movieId);
    }

    public void init(){
        if(popularMovieData != null){
            return;
        }
        repository = movieRepository.getInstance();
        popularMovieData = repository.getPopularMovies(TMDB_API_KEY, LANGUAGE, 1);
        topRatedMovieData = repository.getTopRatedMovies(TMDB_API_KEY, LANGUAGE, 1);
    }

    public MutableLiveData<List<Movie>> getPopularMovies(int pageNumber){
        if(pageNumber == 1){
            return popularMovieData;
        }
        popularMovieData = repository.getPopularMovies(TMDB_API_KEY, LANGUAGE, pageNumber);
        return popularMovieData;
    }

    public MutableLiveData<List<Movie>> getTopRatedMovies(int pageNumber){
        if(pageNumber == 1){
            return topRatedMovieData;
        }
        topRatedMovieData = repository.getTopRatedMovies(TMDB_API_KEY, LANGUAGE, pageNumber);
        return topRatedMovieData;
    }

    public MutableLiveData<List<TrailerData>> getTrailerData(int movieId){
        return repository.getTrailers(TMDB_API_KEY, movieId);
    }

    public MutableLiveData<List<ReviewData>> getReviewData(int movieId){
        return repository.getReviews(TMDB_API_KEY, movieId);
    }

    public MutableLiveData<Movie> getMovieDetails(int movieId){
        return repository.getMovieDetails(TMDB_API_KEY, movieId);
    }
}
