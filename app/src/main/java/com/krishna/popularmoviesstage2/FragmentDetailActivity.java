package com.krishna.popularmoviesstage2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetailActivity extends Fragment implements View.OnClickListener {
    public static final String TAG = FragmentDetailActivity.class.getSimpleName();
    public static final String MOVIE_KEY = "movie";
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w185";
    public static final String FAV_MOVIE = "FAV_MOVIE";
    private Movie movie;
    private AdapterReviews adapterReviews;
    private AdapterTrailers adapterTrailers;
    private ListView lvTrailer;
    private ListView lvReview;
    private ImageView favMovie;

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
        favMovie = (ImageView) rootView.findViewById(R.id.iv_fav);
        favMovie.setOnClickListener(this);
        lvTrailer = (ListView) rootView.findViewById(R.id.lv_trailer);
        lvReview = (ListView) rootView.findViewById(R.id.lv_review);
        lvReview.setEmptyView(rootView.findViewById(R.id.tv_empty));
        lvTrailer.setOnItemClickListener(trailerItemClickListener);

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

            if (checkIfAlreadySaved())
                favMovie.setImageResource(R.drawable.ic_favorite_clicked);
        }
        loadTrailers();
        loadReviews();
        return rootView;
    }

    private AdapterView.OnItemClickListener trailerItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String videoKey = adapterTrailers.getItem(position).key;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoKey)));
        }
    };

    private void saveAsFavouriteMovie() {
        Gson gson = new Gson();
        String movieAsJson = gson.toJson(movie);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String favMovieList = preferences.getString(FAV_MOVIE, "");
        preferences.edit().putString(FAV_MOVIE, favMovieList + movieAsJson + ",").commit();
    }

    private void loadTrailers() {
        ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Trailer> call = apiService.getTrailers(movie.getId(), FetchMovieTask.API_KEY_VALUE);
        call.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, Response<Trailer> response) {
                List<Trailer.Result> trailers = response.body().results;
                adapterTrailers = new AdapterTrailers(getContext(), trailers);
                lvTrailer.setAdapter(adapterTrailers);
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                Log.d(TAG, "Failed to load trailer");
            }
        });
    }

    private void loadReviews() {
        ApiInterface apiService = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Reviews> reviewsCall = apiService.getReviews(movie.getId(), FetchMovieTask.API_KEY_VALUE);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                List<Reviews.Result> results = response.body().results;
                adapterReviews = new AdapterReviews(getContext(), results);
                lvReview.setAdapter(adapterReviews);
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.d(TAG, "Failed to load trailer");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_fav) {
            if (checkIfAlreadySaved()) {
                Toast.makeText(getContext(), "Already marked", Toast.LENGTH_SHORT).show();
            } else {
                saveAsFavouriteMovie();
                favMovie.setImageResource(R.drawable.ic_favorite_clicked);
                Toast.makeText(getContext(), "Marked as Favourite", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkIfAlreadySaved() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String favMovieList = preferences.getString(FAV_MOVIE, "");
        if (favMovieList.contains("\"id\":\"" + movie.getId() + "\""))
            return true;
        return false;
    }
}
