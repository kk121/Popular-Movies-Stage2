package com.krishna.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentActivityMain extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = FragmentActivityMain.class.getSimpleName();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sort = preferences.getString(PREF_KEY_SORT_BY, "popularity");
            new FetchMovieTask().execute(sort.equals("popularity") ? PATH_POPULAR : PATH_TOP_RATED);
        } else
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    private class FetchMovieTask extends AsyncTask<String, Void, String> {
        private static final String BASE_URL = "https://api.themoviedb.org/3";
        private static final String API_KEY_PARAM = "api_key";
        //your api key
        private static final String API_KEY_VALUE = "";
        private String pathToAppend;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            pathToAppend = params[0];
            BufferedReader bufferedReader = null;
            HttpURLConnection urlConnection = null;
            StringBuffer stringBuffer = null;
            try {
                Uri uri = Uri.parse(BASE_URL + pathToAppend).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                        .build();
                URL url = new URL(uri.toString());
                Log.d(TAG, uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    return null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                if (stringBuffer.length() == 0)
                    return null;
                return stringBuffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            movieList = parseJSONData(s);
            resetAdapter();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void resetAdapter() {
        adapterMovieGrid.clear();
        adapterMovieGrid.addAll(movieList);
        adapterMovieGrid.notifyDataSetChanged();
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
    }

}
