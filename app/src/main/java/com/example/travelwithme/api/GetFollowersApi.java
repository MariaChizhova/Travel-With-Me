package com.example.travelwithme.api;

import com.example.travelwithme.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetFollowersApi {
    @GET("/get_followers")
    Call<List<User>> getFollowers(
            @Query("userID") Long userId,
            @Query("offset") Long offset,
            @Query("count") Long count
    );
}
