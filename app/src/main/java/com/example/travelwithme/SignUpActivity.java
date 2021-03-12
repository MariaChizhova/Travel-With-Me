package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth auth;

    // UI references
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private Button loginButton;

    // Toast messages
    private static final String emptyCaseMessage = "Please fill all the fields";
    private static final String successCaseMessage = "Successfully Registered";
    private static final String failedCaseMessage = "Registration Failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        // initialization of Firebase Authentication SDK
        auth = FirebaseAuth.getInstance();

        // initialization of UI references
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pass_edit_text);
        signUpButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);

        // set function for the click
        signUpButton.setOnClickListener(view -> signUp());
        loginButton.setOnClickListener(view -> toLoginActivity());
    }

    // try to sing up
    private void signUp() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), emptyCaseMessage, Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), successCaseMessage, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), failedCaseMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    // move to the LoginActivity
    private void toLoginActivity() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
