package com.example.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private LiveData<List<Movies>> favMovies;
    //private final String TAG = MainViewModel.class.getSimpleName();
    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase db = MovieDatabase.getInstance(this.getApplication());
        favMovies = db.moviesDao().loadAllMovies();
    }

    public LiveData<List<Movies>> getFavMovies() {
        return favMovies;
    }
}
