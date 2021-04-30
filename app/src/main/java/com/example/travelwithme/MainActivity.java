package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.travelwithme.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
                        selectedFragment = new ProfileFragment();
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
}