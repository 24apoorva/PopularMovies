package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movies>> {
    //public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 1;
    private static final int MOVIES_LOADER_HIGH_ID = 2;
    private static final String MOVIE_REQUEST_POP_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String MOVIE_REQUEST_RATING_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";
    static List<Movies> popular_movies;
    int currentLoader = MOVIES_LOADER_ID;
    @BindView(R.id.grid)
    GridView moviesGridView;
    @BindView(R.id.internet_tv)
    TextView internet_tv;
    private CustomMoviePathAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAdapter = new CustomMoviePathAdapter(this, new ArrayList<Movies>());
        //Checking internet connection.
        checkNetworkConnection();
        getSupportLoaderManager().initLoader(this.currentLoader, null, this).forceLoad();
        //Setting onclickItemListener.
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchMovieDetailActivity(position);
            }
        });
        moviesGridView.setAdapter(mAdapter);

    }

    /**
     * This activity launches Movie details Activity when an item is clicked.
     * @param position is the position of the item clicked.
     */
    private void launchMovieDetailActivity(int position) {
        Intent movieDeatilIntent = new Intent(this, MovieDetailsActivity.class);
        movieDeatilIntent.putExtra(MovieDetailsActivity.MOVIE_DATA, popular_movies.get(position));
        startActivity(movieDeatilIntent);
    }


     // This method checks the network connection.
    private void checkNetworkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            moviesGridView.setVisibility(View.VISIBLE);
            internet_tv.setVisibility(View.GONE);


        } else {
            //when there is no network connection grid view will be gone and text view will be visible.
            moviesGridView.setVisibility(View.GONE);
            internet_tv.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortorder, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular_id) {
            setTitle(getResources().getString(R.string.app_name));
            this.currentLoader = MOVIES_LOADER_ID;
            loadMovies(MOVIES_LOADER_ID);
            return true;

        }
        if (id == R.id.highest_rating_id) {
            setTitle("Highest Rated Movies");
            this.currentLoader = MOVIES_LOADER_HIGH_ID;
            loadMovies(MOVIES_LOADER_HIGH_ID);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.currentLoader == MOVIES_LOADER_ID) {
            getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this).forceLoad();
        } else {
            getSupportLoaderManager().initLoader(MOVIES_LOADER_HIGH_ID, null, this).forceLoad();
        }
    }

    /**
     * This method loads data .
     * @param id is the id for the loader.
     */
    private void loadMovies(int id) {
        getSupportLoaderManager().initLoader(id, null, this).forceLoad();
    }

    @NonNull
    @Override
    public Loader<List<Movies>> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == MOVIES_LOADER_ID) {
            return new MoviesPathLoader(this, MOVIE_REQUEST_POP_URL);
        } else {
            return new MoviesPathLoader(this, MOVIE_REQUEST_RATING_URL);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movies>> loader, List<Movies> data) {
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            popular_movies = data;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movies>> loader) {
        mAdapter.clear();

    }
}
