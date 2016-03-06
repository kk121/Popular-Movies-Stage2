package com.krishna.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ActivityMain extends AppCompatActivity implements FragmentActivityMain.MovieClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(this, ActivityDetail.class);
        intent.putExtra(FragmentDetailActivity.MOVIE_KEY, movie);
        startActivity(intent);
    }
}
