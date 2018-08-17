package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DATA = "movie_data";
    @BindView(R.id.movie_poster_iv)
    ImageView movie_image_iv;
    @BindView(R.id.movie_title_tv)
    TextView movieTitle_tv;
    @BindView(R.id.rating_tv)
    TextView userRating_tv;
    @BindView(R.id.overview_tv)
    TextView movieOverview_tv;
    @BindView(R.id.date_tv)
    TextView releaseDate_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().hide();

        Intent intent = getIntent();
        //Checking if the intent is null.
        if (intent == null) {
            closeOnError();
        }
        Movies currentMovie = null;
        if (intent != null) {
            //getting current movie
            currentMovie = intent.getParcelableExtra(MOVIE_DATA);
        }

        if (currentMovie == null) {
            closeOnError();
        }
        //getting title from current movie to display it as title.
        setTitle(currentMovie.getOriginalTitle());
        displayMovieData(currentMovie);


    }

    /**
     * This method displays data using current movie data
     * @param currentMovie movie data will be in current movie.
     */
    private void displayMovieData(Movies currentMovie) {
        String path;
        if (currentMovie.getmBackDropPath() == null) {
            path = "https://image.tmdb.org/t/p/w500/" + currentMovie.getmBackDropPath();
        } else {
            path = "https://image.tmdb.org/t/p/w780/" + currentMovie.getPosterPath();
        }
        //Using Picasso to easily load album art thumbnails into views
        Picasso.with(this)
                .load(path)
                .placeholder(R.drawable.image)
                .error(R.drawable.error_image)
                .into(movie_image_iv);
        movieTitle_tv.setText(currentMovie.getOriginalTitle());
        releaseDate_tv.setText(currentMovie.getReleaseDate());
        movieOverview_tv.setText(currentMovie.getOverView());
        userRating_tv.setText(String.valueOf(currentMovie.getUserRating()));

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message_moviedata, Toast.LENGTH_SHORT).show();
    }
}

