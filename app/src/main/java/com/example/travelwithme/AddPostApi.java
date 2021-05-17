package com.example.travelwithme;

import com.example.travelwithme.requests.PostCreateRequest;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface AddPostApi {
    @POST("/add_post")
    Call<Void> addPost(@Body PostCreateRequest body);
}

