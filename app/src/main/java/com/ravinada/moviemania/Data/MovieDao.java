package com.ravinada.moviemania.Data;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getMovies();

    @Query("SELECT * FROM movie where id = :movieId")
    Movie getMovieById(int movieId);

    @Query("DELETE FROM movie where id = :movieId")
    void deleteMovie(int movieId);
}