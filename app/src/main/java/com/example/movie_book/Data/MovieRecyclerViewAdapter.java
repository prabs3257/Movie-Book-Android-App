package com.example.movie_book.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_book.Activities.DetailsActivity;
import com.example.movie_book.Model.Movie;
import com.example.movie_book.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>{

    Context context;
    List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {

        this.context = context;
        this.movieList = movies;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewAdapter.ViewHolder holder, int position) {

        Movie movie  = movieList.get(position);

        String posterLink = movie.getPoster();
        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());
        holder.year.setText("Released : " + movie.getYear());

        Picasso.with(context).load(posterLink).placeholder(R.drawable.ic_launcher_foreground).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView year;
        TextView type;
        ImageView poster;

        public ViewHolder(View itemView , Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView)itemView.findViewById(R.id.movieTitleId);
            year = (TextView)itemView.findViewById(R.id.movieReleaseId);
            type = (TextView)itemView.findViewById(R.id.movieCatId);
            poster = (ImageView)itemView.findViewById(R.id.movieImageId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movieList.get(getAdapterPosition());

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("movie", movie);
                    context.startActivity(intent);


                }
            }
            );

        }

        @Override
        public void onClick(View v) {

        }
    }
}
