package com.krava.movies.api;

import com.krava.movies.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Крава on 10.07.2016.
 */
public interface Api {

    @GET("popular")
    Call<Movies> getPopular(@Query("api_key") String key, @Query("page") int pages);

    @GET("top_rated")
    Call<Movies> getTopRated(@Query("api_key") String key,  @Query("page") int pages);
}
