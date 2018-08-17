package com.example.android.popularmovies;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Details implements Parcelable{
    private String mPosterPath;
    private int mId;
    private String mOriginalTitle;
    private String mOverView;
    private double mUserRating;
    private String mReleaseDate;
    private String mBackDropPath;
    private String mOriginalLanguage;
    private List<String> mTrailer;


    Details(String path,int id,String originalTitle,String overView, double userRating,
            String releaseDate, String backdropPath,String originalLanguage, List<String> trailer) {
        mPosterPath = path;
        mId = id;
        mOriginalTitle = originalTitle;
        mOverView = overView;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mBackDropPath = backdropPath;
        mOriginalLanguage = originalLanguage;
        mTrailer = trailer;
    }

    protected Details(Parcel in) {
        mPosterPath = in.readString();
        mId = in.readInt();
        mOriginalTitle = in.readString();
        mOverView = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();
        mBackDropPath = in.readString();
        mOriginalLanguage = in.readString();
        mTrailer = in.createStringArrayList();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public List<String> getTrailer() {
        return mTrailer;
    }

    public int getId() {  return mId;    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverView() {
        return mOverView;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public String getBackDropPath() {
        return mBackDropPath;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPosterPath);
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverView);
        dest.writeDouble(mUserRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mBackDropPath);
        dest.writeString(mOriginalLanguage);
        dest.writeStringList(mTrailer);
    }
}
