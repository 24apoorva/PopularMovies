package com.example.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    public static String MOVIE_REQUEST_URL = "https://api.themoviedb.org/3/movie/";
    public static String API_KEY = "";
    private final String MAIN_TAG = MainActivity.class.getSimpleName();
    private final String MOVIES_STRING = "movies";
    private final int ID_POP = 1;
    private final int ID_RATING = 2;
    private final int ID_FAV = 3;
    private final String REQUEST1_TAG1 = "request1";
    @BindView(R.id.grid)
    GridView moviesGridView;
    @BindView(R.id.internet_tv)
    TextView internet_tv;
    private int loader_id;
    private List<Movies> popular_movies;
    private String SORT_POP = "popular?api_key=";
    private String SORT_RATING = "top_rated?api_key=";
    private CustomMoviePathAdapter mAdapter;
    private CustomMoviePathAdapter mFavAdapter;
    private RequestQueue mRequestQueue;
    private MovieDatabase mDb;
    private JsonObjectRequest mJsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDb = MovieDatabase.getInstance(getApplicationContext());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_STRING)) {
                mAdapter.addAll(savedInstanceState.<Movies>getParcelableArrayList(MOVIES_STRING));
            }
        } else {
            loader_id = ID_POP;
            volleyRequest(MOVIE_REQUEST_URL + SORT_POP + API_KEY, this);
        }

        //Setting onclickItemListener.
        moviesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchMovieDetailActivity(position);
            }
        });

    }

    /**
     * Api request and Json parsing happens in this method.
     * @param requestUrl -- Url we have to pass
     * @param context -- context
     */
    private void volleyRequest(String requestUrl, Context context) {
        final List<Movies> moviesData = new ArrayList<>();
        mAdapter = new CustomMoviePathAdapter(this, new ArrayList<Movies>());
        mRequestQueue = AppSingleton.getInstance(context).getRequestQueue(context);

         mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray moviesArray = response.optJSONArray("results");
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject currentMovie = moviesArray.optJSONObject(i);
                    String poster = currentMovie.optString("poster_path");
                    int movieId = currentMovie.optInt("id");
                    Movies data = new Movies(poster, movieId);
                    moviesData.add(data);
                    popular_movies = moviesData;
                }
                //adding all the movies to mAdapter
                mAdapter.addAll(moviesData);
                //Displaying the added movies in grid Layout.
                moviesGridView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(MAIN_TAG, getResources().getString(R.string.http_error));
                if(isNetworkProblem(volleyError)){
                    setVisibility(GONE,VISIBLE);
                }

            }
        });
        mJsonObjectRequest.setTag(REQUEST1_TAG1);
        //Adding request to requestQueue
        mRequestQueue.add(mJsonObjectRequest);
    }


    @Override
    protected void onStop() {
        super.onStop();
        //cancelling the request with tag reference.
        mRequestQueue.cancelAll(REQUEST1_TAG1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_STRING, (ArrayList<? extends Parcelable>) popular_movies);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.addAll(savedInstanceState.<Movies>getParcelableArrayList(MOVIES_STRING));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sortorder, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * When menu items are slected according to the input data will be displayed
     * @param item -- item clicked by the user
     * @return -- boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        setVisibility(VISIBLE,GONE);
        switch (id) {
            case R.id.popular_id:
                loader_id = ID_POP;
                setTitle(getResources().getString(R.string.app_name));
                volleyRequest(MOVIE_REQUEST_URL + SORT_POP + API_KEY, this);
                return true;

            case R.id.highest_rating_id:
                loader_id = ID_RATING;
                setTitle(getResources().getString(R.string.title_highest_rated_movies));
                volleyRequest(MOVIE_REQUEST_URL + SORT_RATING + API_KEY, this);
                return true;

            case R.id.favorite_movies_id:
                setTitle(getResources().getString(R.string.fav_movies_title));
                loader_id = ID_FAV;
                displayFavMovies();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * OnResume according to the loader_id data will be updated.
     */
    @Override
    protected void onResume() {
        super.onResume();
        switch (loader_id) {
            case ID_POP:
                volleyRequest(MOVIE_REQUEST_URL + SORT_POP + API_KEY, this);
                break;
            case ID_RATING:
                volleyRequest(MOVIE_REQUEST_URL + SORT_RATING + API_KEY, this);
                break;
            case ID_FAV:
                displayFavMovies();
                break;
        }

    }

    /**
     * This method is used to display favorite movies of the user.
     */
    private void displayFavMovies() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavMovies().observe(this, new Observer<List<Movies>>() {
            @Override
            public void onChanged(@Nullable List<Movies> movies) {
                popular_movies = movies;
                mFavAdapter = new CustomMoviePathAdapter(MainActivity.this, new ArrayList<Movies>());
                if (movies.size() == 0) {
                   setVisibility(GONE,VISIBLE);
                    internet_tv.setText(getResources().getString(R.string.no_fav_movies));
                } else {
                    mFavAdapter.addAll(movies);
                    moviesGridView.setAdapter(mFavAdapter);
                }
            }
        });


    }

  /*
   * This method is used to set the visibility of the layout.
   */
    private void setVisibility(int gridViewVisibility, int textViewVisibility) {
        moviesGridView.setVisibility(gridViewVisibility);
        internet_tv.setVisibility(textViewVisibility);
    }

    public static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

}
