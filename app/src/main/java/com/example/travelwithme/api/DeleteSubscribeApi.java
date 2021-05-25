package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface DeleteSubscribeApi {
    @DELETE("/delete_subscribe")
    Call<Void> deleteSubscribe(
            @Query("followingID") Long followingID,
            @Query("followerID") Long followerID
    );
}
