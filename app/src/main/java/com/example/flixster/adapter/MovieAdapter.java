package com.example.flixster.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;
import com.example.flixster.models.PopularMovie;
import org.parceler.Parcels;


import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  Context context;
  List<Object> movies;
  private final int MOVIE = 0, POSTER = 1;


  public MovieAdapter(Context context, List<Object> movies){
    this.context = context;
    this.movies = movies;

  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Log.d("MovieAdapter", "onCreateViewHolder" + viewType);
    // check for popular movies
    View view;
    LayoutInflater inflater = LayoutInflater.from(context);
    if(viewType == MOVIE){
       view = inflater.inflate(R.layout.item_movie, parent, false);
      return new ViewHolder1(view);
    }
    else if(viewType == POSTER) {
       view = inflater.inflate(R.layout.item_popular_movie, parent, false);
      return new ViewHolder2(view);
    }

    return null;

  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    Log.d("MovieAdapter", "onBindViewHolder" + position);
    Object obj = movies.get(position);
    int viewType = holder.getItemViewType();

    switch (viewType) {
      case MOVIE:
        ((ViewHolder1) holder).bindMovie((Movie) obj);
        break;
      case POSTER:
        ((ViewHolder2) holder).bindPopularMovie((PopularMovie) obj);
        break;
      default:
        break;
    }
  }



  @Override
  public int getItemCount() {
    return movies.size();
  }

  @Override
  public int getItemViewType(int position) {
    Object item = movies.get(position);
    if(item instanceof Movie){
      return MOVIE;
    } if (item instanceof PopularMovie) {
      return POSTER;
    }

    return 0;
  }


  //ViewHolder1 for movies
  public class ViewHolder1 extends RecyclerView.ViewHolder {
    RelativeLayout movieContainer;
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;


    public ViewHolder1(@NonNull View itemView) {
      super(itemView);
      tvTitle = itemView.findViewById(R.id.tvTitle);
      tvOverview = itemView.findViewById(R.id.tvOverview);
      ivPoster = itemView.findViewById(R.id.ivPoster);
      movieContainer = itemView.findViewById(R.id.movieContainer);
    }

    public void bindMovie(final Movie movie ) {
      tvTitle.setText(movie.getTitle());
      tvOverview.setText(movie.getOverview());
      String imageUrl;
      if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
        imageUrl = movie.getBackdropPath();
      }
      else{
        imageUrl = movie.getPosterPath();
      }
      Glide.with(context).load(imageUrl).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(ivPoster);
      movieContainer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
          int position = getAdapterPosition();
          if(position != RecyclerView.NO_POSITION){
            Log.d("DetailActivity", "OnClick" + position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
            context.startActivity(intent);
          }

        }
      });
    }
  }// ViewHolder ends


  // ViewHolder2 for Popular movies
  public class ViewHolder2 extends RecyclerView.ViewHolder{

    ImageView ivBackdrop;

    public ViewHolder2(@NonNull View itemView) {
      super(itemView);
      ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
    }

    public void bindPopularMovie(PopularMovie movie ){
      String url = movie.getBackdropPath();
      Glide.with(context).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)).into(ivBackdrop);
    }
  }
}
