package com.example.travelwithme.api;


import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface AddSubscribeApi {
    @PUT("/add_subscribe")
    Call<Void> addSubscribe(
            @Query("followingID") Long followingID,
            @Query("followerID") Long followerID
    );
}
