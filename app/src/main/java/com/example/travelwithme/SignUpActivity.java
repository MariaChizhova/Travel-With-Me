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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        final Intent intentMainActivity = new Intent(this, MainActivity.class);

        final Toast toastEmptyCase = Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG);
        final Toast toastSuccessCase = Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG);
        final Toast toastFailedCase = Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.email_edit_text);
        final EditText passwordEditText = findViewById(R.id.pass_edit_text);

        final Button signUpButton = findViewById(R.id.signup_button);
        final Button loginButton = findViewById(R.id.login_button);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                toastEmptyCase.show();
            } else {

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                toastSuccessCase.show();
                                startActivity(intentMainActivity);
                                finish();
                            } else {
                                toastFailedCase.show();
                            }
                        });
            }
        });

        loginButton.setOnClickListener(v -> {
            final Intent intentLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(intentLoginActivity);
            finish();
        });

    }

}
