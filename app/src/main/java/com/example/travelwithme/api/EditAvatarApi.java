package com.example.travelwithme.api;


import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface EditAvatarApi {
    @PUT("/edit_avatar")
    Call<Void> editAvatar(
            @Query("userID") Long userID,
            @Query("newAvatar") String newAvatar
    );
}
