package com.krishna.popularmoviesstage1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetailActivity extends Fragment {


    public static final String MOVIE_KEY = "movie";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w185";
    private Movie movie;

    public FragmentDetailActivity() {
        // Required empty public constructor
    }

    public static Fragment getInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_KEY, movie);
        Fragment fragment = new FragmentDetailActivity();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_KEY, movie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);
        TextView tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        TextView tvOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        TextView tvRating = (TextView) rootView.findViewById(R.id.tv_rating);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        TextView tvPopularity = (TextView) rootView.findViewById(R.id.tv_popularity);
        TextView tvVoteCount = (TextView) rootView.findViewById(R.id.tv_vote_count);

        if (savedInstanceState == null) {
            movie = getArguments().getParcelable(MOVIE_KEY);
        } else {
            movie = savedInstanceState.getParcelable(MOVIE_KEY);
        }
        if (movie != null) {
            Picasso.with(getActivity()).load(BASE_URL + movie.getPosterPath()).into(ivPoster);
            tvTitle.setText(movie.getOrigTitle());
            tvOverview.setText(movie.getOverView());
            tvRating.setText(movie.getVoteAvg());
            tvReleaseDate.setText(movie.getReleaseDate());
            tvPopularity.setText(movie.getPopularity());
            tvVoteCount.setText(movie.getVoteAvg());
        }
        return rootView;
    }

}
