package com.krishna.popularmoviesstage2;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Krishna on 27/03/16.
 */
class FetchMovieTask extends AsyncTask<String, Void, String> {
    private static final String API_KEY_PARAM = "api_key";
    //your api key
    public static final String API_KEY_VALUE = "";
    private String url;
    private AsyncTaskResult taskResult;

    public FetchMovieTask(AsyncTaskResult taskResult) {
        this.taskResult = taskResult;
    }

    @Override
    protected String doInBackground(String... params) {
        url = params[0];
        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection = null;
        StringBuffer stringBuffer = null;
        try {
            Uri uri = Uri.parse(url).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                    .build();
            URL url = new URL(uri.toString());
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
        taskResult.onTaskResult(s);
    }

    interface AsyncTaskResult {
        void onTaskResult(String s);
    }
}
