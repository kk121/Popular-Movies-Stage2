package com.krishna.popularmoviesstage2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Krishna on 10/07/16.
 */
public interface ApiInterface {
    @GET("movie/{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<Trailer> getTrailers(@Path("id") String id, @Query("api_key") String apiKey);

}
