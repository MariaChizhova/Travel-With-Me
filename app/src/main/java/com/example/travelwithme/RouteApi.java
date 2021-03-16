package com.example.travelwithme;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RouteApi {
    @GET("/maps/api/directions/json")
    void getRoute(
            @Query(value = "origin", encodeValue = false) String position,
            @Query(value = "destination", encodeValue = false) String destination,
            @Query("sensor") boolean sensor,
            @Query("language") String language,
            @Query("key") String key,
            Callback<RouteResponse> cb
    );
}
