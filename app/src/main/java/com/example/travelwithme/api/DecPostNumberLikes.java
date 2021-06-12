package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DecPostNumberLikes {
    @POST("/dec_post_number_likes")
    Call<Void> decNumberLikes(
            @Query("postID") Long postID,
            @Query("userID") Long userID
    );
}
