package com.example.travelwithme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetPostsApi {
    @GET("/get_posts/{authorId}")
    Call<List<GetPostResponse>> getPosts(@Path("authorId") Long authorId);


}

