package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.email_edit_text);

        final Button resetPasswordButton = findViewById(R.id.reset_pass_button);
        final Button backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPasswordActivity.this, "Please enter email id", Toast.LENGTH_LONG).show();
            } else {

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(ForgotPasswordActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

    }
}