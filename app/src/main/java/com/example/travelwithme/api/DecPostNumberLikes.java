package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DecPostNumberLikes {
    @POST("/delete_post/{postID}")
    Call<Void> decNumberLikes(@Path("postID") Long postID);
}
