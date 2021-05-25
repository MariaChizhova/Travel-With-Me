package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeletePostApi {
    @DELETE("/delete_post/{postID}")
    Call<Void> deletePost(@Path("postID") Long postID);
}
