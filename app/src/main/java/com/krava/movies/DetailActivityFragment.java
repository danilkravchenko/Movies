package com.krava.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView thumbnail = (ImageView) view.findViewById(R.id.movie_thumbnail_imageview);
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        TextView date = (TextView) view.findViewById(R.id.movie_date_textview);
        TextView overview = (TextView) view.findViewById(R.id.movie_overview_textview);
        TextView rate = (TextView) view.findViewById(R.id.movie_rate_textview);
        Intent intent = getActivity().getIntent();

        Uri path = Uri.parse(getString(R.string.base_thumbnail_url)).buildUpon()
                .appendEncodedPath(getString(R.string.thumbnail_size))
                .appendEncodedPath(intent.getStringExtra(getString(R.string.thumbnail_extra)))
                .build();
        Picasso.with(getContext()).load(path).into(thumbnail);

        title.setText(intent.getStringExtra(getString(R.string.title_extra)));
        date.setText(intent.getStringExtra(getString(R.string.date_extra)));
        overview.setText(intent.getStringExtra(getString(R.string.overview_extra)));
        rate.setText(intent.getStringExtra(getString(R.string.rating_extra)));

        return view;
    }
}
