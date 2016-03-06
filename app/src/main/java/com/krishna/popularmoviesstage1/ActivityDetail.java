package com.krishna.popularmoviesstage1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class ActivityDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Movie movie = getIntent().getParcelableExtra(FragmentDetailActivity.MOVIE_KEY);
        Fragment fragment = FragmentDetailActivity.getInstance(movie);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

}
