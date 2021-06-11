package com.example.travelwithme.api;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddChatApi {
    @POST("/add_chat")
    Call<Void> addChat(
            @Query("firstID") Long firstID,
            @Query("secondID") Long secondID
    );
}
