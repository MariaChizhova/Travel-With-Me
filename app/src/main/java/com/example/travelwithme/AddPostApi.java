package com.example.travelwithme;

import com.example.travelwithme.pojo.Post;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface AddPostApi {
    @POST("/add_post")
    Call<BaseResponse> addPost(@Body Post body);
}

