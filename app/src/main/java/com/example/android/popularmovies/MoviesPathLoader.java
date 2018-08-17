package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class MoviesPathLoader extends AsyncTaskLoader<List<Movies>>{
    private String mUrl;
    MoviesPathLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movies> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtil.getFullMoviesData(mUrl);
    }
}


