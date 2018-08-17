package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable{
    private String mMoviePosterPath;
    private int mMovieId ;


    public Movies(String moviePosterPath, int movieId) {
        mMoviePosterPath = moviePosterPath;
        mMovieId = movieId;

    }

    protected Movies(Parcel in) {
        mMoviePosterPath = in.readString();
        mMovieId = in.readInt();

    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public int getMovieId() {
        return mMovieId;
    }

    public String getMoviePosterPath() {
        return mMoviePosterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMoviePosterPath);
        dest.writeInt(mMovieId);
    }
}