package com.example.travelwithme.api;


import com.example.travelwithme.requests.UserEditRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface EditUserApi {
    @PUT("/edit_user")
    Call<Void> editUser(@Body UserEditRequest editedUser);
}
