package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class CustomMoviePathAdapter extends ArrayAdapter<Movies> {
    private Context context;
    private LayoutInflater inflater;
    private List<Movies> mMovieData;

    public CustomMoviePathAdapter(Context context, ArrayList<Movies> movieData) {
        super(context, R.layout.poster_grid, movieData);
        this.context = context;
        this.mMovieData = movieData;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.poster_grid, parent, false);
        }
        ImageView posterView = (ImageView)convertView.findViewById(R.id.pos_id);
        String path = "http://image.tmdb.org/t/p/w185";
        String path2 = mMovieData.get(position).getPosterPath();

        Picasso.with(context)
                .load(path + path2)
                .fit()
                .placeholder(R.drawable.image)
                .error(R.drawable.error_image)
                .into(posterView);
        TextView textView = (TextView) convertView.findViewById(R.id.title_tv);
        textView.setText(mMovieData.get(position).getOriginalTitle());
        return convertView;
    }
}


