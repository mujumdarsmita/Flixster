package com.example.flixster;

import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import okhttp3.Headers;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class DetailActivity extends YouTubeBaseActivity {

  private static final String YOUTUBE_API_KEY = "AIzaSyBJGxkR32iLPR1WL47-8e2Js4cyXEk84Tc";
  public static final String VIDEO_URL = "https://api.themoviedb" +
                                         ".org/3/movie/%d/videos?api_key" +
                                         "=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  TextView tvTitle;
  TextView tvOverview;
  TextView tvDate;
  RatingBar rbVoterAverage;
  Movie movie;
  YouTubePlayerView youTubePlayerView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);

    tvTitle = findViewById(R.id.tvTitle);
    tvOverview = findViewById(R.id.tvOverview);
    rbVoterAverage = findViewById(R.id.ratingBar);
    youTubePlayerView = findViewById(R.id.player);
    tvDate = findViewById(R.id.tvDate);

    // Unwraping the bundle from the intent
    movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
    Log.d("MovieDetailsActivity", "Parcel received");


    // getting response from Video API
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(String.format(VIDEO_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.d("DetailActivity", "onSuccess:  Video API Passed " + movie.getMovieID() );
        try {
          JSONArray results = json.jsonObject.getJSONArray("results");
          Log.i("DetailActivity", "Results: " + results.toString());
          if(results.length()==0){
            // Put a error place holder Image;
            return;
          }
          String youtubeVideokey = results.getJSONObject(0).getString("key");
          Log.i("DetailActivity", "Results: " + youtubeVideokey);
          initializeYoutube(youtubeVideokey);
        }
        catch (JSONException e){
          Log.d("DetailActivity", "onFailure: Failed to parse Json");
        }

      }

      private void initializeYoutube(final String youtubeVideokey) {
        // Initializing Youtube API
        if(youtubeVideokey!=null) {
          youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
              Log.d("DetailActivity", "Succeeded Youtube Player display");
              youTubePlayer.cueVideo(youtubeVideokey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
              Log.d("DetailActivity", "Failed");
            }
          });
        }
      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.d("DetailActivity", "onFailure: Failed");
      }
    });




    tvTitle.setText(movie.getTitle());
    tvOverview.setText(movie.getOverview());
    tvDate.setText(movie.getDate());
    Float voteAverage = movie.getVoteAverage().floatValue();
    rbVoterAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


  }

}
