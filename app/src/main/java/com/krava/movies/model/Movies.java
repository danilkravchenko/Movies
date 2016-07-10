package com.krava.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Крава on 10.07.2016.
 */
public class Movies {

    @SerializedName("results")
    private List<Movie> movies;


    public List<Movie> getMovies() {
        return movies;
    }

    public class Movie {
        @SerializedName("poster_path")
        private String thumbnailUrl;

        @SerializedName("vote_average")
        private String rating;

        @SerializedName("release_date")
        private Date date;

        private String overview;

        private String title;

        public String getRating() {
            return rating;
        }

        public Date getDate() {
            return date;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getOverview() {
            return overview;
        }

        public String getTitle() {
            return title;
        }
    }
}
