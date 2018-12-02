package com.jge.topratedmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jge.topratedmovies.database.AppDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{
    private TextView mDescriptionTextView;
    private TextView mReleaseDateTV;
    private ImageView mImageUrlImageView;
    private RatingBar mRatingBar;
    private Switch mFavoriteSwitch;
    private RecyclerView mRecyclerViewTrailers;
    private TrailerAdapter mTrailerAdapter;
    private JSONObject mJSONObjectResponse;
    private Gson gson;
    private RequestQueue queue;
    private float rating;
    private String overview;
    private String imgPath;
    private String title;
    private String releaseDate;
    private boolean isFavorite;
    private List<Trailer> trailers;
    private static String favoriteMoviePrefKey = "KEY";
    private int id;
    private AppDatabase mFavoriteMoviesDatabase;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        init();
        overview = getIntent().getExtras().getString("overview");
        imgPath = getIntent().getExtras().getString("imgUrl");
        title = getIntent().getExtras().getString("title");
        rating = getIntent().getExtras().getFloat("rating");
        releaseDate = getIntent().getExtras().getString("release");
        id = getIntent().getExtras().getInt("id");
        volleyRequest(id);
        populateUI();
        onClicks();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init(){
        mFavoriteMoviesDatabase = AppDatabase.getInstance(getApplicationContext());
        mDescriptionTextView = findViewById(R.id.overview_tv);
        mImageUrlImageView = findViewById(R.id.poster_iv);
        mRatingBar = findViewById(R.id.ratingBar);
        mReleaseDateTV = findViewById(R.id.release_date_tv);
        mFavoriteSwitch = findViewById(R.id.switch_favorites);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mTrailerAdapter = new TrailerAdapter(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewTrailers = findViewById(R.id.recyclerview_list_of_trailers);
        mRecyclerViewTrailers.setLayoutManager(llm);
        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewTrailers.setAdapter(mTrailerAdapter);
    }

    private void onClicks(){
        final Movie movie = new Movie();
        movie.setId(id);
        movie.setVoteAverage(rating);
        movie.setOriginalTitle(title);
        movie.setPosterImagePath(imgPath);
        movie.setReleaseDate(releaseDate);
        movie.setDescription(overview);
        movie.setTitle(title);
        mFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(isChecked){
                            sharedPreferences.edit().putBoolean(title+favoriteMoviePrefKey,isChecked).apply();
                            mFavoriteMoviesDatabase.movieDao().insertMovie(movie);
                            Log.e("DBINSERT",title+"MOVIE INSERTED");
                        }else{
                            sharedPreferences.edit().putBoolean(title+favoriteMoviePrefKey,false).apply();
                            mFavoriteMoviesDatabase.movieDao().deleteMovie(movie);
                            Log.e("DBDELETE", title+"MOVIE DELETED");
                        }
                    }
                });
            }
        });
    }

    private void populateUI(){
        setTitle(title);
        mDescriptionTextView.setText(overview);
        mRatingBar.setRating(rating/2);
        mReleaseDateTV.setText(releaseDate);
        Picasso.with(this).load(NetworkUtils.buildImageUrl(imgPath))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mImageUrlImageView);
        if(!sharedPreferences.getBoolean(title+favoriteMoviePrefKey,false)){
            return;
        }else{
            mFavoriteSwitch.setChecked(sharedPreferences.getBoolean(title+favoriteMoviePrefKey, false));
        }

    }
    private void gsonMap(JSONArray jsonArray) {
        if(mJSONObjectResponse == null){
            Log.e("ERROR","gsonMAP ERROR");
        }else{
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
            if (jsonArray.length() > 0) {
                trailers = Arrays.asList(gson.fromJson(jsonArray.toString(), Trailer[].class));
                mTrailerAdapter.setTrailerData(trailers);

            }
        }
    }
    private void volleyRequest(int id){
        queue = Volley.newRequestQueue(getBaseContext());
        String url = String.valueOf(NetworkUtils.buildBaseVideoUrl(getString(R.string.movie_api_key), id));
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
                        Log.e("VOLLEY", volleyError.toString());
                    }
                });
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onListItemClick(Trailer trailerNameIndexClicked) {
        Intent videoIntent = new Intent(Intent.ACTION_VIEW, NetworkUtils.buildVideoUrl(trailerNameIndexClicked.getKey()));
        this.startActivity(videoIntent);
    }
}
