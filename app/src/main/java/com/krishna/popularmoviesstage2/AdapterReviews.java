package com.krishna.popularmoviesstage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 09/07/16.
 */
public class AdapterReviews extends BaseAdapter {
    private final Context context;
    List<Reviews.Result> reviewList = new ArrayList<>();

    public AdapterReviews(Context context, List<Reviews.Result> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Reviews.Result getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = layoutInflater.inflate(R.layout.item_review, null, false);
        TextView tvAuther = (TextView) view.findViewById(R.id.tv_author);
        tvAuther.setText(reviewList.get(position).author);

        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.setText(reviewList.get(position).content);

        return view;
    }
}
