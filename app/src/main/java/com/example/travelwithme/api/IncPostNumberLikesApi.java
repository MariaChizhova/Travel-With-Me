package com.example.travelwithme.api;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IncPostNumberLikesApi {
    @POST("/inc_post_number_likes")
    Call<Void> incNumberLikes(
            @Query("postID") Long postID,
            @Query("userID") Long userID
    );
}
