package com.example.android.popularmovies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favMoviesList")
public class FavMovies {
    @PrimaryKey
    protected int mMovieId;
    public String mMoviePosterPath;
    public boolean mFavorite;

    FavMovies(int movieId, String moviePosterPath, boolean favorite){
        mMovieId =movieId;
        mMoviePosterPath = moviePosterPath;
        mFavorite = favorite;
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public String getmMoviePosterPath() {
        return mMoviePosterPath;
    }

    public boolean getmFavorite(){
        return mFavorite;
    }
}
