package com.krava.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.krava.movies.api.ApiFactory;
import com.krava.movies.model.Movies;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private int page = 1;
    private MovieAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_videos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                page = 1;
                updateMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.movie_rec_view);
        
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(new ArrayList<Movies.Movie>());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = layoutManager.getItemCount();
                int last = layoutManager.findLastCompletelyVisibleItemPosition();
                if (last >= totalItemCount - 4) {
                    page += 1;
                    updateMovies();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortOrder = preferences.getString(getString(R.string.pref_order_key), getString(R.string.pref_order_popular));
        Call<Movies> call;
        if (sortOrder.equals(getString(R.string.pref_order_popular))) {
            call = ApiFactory.getService().getPopular(getString(R.string.api_key), page);
        } else if (!sortOrder.equals(getString(R.string.pref_order_top_rated))) {
            return;
        } else {
            call = ApiFactory.getService().getTopRated(getString(R.string.api_key), page);
        }


        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    adapter.add(response.body().getMovies());
                } else {
                    Toast.makeText(getContext(), "Could not load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
        private ArrayList<Movies.Movie> data;


        public MovieAdapter(ArrayList<Movies.Movie> data) {
            this.data = data;
        }

        public void clear() {
            data.clear();
            notifyDataSetChanged();
        }

        public void add(List<Movies.Movie> data) {
            int startPosition = this.data.size();
            this.data.addAll(data);
            notifyItemRangeChanged(startPosition, this.data.size());
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            Uri path = Uri.parse(getString(R.string.base_thumbnail_url)).buildUpon()
                    .appendEncodedPath(getString(R.string.thumbnail_size))
                    .appendEncodedPath(data.get(position).getThumbnailUrl())
                    .build();
            Picasso.with(getContext()).load(path).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        protected class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imageView;

            public MovieHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.movie_imageview);
                imageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                startActivity(prepareIntent());
            }

            private Intent prepareIntent() {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Movies.Movie movie = data.get(this.getAdapterPosition());
                intent.putExtra(getString(R.string.title_extra), movie.getTitle());
                intent.putExtra(getString(R.string.overview_extra), movie.getOverview());
                DateFormat format = new SimpleDateFormat("yyyy", Locale.US);
                intent.putExtra(getString(R.string.date_extra), format.format(movie.getDate()));
                intent.putExtra(getString(R.string.rating_extra), movie.getRating());
                intent.putExtra(getString(R.string.thumbnail_extra), movie.getThumbnailUrl());
                return intent;
            }


        }
    }

}
