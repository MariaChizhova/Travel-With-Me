package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth auth;

    // UI references
    private EditText emailEditText;
    private Button resetPasswordButton;
    private Button backButton;

    // Toast messages
    private static final String emptyCaseMessage = "Please enter email id";
    private static final String successCaseMessage = "Reset link sent to your email";
    private static final String failedCaseMessage = "Unable to send reset mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // initialization of Firebase Authentication SDK
        auth = FirebaseAuth.getInstance();

        // initialization of UI references
        emailEditText = findViewById(R.id.email_edit_text);
        resetPasswordButton = findViewById(R.id.reset_pass_button);
        backButton = findViewById(R.id.back_button);

        // set function for the click
        resetPasswordButton.setOnClickListener(view -> resetPassword());
        backButton.setOnClickListener(view -> finish());
    }

    // try to send a reset link
    private void resetPassword() {
        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), emptyCaseMessage, Toast.LENGTH_LONG).show();
        } else {
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(ForgotPasswordActivity.this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), successCaseMessage, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), failedCaseMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}