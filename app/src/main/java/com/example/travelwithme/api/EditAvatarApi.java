package com.example.travelwithme.api;


import com.example.travelwithme.requests.AvatarEditRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface EditAvatarApi {
    @PUT("/edit_avatar")
    Call<Void> editAvatar(@Body AvatarEditRequest avatarEditRequest);
}
