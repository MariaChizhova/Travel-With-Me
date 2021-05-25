package com.example.travelwithme.api;

import com.example.travelwithme.pojo.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetUserByIDApi {
    @GET("/get_user/{userID}")
    Call<User> getUser(@Path("userID") Long userID);
}
