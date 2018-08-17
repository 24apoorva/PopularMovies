package com.example.android.popularmovies;

public class Reviews {
    private String mAuthor;
    private String mReview;

    Reviews(String author, String review){
        mAuthor = author;
        mReview = review;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getReview() {
        return mReview;
    }
}
