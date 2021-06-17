package com.example.travelwithme.api;

import com.example.travelwithme.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchChatsApi {
    @GET("/search_chats")
    Call<List<User>> searchChats(
            @Query("myID") Long myID,
            @Query("message") String message,
            @Query("offset") Long offset,
            @Query("count") Long count
    );
}
