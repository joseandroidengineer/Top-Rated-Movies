package com.jge.topratedmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jge.topratedmovies.database.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.e(TAG, "Retrieving Favorite Movies from Database");
        movies= database.movieDao().loadAllMovies();

    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
