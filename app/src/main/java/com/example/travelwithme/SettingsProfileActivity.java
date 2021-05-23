package com.example.travelwithme;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelwithme.api.EditUserApi;
import com.example.travelwithme.requests.UserEditRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SettingsProfileActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText description;
    private EditText location;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        description = findViewById(R.id.description);
        location = findViewById(R.id.location);
        email = getIntent().getExtras().getString("email");

    }

    public void updateProfile(View view) {
        new Api().getUser(email, user -> {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://84.252.137.106:9090")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            EditUserApi editUserApi = retrofit.create(EditUserApi.class);
            UserEditRequest userEditRequest = new UserEditRequest();
            userEditRequest.setFirstName(firstName.getText().toString());
            userEditRequest.setLastName(lastName.getText().toString());
            userEditRequest.setUserId(user.getUserID());
            Call<Void> call = editUserApi.editUser(userEditRequest);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.i("sucsess", "sucsess edit user");
                    } else {
                        Log.i("eeeerrror", "error1");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                    Log.i("eeeerrror", "error2");
                }
            });
            finish();
        });
    }


}