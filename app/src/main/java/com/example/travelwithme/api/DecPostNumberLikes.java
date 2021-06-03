package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DecPostNumberLikes {
    @POST("/delete_post")
    Call<Void> decNumberLikes(
            @Query("postID") Long postID,
            @Query("userID") Long userID
    );
}
