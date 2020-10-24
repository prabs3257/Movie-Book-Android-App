package com.example.movie_book.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movie_book.Data.MovieRecyclerViewAdapter;
import com.example.movie_book.Model.Movie;
import com.example.movie_book.R;
import com.example.movie_book.Util.Constants;
import com.example.movie_book.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private Button searchButton;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton = (Button)findViewById(R.id.searchButton);

        movieList = new ArrayList<>();

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        //getMovies(search);

        movieList = getMovies(search);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show();
                showInputDialog();
            }
        });

    }


    public void showInputDialog(){

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view,null);

        final EditText newSearchEdit = (EditText)view.findViewById(R.id.newSearchEdit);
        Button newSearchButton = (Button) view.findViewById(R.id.newSearchButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        newSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!newSearchEdit.getText().toString().isEmpty()){

                    Prefs prefs = new Prefs(MainActivity.this);
                    prefs.setSearch(newSearchEdit.getText().toString());

                    getMovies(newSearchEdit.getText().toString());


                }
                dialog.dismiss();

            }
        });
    }

    public List<Movie> getMovies(String searchTerm) {

        movieList.clear();

       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT,null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {

               try {
                   JSONArray moviesArray = response.getJSONArray("Search");


                   for(int i = 0; i < moviesArray.length(); i++){

                       JSONObject movieObj = moviesArray.getJSONObject(i);

                       Movie movie = new Movie();
                       movie.setTitle(movieObj.getString("Title"));
                       movie.setYear(movieObj.getString("Year"));
                       movie.setMovieType(movieObj.getString("Type"));
                       movie.setPoster(movieObj.getString("Poster"));
                       movie.setImdbId(movieObj.getString("imdbID"));

                       movieList.add(movie);

                       Log.d("Title", movie.getTitle());
                   }

                   movieRecyclerViewAdapter.notifyDataSetChanged();

               }catch (JSONException e){

                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

           }
       });

       queue.add(jsonObjectRequest);

       return movieList;

    }



}