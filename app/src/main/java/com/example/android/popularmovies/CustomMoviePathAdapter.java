package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CustomMoviePathAdapter extends ArrayAdapter<Movies> {
    private  Context context;
    private  LayoutInflater inflater;
    private  List<Movies> mMovieData;

    CustomMoviePathAdapter(Context context, ArrayList<Movies> movieData) {
        super(context, R.layout.poster_grid, movieData);
        this.context = context;
        this.mMovieData = movieData;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.poster_grid, parent, false);
        }
        ImageView posterView = convertView.findViewById(R.id.pos_id);
        String path = "https://image.tmdb.org/t/p/w185";
        String path2 = mMovieData.get(position).getMoviePosterPath();

        Picasso.with(context)
                .load(path + path2)
                .fit()
                .placeholder(R.color.color_black)
                .error(R.drawable.error_image)
                .into(posterView);
        return convertView;
    }
}


