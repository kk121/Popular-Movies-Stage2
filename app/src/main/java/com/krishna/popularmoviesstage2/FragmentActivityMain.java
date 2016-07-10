package com.krishna.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentActivityMain extends Fragment implements AdapterView.OnItemClickListener, FetchMovieTask.AsyncTaskResult {

    private static final String TAG = FragmentActivityMain.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String PREF_KEY_SORT_BY = "pref_sort_by";
    private static final String PATH_POPULAR = "/movie/popular";
    private static final String PATH_TOP_RATED = "/movie/top_rated";
    private static final int SETTING_ACTIVITY = 1;
    private GridView gridView;
    private ArrayList<Movie> movieList;
    private AdapterMovieGrid adapterMovieGrid;
    private MovieClickedListener movieClickedListener;
    private ProgressBar progressBar;

    public FragmentActivityMain() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_veiw);
        gridView.setOnItemClickListener(this);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        if (savedInstanceState == null)
            movieList = new ArrayList<>();
        else
            movieList = savedInstanceState.getParcelableArrayList(FragmentDetailActivity.MOVIE_KEY);
        adapterMovieGrid = new AdapterMovieGrid(getActivity(), movieList);
        gridView.setAdapter(adapterMovieGrid);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FragmentDetailActivity.MOVIE_KEY, movieList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getActivity(), ActivityPreference.class), SETTING_ACTIVITY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SETTING_ACTIVITY:
                updateMovieList();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (movieList != null && movieList.size() == 0)
            updateMovieList();
    }

    public void updateMovieList() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = preferences.getString(PREF_KEY_SORT_BY, "popularity");
        if (sort.equals("favourite")) {
            loadAllFavouriteMovies();
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String url = BASE_URL + (sort.equals("popularity") ? PATH_POPULAR : PATH_TOP_RATED);
            progressBar.setVisibility(View.VISIBLE);
            new FetchMovieTask(this).execute(url);
        } else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    private void loadAllFavouriteMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String favMovieList = preferences.getString(FragmentDetailActivity.FAV_MOVIE, "");
        if (favMovieList.equals("")) return;
        String movieJsonList[] = favMovieList.split("\\},");
        movieList = new ArrayList<>();
        for (String movieJson : movieJsonList) {
            Log.d(TAG, movieJson + "}");
            Movie movie = new Gson().fromJson(movieJson + "}", Movie.class);
            movieList.add(movie);
        }
        resetAdapter();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            movieClickedListener = (MovieClickedListener) context;
        } catch (Exception e) {
            throw new ClassCastException(e.getMessage()
                    + " implement interface MovieClickListener in host activity.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (movieClickedListener != null)
            movieClickedListener.onMovieClicked(adapterMovieGrid.getItem(position));
    }

    @Override
    public void onTaskResult(String s) {
        movieList = parseJSONData(s);
        resetAdapter();
        progressBar.setVisibility(View.GONE);
    }

    private void resetAdapter() {
        adapterMovieGrid.clear();
        adapterMovieGrid.addAll(movieList);
        gridView.setAdapter(adapterMovieGrid);
        adapterMovieGrid.notifyDataSetChanged();
        movieClickedListener.updateDetailFragment(adapterMovieGrid.getItem(0));
    }

    private ArrayList<Movie> parseJSONData(String s) {
        if (s == null)
            return null;
        ArrayList<Movie> moviesList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray movieArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieObject = movieArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(movieObject.getString("id"));
                movie.setTitle(movieObject.getString("title"));
                movie.setOrigTitle(movieObject.getString("original_title"));
                movie.setOverView(movieObject.getString("overview"));
                movie.setPopularity(movieObject.getString("popularity"));
                movie.setPosterPath(movieObject.getString("poster_path"));
                movie.setVoteAvg(movieObject.getString("vote_average"));
                movie.setReleaseDate(movieObject.getString("release_date"));
                moviesList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    public interface MovieClickedListener {
        void onMovieClicked(Movie movie);

        void updateDetailFragment(Movie movie);
    }
}
