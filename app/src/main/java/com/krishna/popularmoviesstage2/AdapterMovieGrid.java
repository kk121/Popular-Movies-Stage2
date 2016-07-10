package com.krishna.popularmoviesstage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Krishna on 06/03/16.
 */
public class AdapterMovieGrid extends ArrayAdapter<Movie> {
    private Context mContext;
    private List<Movie> movieList;
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w185";

    private static class ViewHolderMovie {
        TextView tvTitle;
        ImageView ivThumbnail;

        public ViewHolderMovie(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }

    public AdapterMovieGrid(Context context, List<Movie> list) {
        super(context, 0, list);
        mContext = context;
        movieList = list;
    }

    @Override
    public int getCount() {
        if (movieList != null)
            return movieList.size();
        return -1;
    }

    @Override
    public Movie getItem(int position) {
        if (movieList != null)
            return movieList.get(position);
        return super.getItem(position);
    }

    @Override
    public int getPosition(Movie item) {
        if (movieList != null)
            return movieList.indexOf(item);
        return super.getPosition(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view, parent, false);
            ViewHolderMovie holderMovie = new ViewHolderMovie(convertView);
            convertView.setTag(holderMovie);
        }
        bindView(convertView, position);

        return convertView;
    }

    private void bindView(View convertView, int position) {
        ViewHolderMovie holderMovie = (ViewHolderMovie) convertView.getTag();
        holderMovie.tvTitle.setText(movieList.get(position).getTitle());
        Picasso.with(mContext).load(BASE_URL + movieList.get(position).getPosterPath()).into(holderMovie.ivThumbnail);
    }
}
