package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM favMoviesList")
    LiveData<List<FavMovies>> loadFavMovies();

    @Query("SELECT mMoviePosterPath,mMovieId FROM favMoviesList")
    LiveData<List<Movies>> loadAllMovies();

    @Insert
    void insertMovie(FavMovies favMovie);

    @Delete
    void removeMovie(FavMovies favMovie);

    @Query("SELECT mFavorite FROM favMoviesList where mMovieId = :id")
    boolean isMovieFav(int id);


}
