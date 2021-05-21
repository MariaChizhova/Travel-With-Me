package com.example.travelwithme.api;


import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetFollowingsApi {
    @GET("/get_followings/{userId}")
    List<Long> getFollowings(@Path("userId") Long userId);
}
