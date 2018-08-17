package com.example.android.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.android.popularmovies.MainActivity.API_KEY;
import static com.example.android.popularmovies.MainActivity.MOVIE_REQUEST_URL;
import static com.example.android.popularmovies.MovieDetailsActivity.selectedMovieId;


public class ReviewsFragment extends Fragment {

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ListView lv = view.findViewById(R.id.reviews_list_id);
        TextView tv = view.findViewById(R.id.tv_review_no);
        String REVIEWS_URL = "/reviews?api_key=";
        String url = MOVIE_REQUEST_URL+selectedMovieId+ REVIEWS_URL +API_KEY;
        movieReviewRequest(url,lv,tv,getContext());
        return view;
    }
    public void movieReviewRequest(final String requestUrl, final ListView listView, final TextView textView, Context context) {
        final ArrayList<Reviews> reviewDetails = new ArrayList<>();
        JsonObjectRequest mJsonOjRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.print("review:::" + response.toString());
                JSONArray reviewsArray = response.optJSONArray("results");
                for (int i = 0; i < reviewsArray.length(); i++) {
                    JSONObject currentMovie = reviewsArray.optJSONObject(i);
                    String name = currentMovie.optString("author");
                    String review = currentMovie.optString("content");
                    Reviews movieReview = new Reviews(name, review);
                    reviewDetails.add(movieReview);
                }
                if (reviewDetails.size() == 0) {
                    displayTextView(listView, textView);
                } else {
                    displayReviews(reviewDetails, listView);
                }

            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volly", "Error while jsonrequest");
            }
        });
        // mJsonOjRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 10, 1.0f));
        String REQUEST3_TAG = "request3";
        mJsonOjRequest.setTag(REQUEST3_TAG);
        RequestQueue mRtQueue = AppSingleton.getInstance(context).getRequestQueue(context);
        mRtQueue.add(mJsonOjRequest);

    }

    private void displayTextView(ListView listView,TextView textView) {
        textView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void displayReviews(ArrayList<Reviews> reviewDetails,ListView listView) {
        CustomReviewAdapter mArrayAdapter = new CustomReviewAdapter(getContext(), reviewDetails);
        listView.setAdapter(mArrayAdapter);
    }

}
