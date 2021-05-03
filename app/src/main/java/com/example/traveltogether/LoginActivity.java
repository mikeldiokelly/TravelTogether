package com.example.traveltogether;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail, loginPass;

    private FirebaseAuth mAuth;
    private ProgressBar loginBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_register = findViewById(R.id.login_register);
        login_register.setOnClickListener(this);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(this);

        Button forgot = findViewById(R.id.forgot);
        forgot.setOnClickListener(this);

        loginEmail = findViewById(R.id.login_email);
        loginPass = findViewById(R.id.login_password);
        loginBar = findViewById(R.id.login_progressBar);
        login();
    }


    public void login() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            case R.id.login:
                String email = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();
                user_login(email, password);
                break;
            case R.id.forgot:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void user_login(String email, String password) {

        if (email.isEmpty()) {
            loginEmail.setError("Please enter your email address.");
            loginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email address.");
            loginEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            loginPass.setError("Please enter a password.");
            loginPass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            loginPass.setError("Password must have at least 6 characters.");
            loginPass.requestFocus();
            return;
        }
        loginBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }
                    loginBar.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login failed. Try again.", Toast.LENGTH_LONG).show();
                loginBar.setVisibility(View.GONE);
            }
        });
    }
}