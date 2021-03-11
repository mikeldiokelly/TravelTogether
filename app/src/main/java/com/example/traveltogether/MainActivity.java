package com.example.traveltogether;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login_register, login, forgot;
    private EditText login_email, login_pass;

    private FirebaseAuth mauth;
    private ProgressBar loginBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        login_register = (Button) findViewById(R.id.login_register);
        login_register.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        forgot = (Button) findViewById(R.id.forgot);
        forgot.setOnClickListener(this);

        login_email = (EditText) findViewById(R.id.login_email);
        login_pass = (EditText) findViewById(R.id.login_password);
        loginBar = (ProgressBar) findViewById(R.id.login_progressBar);

        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser() != null) {
            startActivity(new Intent(this, ChatActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            case R.id.login:
                User_login();
                break;
            case R.id.forgot:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void User_login() {
        String email = login_email.getText().toString().trim();
        String password = login_pass.getText().toString().trim();

        if (email.isEmpty()) {
            login_email.setError("Please enter your email address.");
            login_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            login_email.setError("Please enter a valid email address.");
            login_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            login_pass.setError("Please enter a password.");
            login_pass.requestFocus();
            return;
        }
        if (password.length() < 6) {
            login_pass.setError("Password must have at least 6 characters.");
            login_pass.requestFocus();
            return;
        }
        loginBar.setVisibility(View.VISIBLE);
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(com.example.traveltogether.MainActivity.this, ChatActivity.class));
                        loginBar.setVisibility(View.GONE);
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(com.example.traveltogether.MainActivity.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                        loginBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(com.example.traveltogether.MainActivity.this, "Login failed. Try again.", Toast.LENGTH_LONG).show();
                    loginBar.setVisibility(View.GONE);
                }
            }
        });
    }
}