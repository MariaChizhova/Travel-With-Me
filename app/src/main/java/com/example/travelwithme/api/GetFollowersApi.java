package com.example.travelwithme.api;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetFollowersApi {
    @GET("/get_followers/{userId}")
    List<Long> getFollowers(@Path("userId") Long userId);
}
