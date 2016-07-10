package com.krishna.popularmoviesstage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        if (findViewById(R.id.fragment_container) != null) {
            Fragment fragment = FragmentDetailActivity.getInstance(movie);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, FragmentDetailActivity.TAG).commit();
        } else {
            Intent intent = new Intent(this, ActivityDetail.class);
            intent.putExtra(FragmentDetailActivity.MOVIE_KEY, movie);
            startActivity(intent);
        }
    }

    @Override
    public void updateDetailFragment(Movie movie) {
        if (findViewById(R.id.fragment_container) != null) {
            Fragment fragment = FragmentDetailActivity.getInstance(movie);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, FragmentDetailActivity.TAG).commitAllowingStateLoss();
        }
    }
}
