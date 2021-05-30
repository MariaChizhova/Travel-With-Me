package com.example.travelwithme.api;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IncPostNumberLikesApi {
    @POST("/inc_post_number_likes/{postID}")
    Call<Void> incNumberLikes(@Path("postID") Long postID);
}
