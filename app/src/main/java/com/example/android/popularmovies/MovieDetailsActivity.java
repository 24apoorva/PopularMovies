package com.example.android.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.MainActivity.API_KEY;
import static com.example.android.popularmovies.MainActivity.MOVIE_REQUEST_URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private static RequestQueue mRtQueue;
    private static Details toSendData;
    private final String LOG_TAG_DETAILS =MovieDetailsActivity.class.getSimpleName();
    private List<String> trailes_frag;
    public static int selectedMovieId;
    public static final String MOVIE_DATA = "movie_data";
    private final String REQUEST2_TAG = "request2";
    @BindView(R.id.movie_poster_iv)
    ImageView movie_image_iv;
    @BindView(R.id.rating_tv)
    TextView userRating_tv;
    @BindView(R.id.date_tv)
    TextView releaseDate_tv;
    @BindView(R.id.fav_view) Button fav_button_view;
    @BindView(R.id.fav_textview) TextView fav_textView;
    private MovieDatabase mDb;
    private boolean isMovieFav ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().hide();
        mDb = MovieDatabase.getInstance(getApplicationContext());
        Intent intent = getIntent();
        //Checking if the intent is null.
        if (intent == null) {
            closeOnError();
        }else  {
            //getting current movie
            Movies movie = intent.getParcelableExtra(MOVIE_DATA);
            selectedMovieId = movie.getMovieId();
        }
        String MOVIE_Details_REQUEST_URL = MOVIE_REQUEST_URL+selectedMovieId+"?api_key="+API_KEY+"&append_to_response=videos";
        movieDetailsRequest(MOVIE_Details_REQUEST_URL,this);

    }
    private void movieDetailsRequest(final String requestUrl, final Context context) {

        JsonObjectRequest mJsonOjRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String backdropPath = response.optString("backdrop_path");
                    int id = response.optInt("id");
                    String originalLang = response.optString("original_language");
                    String originalTitle = response.optString("original_title");
                    String overview = response.optString("overview");
                    String posterPath = response.optString("poster_path");
                    String date = response.optString("release_date");
                    double rating = response.optDouble("vote_average");
                    JSONObject videos = response.getJSONObject("videos");
                    JSONArray results = videos.getJSONArray("results");
                    trailes_frag = new ArrayList<>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currentMovie = results.optJSONObject(i);
                        String trailerKey = currentMovie.getString("key");
                        trailes_frag.add(trailerKey);
                    }
                    Details movieDetails = new Details(posterPath, id, originalTitle, overview, rating, date, backdropPath, originalLang, trailes_frag);
                    toSendData = movieDetails;
                    displayMovieData(movieDetails);
                    // Find the view pager that will allow the user to swipe between fragments
                    ViewPager viewPager = findViewById(R.id.viewpager);
                    // Create an adapter that knows which fragment should be shown on each page
                    FragmentSelectionAdapter adapter = new FragmentSelectionAdapter(getSupportFragmentManager(), context);

                    // Set the adapter onto the view pager
                    viewPager.setAdapter(adapter);
                    TabLayout tabLayout = findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(LOG_TAG_DETAILS, getResources().getString(R.string.http_error));
                if (MainActivity.isNetworkProblem(volleyError)) {
                    TextView tv = findViewById(R.id.details_internet_id);
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });
       // mJsonOjRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));
        mJsonOjRequest.setTag(REQUEST2_TAG);
        mRtQueue = AppSingleton.getInstance(context).getRequestQueue(context);
        mRtQueue.add(mJsonOjRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mRtQueue.cancelAll(REQUEST2_TAG);
    }

    /**
     * This method displays data using current movie data
     * @param currentMovie movie data will be in current movie.
     */
    private void displayMovieData(final Details currentMovie) {
        //getting title from current movie to display it as title.
        setTitle(currentMovie.getOriginalTitle());
        String path;
        if (currentMovie.getBackDropPath() == null) {
            path = "https://image.tmdb.org/t/p/w500/" + currentMovie.getBackDropPath();
        } else {
            path = "https://image.tmdb.org/t/p/w780/" + currentMovie.getPosterPath();
        }
        //Using Picasso to easily load album art thumbnails into views
        Picasso.with(this)
                .load(path)
                .placeholder(R.drawable.image)
                .error(R.drawable.error_image)
                .into(movie_image_iv);
        releaseDate_tv.setText(currentMovie.getReleaseDate());
//        tv_overview.setText(currentMovie.getOverView());
        userRating_tv.setText(String.valueOf(currentMovie.getUserRating()));
//        if(currentMovie.getTrailer().size() < 1){
//            trailer_header_tv.setVisibility(View.INVISIBLE);
//        }
        //displayTrailer(currentMovie.getTrailer());
        displayButtonFav(currentMovie);
        favoriteButtonClicked(currentMovie);
            }

    private void displayButtonFav(final Details movie){
        AppExecutors.getsInstance().getDiscIo().execute(new Runnable() {
            @Override
            public void run() {
                isMovieFav = mDb.moviesDao().isMovieFav(movie.getId());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isMovieFav){
                            buttonFav();
                        }else {
                            buttonNotFav();
                        }
                    }
                });

            }
        });
    }

    private void addToFavorites(Details movie){
        String path = movie.getPosterPath();
        int id = movie.getId();
        final FavMovies favMovies = new FavMovies(id,path, true);
        AppExecutors.getsInstance().getDiscIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.moviesDao().insertMovie(favMovies);
            }
        });
    }

    private void removeFromFavorites(Details movie){
        String path = movie.getPosterPath();
        int id = movie.getId();
        final FavMovies favMovies = new FavMovies(id,path, false);
        AppExecutors.getsInstance().getDiscIo().execute(new Runnable() {
            @Override
            public void run() {
                mDb.moviesDao().removeMovie(favMovies);
            }
        });
    }

    private void favoriteButtonClicked(final Details movie){
        fav_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fav_textView.getText() == getResources().getString(R.string.fav_rem)){
                    buttonNotFav();
                    removeFromFavorites(movie);


                }else{
                    buttonFav();
                    addToFavorites(movie);


                }

            }
        });
    }
    private void buttonFav(){
        fav_button_view.setBackgroundResource(R.drawable.ic_star_fav);
        fav_textView.setText(getResources().getString(R.string.fav_rem));
    }
    private void buttonNotFav(){
        fav_button_view.setBackgroundResource(R.drawable.ic_star);
        fav_textView.setText(getResources().getString(R.string.fav_add));
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message_moviedata, Toast.LENGTH_SHORT).show();
    }

    public static Details getToSendData() {
        return toSendData;
    }
}
