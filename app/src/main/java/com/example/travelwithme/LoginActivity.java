package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth auth;

    // UI references
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private TextView resetPasswordTv;

    // Toast messages
    private static final String emptyCaseMessage = "Please fill all the fields";
    private static final String successCaseMessage = "Successfully Logged In";
    private static final String failedCaseMessage = "Login Failed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialization of Firebase Authentication SDK
        auth = FirebaseAuth.getInstance();

        // initialization of UI references
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pass_edit_text);
        signUpButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        resetPasswordTv = findViewById(R.id.reset_pass_tv);

        // set function for the click
        loginButton.setOnClickListener(view -> login());
        signUpButton.setOnClickListener(view -> toSignUpActivity());
        resetPasswordTv.setOnClickListener(view -> toForgotPasswordActivity());
    }

    // try to login
    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), emptyCaseMessage, Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
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

    // move to the SignUpActivity
    private void toSignUpActivity() {
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
        finish();
    }

    // move to the ForgotPasswordActivity
    private void toForgotPasswordActivity() {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
    }

}
