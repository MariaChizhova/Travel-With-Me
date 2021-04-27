package com.example.travelwithme;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface StaticApi {
    @GET("/maps/api/staticmap")
    void getRoute(
            @Query("size") String size,
            @Query("markers") String markers,
            @Query("path") String path,
            @Query("key") String key,
            Callback<Response> cb
    );

}
