package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth auth;

    // UI references
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialization of Firebase Authentication SDK
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            toLoginActivity();
        }

        setContentView(R.layout.activity_main);

        // initialization of UI references
        logoutButton = findViewById(R.id.logout_button);

        // set function for the click
        logoutButton.setOnClickListener(view -> toLoginActivity());
    }

    // move to the LoginActivity with sing out
    private void toLoginActivity() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}