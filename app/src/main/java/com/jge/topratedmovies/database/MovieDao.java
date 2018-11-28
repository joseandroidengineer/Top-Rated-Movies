package com.jge.topratedmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.jge.topratedmovies.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM favorite ORDER BY id")
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);
}
