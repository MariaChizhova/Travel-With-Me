package com.example.travelwithme.api;

import com.example.travelwithme.requests.PostCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetFollowingsPostsApi {
    @GET("/get_followings_posts")
    Call<List<PostCreateRequest>> getFollowingsPosts(
            @Query("userID") Long userID,
            @Query("offset") Long offset,
            @Query("count") Long count
    );
}
