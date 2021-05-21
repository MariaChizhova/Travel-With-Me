package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        mAuth.signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }
}