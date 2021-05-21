package com.example.travelwithme;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.travelwithme.api.GetUserApi;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public void getUser(String email, Consumer<User> onUserLoaded) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GetUserApi getUserApi = retrofit.create(GetUserApi.class);
        Call<User> call = getUserApi.getUser(email);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess");
                    User user = response.body();
                    onUserLoaded.accept(user);
                } else {
                    Log.i("eeeerrror", "error1");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("eeeerrror", "error2");
                t.printStackTrace();
            }
        });
    }
}
