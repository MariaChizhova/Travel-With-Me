package com.example.travelwithme.api;

import com.example.travelwithme.pojo.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUserApi {
    @GET("/get_user")
    Call<User> getUser(@Query("email") String email);
}
