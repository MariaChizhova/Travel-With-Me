package com.example.travelwithme.api;

import com.example.travelwithme.requests.PostCreateRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetPostApi {
    @GET("/get_post/{postId}")
    Call<PostCreateRequest> getPost(@Path("postId") Long postId);

}


