package com.example.travelwithme;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetPostApi {
    @GET("/get_post/{postId}")
    Call<GetPostResponse> getPost(@Path("postId") Long postId);

}


