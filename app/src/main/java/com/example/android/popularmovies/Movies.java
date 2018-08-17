package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable{
    private String mPosterPath;
    private String mOriginalTitle;
    private String mOverView;
    private double mUserRating;
    private String mReleaseDate;
    private String mBackDropPath;


    Movies(String path, String backDropPath, String title, String overView, double rating, String releaseDate) {
        mPosterPath = path;
        mBackDropPath = backDropPath;
        mOriginalTitle = title;
        mOverView = overView;
        mUserRating = rating;
        mReleaseDate = releaseDate;
    }

    private Movies(Parcel in) {
        mPosterPath = in.readString();
        mOriginalTitle = in.readString();
        mOverView = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();
        mBackDropPath = in.readString();
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

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getmBackDropPath() {
        return mBackDropPath;
    }

    public String getOverView() {
        return mOverView;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverView);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mBackDropPath);
    }
}
