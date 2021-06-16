package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface DeleteChatApi {
    @DELETE("/delete_chat")
    Call<Void> deleteChat(
            @Query("firstID") Long firstID,
            @Query("secondID") Long secondID
    );
}
