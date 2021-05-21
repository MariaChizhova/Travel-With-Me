package com.example.travelwithme.api;

import com.example.travelwithme.requests.PostCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPostsApi {
    @GET("/get_posts")
    Call<List<PostCreateRequest>> getPosts(
            @Query("authorID") Long authorId,
            @Query("offset") Long offset,
            @Query("count") Long count
    );
}

