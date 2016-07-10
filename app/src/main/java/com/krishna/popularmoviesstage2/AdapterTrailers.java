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
public class AdapterTrailers extends BaseAdapter {
    private final Context context;
    List<Trailer.Result> trailerList = new ArrayList<>();

    public AdapterTrailers(Context context, List<Trailer.Result> trailerList) {
        this.context = context;
        this.trailerList = trailerList;
    }

    @Override
    public int getCount() {
        return trailerList.size();
    }

    @Override
    public Trailer.Result getItem(int position) {
        return trailerList.get(position);
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
            view = layoutInflater.inflate(R.layout.item_trailor, null, false);
        TextView tvName = (TextView) view.findViewById(R.id.tv_trailor_name);
        tvName.setText("Trailer " + (position + 1));
        return view;
    }
}
