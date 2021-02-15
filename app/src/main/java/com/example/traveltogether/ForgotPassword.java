package com.example.traveltogether;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    private EditText email;
    private Button reset_password;
    private ProgressBar forgotBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = (EditText) findViewById(R.id.forgot_email);
        reset_password = (Button) findViewById(R.id.reset_password);
        forgotBar = (ProgressBar) findViewById(R.id.forgotBar);
        auth = FirebaseAuth.getInstance();

        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_pass();
            }
        });
    }

    private void reset_pass() {
        String f_email = email.getText().toString().trim();
        if (f_email.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(f_email).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        forgotBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(f_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(com.example.traveltogether.ForgotPassword.this, "Please check your email to reset your password", Toast.LENGTH_LONG).show();
                    forgotBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(com.example.traveltogether.ForgotPassword.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    forgotBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}