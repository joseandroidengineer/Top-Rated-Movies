package com.jge.topratedmovies;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NetworkUtils {


    private static final String VIDEO_TAG = "Video Url";
    private static final String API_KEY = "api_key";
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String NUM_PARAM = "3/";
    private static final String MEDIA_TYPE_PATH = "movie/";
    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String SIZE_IMAGE_PATH ="w500";
    private static final String VIDEO_KEY = "v";
    private static final String VIDEO_BASE_URL = "https://www.youtube.com/";
    private static final String VIDEO_PATH = "watch";
    private static final String VIDEO_VAL = "videos";


    public static URL buildBaseUrl(String key, String encodedPath) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(NUM_PARAM)
                .appendEncodedPath(MEDIA_TYPE_PATH)
                .appendEncodedPath(encodedPath)
                .appendQueryParameter(API_KEY,key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e("URL", builtUri.toString());

        return url;
    }

    public static Uri buildImageUrl(String backdropPath){
        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendEncodedPath(SIZE_IMAGE_PATH)
                .appendEncodedPath(backdropPath)
                .build();

        Log.v(TAG, "Built IMAGE URI " + builtUri);

        return builtUri;
    }

    public static Uri buildVideoUrl(String videoID){
        Uri builtUri = Uri.parse(VIDEO_BASE_URL).buildUpon()
                .appendEncodedPath(VIDEO_PATH)
                .appendQueryParameter(VIDEO_KEY,videoID)
                .build();
        Log.v(VIDEO_TAG, "Built Video URI"+ builtUri);
        return builtUri;

    }

    public static URL buildBaseVideoUrl(String key, int movieID) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(NUM_PARAM)
                .appendEncodedPath(MEDIA_TYPE_PATH)
                .appendEncodedPath(movieID +"/")
                .appendEncodedPath(VIDEO_VAL)
                .appendQueryParameter(API_KEY,key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e("URL", builtUri.toString());

        return url;
    }

}
