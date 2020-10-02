package com.example.flixster;

import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieTrailerBinding;
import com.example.flixster.models.PopularMovie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import okhttp3.Headers;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class MovieTrailerActivity extends YouTubeBaseActivity {

  YouTubePlayerView youtubePlayerView;
  PopularMovie movie;

  private static final String YOUTUBE_API_KEY = "AIzaSyBJGxkR32iLPR1WL47-8e2Js4cyXEk84Tc";
  public static final String VIDEO_URL = "https://api.themoviedb" +
                                         ".org/3/movie/%d/videos?api_key" +
                                         "=a07e22bc18f5cb106bfe4cc1f83ad8ed";

  private ActivityMovieTrailerBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_trailer);

    youtubePlayerView = binding.player;

    //get Intent
    movie = Parcels.unwrap(getIntent().getParcelableExtra(PopularMovie.class.getSimpleName()));
    Log.d("MovieTrailerActivity", "Received Intent" + movie.toString() );

    // getting Response from Json
    AsyncHttpClient client = new AsyncHttpClient();
    client.get(String.format(VIDEO_URL, movie.getMovieID()), new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Headers headers, JSON json) {
        Log.d("MovieTrailerActivity", "Received Json response for popular movie");
        try {
          JSONArray results = json.jsonObject.getJSONArray("results");
          if(results.length()==0){
//            Glide.with(context).load(R.drawable.No_Video_64).into((player);
            return;
          }
          String youtubeTrailerKey = results.getJSONObject(0).getString("key");
          initializeYoutube(youtubeTrailerKey);

        }
        catch (JSONException e){
          Log.d("MovieTrailerActivity", "Json request did not succeed");
        }

      }

      @Override
      public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
        Log.d("MovieTrailerActivity", "Failed Response");
      }

      // initializing youtubePlayer
      public void initializeYoutube(final String youtubeTrailerKey){
        if (youtubeTrailerKey != null) {
          youtubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
              youTubePlayer.loadVideo(youtubeTrailerKey);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
              Log.d("MovieTrailerActivity", "YoutubePlayerView could not initialize");
            }
          });
        }
      }
    });


  }
}
