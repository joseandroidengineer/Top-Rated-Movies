package com.jge.topratedmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private List<Movie> movies;
    private final ListItemClickListener mOnClickListener;


    public MovieAdapter(ListItemClickListener mOnClickListener){

        this.mOnClickListener = mOnClickListener;
    }

    public interface ListItemClickListener{
        void onListItemClick(Movie movieNameIndexClicked);
    }



    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView moviePosterImageView;
        public TextView movieTitleTextView;

        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.poster_image_iv);
            movieTitleTextView = itemView.findViewById(R.id.movie_title_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(movies.get(position));
        }
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int i) {
        Movie movie = movies.get(i);
        movieAdapterViewHolder.movieTitleTextView.setText(movie.getTitle());
        Picasso.with(movieAdapterViewHolder.moviePosterImageView.getContext())
                .load(NetworkUtils.buildImageUrl(movie.getPosterImagePath()))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(movieAdapterViewHolder.moviePosterImageView);
    }

    @Override
    public int getItemCount() {
        if(movies == null) return 0;
        return movies.size();
    }

    public void setMovieData(List<Movie> movieData){
        movies = movieData;
        notifyDataSetChanged();
    }
}
