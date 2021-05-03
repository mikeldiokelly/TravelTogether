package com.example.traveltogether;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private ProgressBar forgotBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.forgot_email);
        Button resetPassword = findViewById(R.id.reset_password);
        forgotBar = findViewById(R.id.forgotBar);
        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(v -> reset_pass());
    }

    private void reset_pass() {
        String fEmail = email.getText().toString().trim();
        if (fEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(fEmail).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        forgotBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(fEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ForgotPasswordActivity.this, "Please check your email to reset your password", Toast.LENGTH_LONG).show();
                forgotBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(ForgotPasswordActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                forgotBar.setVisibility(View.VISIBLE);
            }
        });
    }
}