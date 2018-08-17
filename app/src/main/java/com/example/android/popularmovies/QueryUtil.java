package com.example.android.popularmovies;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtil {
    private static final String LOG_TAG = QueryUtil.class.getSimpleName();

    /**
     * To retrieve movies data
     * @param requestUrl -- input url
     * @return it returns List array of Movies
     */

    public static List<Movies> getFullMoviesData(String requestUrl) {
        //create Url object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.http_error), e);
        }
        //retrieve Movies data from Json Response.
        return extractMovieDetails(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, String.valueOf(R.string.url_error_message), e);
        }
        return url;
    }

    //This method makes Http request and returns Json response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/* milliseconds */);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                //reads using readFromStream method.
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, String.valueOf(R.string.error_code) + urlConnection.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, String.valueOf(R.string.http_request_error), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Movies} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Movies> extractMovieDetails(String moviesJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(moviesJson)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding movies to
        List<Movies> moviesData = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(moviesJson);
            JSONArray moviesArray = baseJsonResponse.optJSONArray("results");
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject currentMovie = moviesArray.optJSONObject(i);
                String poster = currentMovie.optString("poster_path");
                String backDropPoster = currentMovie.optString("backdrop_path");
                String originalTitle = currentMovie.optString("original_title");
                String overView = currentMovie.optString("overview");
                double userRating = currentMovie.optDouble("vote_average");
                String releaseDate = currentMovie.optString("release_date");
                Movies data = new Movies(poster, backDropPoster, originalTitle, overView, userRating, releaseDate);
                moviesData.add(data);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", String.valueOf(R.string.movies_parsing_error), e);
        }
        return  moviesData;
    }
}
