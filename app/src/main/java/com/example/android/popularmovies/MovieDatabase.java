package com.example.android.popularmovies;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {FavMovies.class},version = 1,exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME ="movieslist";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance (Context context){
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG,context.getString(R.string.new_db));
                sInstance = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class,MovieDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG,context.getString(R.string.get_db));
        return sInstance;
    }

    public abstract MoviesDao moviesDao();
}
