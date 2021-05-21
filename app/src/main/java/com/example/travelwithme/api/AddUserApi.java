package com.example.travelwithme.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AddUserApi {
    @POST("/add_user")
    Call<Void> addUser(@Query("email") String email);
}
