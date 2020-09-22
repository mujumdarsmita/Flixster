package com.example.flixster;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import okhttp3.Headers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  public static final String NOW_PLAYING_URL = "https://api.themoviedb" +
                                               ".org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  public static final String TAG = "MainActivity";
  List<Movie> movies;
  public static final String IMG_URL = "https://api.themoviedb" +
                                       ".org/3/configuration?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AsyncHttpClient client = new AsyncHttpClient();
    client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.d(TAG, "onSuccess: Passed");
        JSONObject jsonObject = json.jsonObject;
        try {
          JSONArray results = jsonObject.getJSONArray("results");
          Log.i(TAG, "Results: " + results.toString());
          movies = Movie.fromJsonArray(results);
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

    // configuring image URL
    client.get(IMG_URL, new JsonHttpResponseHandler(){

      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        JSONObject obj = json.jsonObject;

        try {
          Log.i(TAG, "Image: " + obj.getString("images"));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.d(TAG, "onFailure : Failed");
      }
    });

  }
}
