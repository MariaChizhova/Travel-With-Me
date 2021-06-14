package com.example.travelwithme.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.travelwithme.LoginActivity;
import com.example.travelwithme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class SettingsFragment extends Fragment {

    // Firebase Authentication SDK
    private FirebaseAuth mAuth;

    // UI references
    private Button logoutButton;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // initialization of Firebase Authentication SDK
        mAuth = FirebaseAuth.getInstance();

        // initialization of UI references
        logoutButton = getView().findViewById(R.id.logout_button);

        // set function for the click
        logoutButton.setOnClickListener(v -> toLoginActivity());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    // move to the LoginActivity with sing out
    private void toLoginActivity() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.edit().remove("user").apply();
        preferences.edit().remove("user_email").apply();
        mAuth.signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}