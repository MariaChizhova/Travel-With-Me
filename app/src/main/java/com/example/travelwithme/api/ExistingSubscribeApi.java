package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExistingSubscribeApi {
    @GET("/existing_subscribe")
    Call<Boolean> existingSubscribe(
            @Query("followingID") Long followingID,
            @Query("followerID") Long followerID
    );
}
