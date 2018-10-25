package com.jge.topratedmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private TextView mDescriptionTextView;
    private TextView mReleaseDateTV;
    private ImageView mImageUrlImageView;
    private RatingBar mRatingBar;
    private float rating;
    private String overview;
    private String imgPath;
    private String title;
    private String releaseDate;

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
        populateUI();
    }

    private void init(){
        mDescriptionTextView = findViewById(R.id.overview_tv);
        mImageUrlImageView = findViewById(R.id.poster_iv);
        mRatingBar = findViewById(R.id.ratingBar);
        mReleaseDateTV = findViewById(R.id.release_date_tv);
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
    }
}
