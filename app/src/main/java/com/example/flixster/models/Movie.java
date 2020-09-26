package com.example.flixster.models;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
  String posterPath;
  String backdropPath;
  String title;
  String overview;
  Double voteAverage;



  public String getPosterPath() {
    return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
  }

  public String getBackdropPath(){
    return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
  }

  public String getTitle() {
    return title;
  }

  public String getOverview() {
    return overview;
  }

  public Double getVoteAverage(){
    return voteAverage;
  }

  public Movie(JSONObject jsonObject) throws JSONException {
    posterPath = jsonObject.getString("poster_path");
    backdropPath = jsonObject.getString("backdrop_path");
    title = jsonObject.getString("title");
    overview = jsonObject.getString("overview");
    voteAverage = jsonObject.getDouble("vote_average");
  }


}
