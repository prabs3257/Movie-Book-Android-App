package com.example.movie_book.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie_book.Model.Movie;
import com.example.movie_book.R;
import com.example.movie_book.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView director;
    private TextView category;
    private TextView rating;
    private TextView writer;
    private TextView plot;
    private TextView boxOffice;
    private TextView runTime;
    private RequestQueue queue;
    private String movieId;
    private TextView actors;
    private TextView awards;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        queue = Volley.newRequestQueue(this);

        movie = (Movie)getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        setUpUI();
        getMovieDetails(movieId);

    }

    private void getMovieDetails(String movieId) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT_DET + movieId + Constants.URL_RIGHT_DET
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.has("Ratings")){
                        JSONArray ratings = response.getJSONArray("Ratings");
                        String source = null;
                        String value = null;

                        if(ratings.length() > 0){

                            JSONObject mRatings  = ratings.getJSONObject(ratings.length()-1);
                            source = mRatings.getString("Source");
                            value = mRatings.getString("Value");

                            rating.setText("Rating: " + source + " : " + value);
                        }
                    }else{

                        rating.setText("Ratings : N/A");
                    }

                    movieTitle.setText(response.getString("Title"));
                    movieYear.setText("Released: " + response.getString("Year"));
                    director.setText("Directed By: " + response.getString("Director"));
                    writer.setText("Writers: " + response.getString("Writer"));
                    plot.setText("Plot: " + response.getString("Plot"));
                    runTime.setText("Runtime: " + response.getString("Runtime"));
                    actors.setText("Actors: "+ response.getString("Actors"));
                    category.setText("Genre: " + response.getString("Genre"));
                    awards.setText("Awards: " + response.getString("Awards"));

                    Picasso.with(getApplicationContext())
                            .load(response.getString("Poster"))
                            .into(movieImage);
                    boxOffice.setText("Box Office: " + response.getString("BoxOffice"));


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error:", error.getMessage());

            }
        });
        queue.add(jsonObjectRequest);
    }

    public void setUpUI(){

        movieTitle = (TextView)findViewById(R.id.movieTitleIDDets);
        movieYear = (TextView)findViewById(R.id.movieReleaseIDDets);
        movieImage = (ImageView)findViewById(R.id.movieImageIDDets);
        director = (TextView)findViewById(R.id.directedByIDDets);
        category = (TextView)findViewById(R.id.movieCatIDDets);
        rating = (TextView)findViewById(R.id.movieRatingIDDets);
        writer = (TextView)findViewById(R.id.writersDet);
        plot = (TextView)findViewById(R.id.plotDet);
        boxOffice = (TextView)findViewById(R.id.boxOfficeDet);
        runTime = (TextView)findViewById(R.id.movieRuntimeIDDets);
        actors = (TextView)findViewById(R.id.actorsDet);
        awards = (TextView)findViewById(R.id.awardsDet);







    }
}