package com.example.travelwithme;

import com.example.travelwithme.requests.PostCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetPostsApi {
    @GET("/get_posts/{authorId}")
    Call<List<PostCreateRequest>> getPosts(@Path("authorId") Long authorId);


}

