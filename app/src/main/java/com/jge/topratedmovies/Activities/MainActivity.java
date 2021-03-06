package com.jge.topratedmovies.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jge.topratedmovies.Activities.DetailsActivity;
import com.jge.topratedmovies.Adapters.MovieAdapter;
import com.jge.topratedmovies.MainViewModel;
import com.jge.topratedmovies.Models.Movie;
import com.jge.topratedmovies.NetworkUtils;
import com.jge.topratedmovies.R;
import com.jge.topratedmovies.database.AppDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private JSONObject mJSONObjectResponse;
    private String error;
    private Gson gson;
    private ArrayList<Movie> moviesArrayList;
    private RequestQueue queue;
    private static final String POPULARITY_PATH = "popular";
    private static final String RATING_PATH = "top_rated";
    private AppDatabase mFavoritesDatabase;

    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_list_of_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        if(savedInstanceState != null){
            moviesArrayList = savedInstanceState.getParcelableArrayList("moviesArrayList");
            mMovieAdapter.setMovieData(moviesArrayList);
            loadMovieData();
            mMovieAdapter.notifyDataSetChanged();
        } else{
            mLoadingIndicator.setVisibility(View.VISIBLE);
            volleyRequest(POPULARITY_PATH);
            loadMovieData();
        }

        mMovieAdapter.notifyDataSetChanged();
        mFavoritesDatabase = AppDatabase.getInstance(this);
    }

    private void loadMovieData() {
        showMovieDataView();
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void gsonMap(JSONArray jsonArray) {
        if(mJSONObjectResponse == null){
            showErrorMessage();
        }else{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            if (jsonArray.length() > 0) {
                moviesArrayList = new ArrayList<>(Arrays.asList(gson.fromJson(jsonArray.toString(), Movie[].class)));
                mMovieAdapter.setMovieData(moviesArrayList);

            }
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void volleyRequest(String path){
        queue = Volley.newRequestQueue(getBaseContext());
        String url = String.valueOf(NetworkUtils.buildBaseUrl(getString(R.string.movie_api_key), path));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mJSONObjectResponse = response;
                        try {
                            gsonMap(response.getJSONArray("results"));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        error = volleyError.toString();
                        Log.e("VOLLEY", volleyError.toString());
                        showErrorMessage();
                    }
                });
        queue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();
        if(id == R.id.sort_popular){
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mMovieAdapter.setMovieData(null);
            volleyRequest(POPULARITY_PATH);
        }else if(id == R.id.sort_topRated){
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mMovieAdapter.setMovieData(null);
            volleyRequest(RATING_PATH);

        } else if(id == R.id.sort_favorites){
            setUpViewModel();
            Toast.makeText(this,"Favorites was tapped", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(Movie movieNameIndexClicked) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("title",movieNameIndexClicked.getTitle());
        intent.putExtra("overview", movieNameIndexClicked.getDescription());
        intent.putExtra("imgUrl", movieNameIndexClicked.getPosterImagePath());
        intent.putExtra("rating",movieNameIndexClicked.getVoteAverage());
        intent.putExtra("release",movieNameIndexClicked.getReleaseDate());
        intent.putExtra("id",movieNameIndexClicked.getId());
        startActivity(intent);
    }
    private void setUpViewModel(){
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.e("Observer TAG","Updating list of favorite moviesArrayList from LiveData in ViewModel");
                moviesArrayList = new ArrayList<>(movies);
                mMovieAdapter.setMovieData(moviesArrayList);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("moviesArrayList", moviesArrayList);
    }
}
