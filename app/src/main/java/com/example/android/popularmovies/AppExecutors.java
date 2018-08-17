package com.example.android.popularmovies;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final Object LOCK =new Object();
    private static AppExecutors sInstance;
    private final Executor discIo;

    private AppExecutors(Executor discIo){
        this.discIo = discIo;
    }

    public static AppExecutors getsInstance(){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor getDiscIo(){
        return discIo;
    }
}
