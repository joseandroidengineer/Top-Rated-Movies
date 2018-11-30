package com.jge.topratedmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.jge.topratedmovies.database.AppDatabase;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView mDescriptionTextView;
    private TextView mReleaseDateTV;
    private ImageView mImageUrlImageView;
    private RatingBar mRatingBar;
    private Switch mFavoriteSwitch;
    private float rating;
    private String overview;
    private String imgPath;
    private String title;
    private String releaseDate;
    private boolean isFavorite;
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

}
