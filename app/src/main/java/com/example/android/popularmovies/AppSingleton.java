package com.example.android.popularmovies;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppSingleton {
    private RequestQueue mRequestQueue;
    private static AppSingleton mInstance;

    public static AppSingleton getInstance(Context context) {
        if(mInstance == null){
            mInstance = new AppSingleton(context);
        }
        return mInstance;
    }

    private AppSingleton(Context context){
       mRequestQueue = getRequestQueue(context);
    }

    public RequestQueue getRequestQueue(Context context) {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
            }
        return mRequestQueue;
    }
}
