package com.example.flixster;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapter.MovieAdapter;
import com.example.flixster.models.Movie;
import com.example.flixster.models.PopularMovie;
import okhttp3.Headers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String NOW_PLAYING_URL = "https://api.themoviedb" +
                                               ".org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  public static final String TAG = "MainActivity";
  static  List<Object> movies;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView rvMovies = findViewById(R.id.rvMovies);
    movies = new ArrayList<>();
    //Creating Adapter
    final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

    //Set the adapter to the recycle view
    rvMovies.setAdapter(movieAdapter);

    //Set Layout manager on the recycler view.
    rvMovies.setLayoutManager(new LinearLayoutManager(this));

    AsyncHttpClient client = new AsyncHttpClient();
    client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.d(TAG, "onSuccess: Passed");
        JSONObject jsonObject = json.jsonObject;
        try {
          JSONArray results = jsonObject.getJSONArray("results");
          Log.i(TAG, "Results: " + results.toString());
          fromJsonArray(results);
          movieAdapter.notifyDataSetChanged();
          Log.i(TAG, "Movies:" + movies.size());

        } catch (JSONException e) {

          Log.e(TAG, "exception caught");
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.d(TAG, "onFailure : Failed");
      }
    });


  }
  // getting jsonArray into the list.
  public static List<Object> fromJsonArray(JSONArray movieJsonArray) throws JSONException {

     for(int index = 0; index < movieJsonArray.length(); index++){
       JSONObject obj = movieJsonArray.getJSONObject(index);
       if (obj.getDouble("vote_average") <= 7) {
         movies.add(new Movie(obj));
       } else {
         movies.add(new PopularMovie(obj));
       }
     }
     return movies;
  }
}
