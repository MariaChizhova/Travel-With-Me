package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication SDK
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    // UI references
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private Button signInWithGoogleButton;
    private TextView resetPasswordTv;

    // Toast messages
    private static final String emptyCaseMessage = "Please fill all the fields";
    private static final String successCaseMessage = "Successfully Logged In";
    private static final String failedCaseMessage = "Login Failed";

    // request code for sign in with google
    private static final int RC_SIGN_IN_WITH_GOOGLE = 314;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // initialization of Firebase Authentication SDK
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        // initialization of UI references
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.pass_edit_text);
        signUpButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        signInWithGoogleButton = findViewById(R.id.google_sign_in_button);
        resetPasswordTv = findViewById(R.id.reset_pass_tv);

        // set function for the click
        loginButton.setOnClickListener(view -> login());
        signUpButton.setOnClickListener(view -> toSignUpActivity());
        signInWithGoogleButton.setOnClickListener(view -> signInWithGoogle());
        resetPasswordTv.setOnClickListener(view -> toForgotPasswordActivity());
    }

    // try to login
    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), emptyCaseMessage, Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
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

    // sign in with google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_WITH_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_WITH_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account.getIdToken());
            } else {
                Toast.makeText(getApplicationContext(), failedCaseMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
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
