package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LikeExistsApi {
    @GET("/like_exists")
    Call<Boolean> likeExists(
            @Query("postID") Long postID,
            @Query("userID") Long userID
    );
}
