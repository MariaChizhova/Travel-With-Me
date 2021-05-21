package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.travelwithme.api.AddPostApi;
import com.example.travelwithme.api.AddUserApi;
import com.example.travelwithme.requests.PostCreateRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth mAuth;

    // UI references
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of Firebase Authentication SDK
        mAuth = FirebaseAuth.getInstance();

        // check if user is signed in(not-null)
        if (mAuth.getCurrentUser() == null) {
            toLoginActivity();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = mAuth.getCurrentUser().getEmail();
        preferences.edit().putString("user_email", email).apply();

        String tmp = preferences.getString("user_email", "");
        System.err.println(tmp);
        addUser(mAuth.getCurrentUser().getEmail());

        // set SearchFragment for opening application
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchFragment()).commit();

        setContentView(R.layout.activity_main);

        // initialization of UI references
        bottomNavigation = findViewById(R.id.navigation_view);

        // set function for the click
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
    }

    // Logic of bottom navigation
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.navigation_feed:
                        selectedFragment = new FeedFragment();
                        break;
                    case R.id.navigation_messages:
                        selectedFragment = new MessagesFragment();
                        break;
                    case R.id.navigation_profile:
                        selectedFragment = new MainProfileFragment(mAuth.getCurrentUser().getEmail());
                        break;
                    case R.id.navigation_settings:
                        selectedFragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                return true;
            };

    // move to the LoginActivity with sing out
    private void toLoginActivity() {
        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void addUser(@NotNull String email){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AddUserApi addUserApi = retrofit.create(AddUserApi.class);
        Call<Void> call = addUserApi.addUser(email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("sucsess", "sucsess");
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
    }
}