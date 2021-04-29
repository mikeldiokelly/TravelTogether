package com.example.traveltogether;
import com.example.traveltogether.Model.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.location.LocationComponent;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_LOCATION = 999;
    private Button banner, register;
    private EditText first_name, last_name, age, email, password, gender, perm_residence;
    private Point perm_res, curr_loc;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        //banner = (TextView) findViewById(R.id.banner);
        //banner.setOnClickListener(this);

        register =  (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email_address);
        password = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        gender = (EditText) findViewById(R.id.gender);
        perm_residence = (EditText) findViewById(R.id.perm_residence);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateLocation();

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        currentLocation = location;
                        perm_res = Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude());
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                register ();
                break;
        }
    }

    private void register() {
        String r_first = first_name.getText().toString().trim();
        String r_last = last_name.getText().toString().trim();
        String r_age = age.getText().toString().trim();
        String r_email = email.getText().toString().trim();
        String r_pass = password.getText().toString().trim();
        String r_gender = gender.getText().toString().trim();
        String r_permRes = perm_residence.getText().toString().trim();
        Point r_perm = perm_res;
        Point r_curr = curr_loc;


        if (r_first.isEmpty()) {
            first_name.setError("Please enter your first name.");
            first_name.requestFocus();
            return;
        }
        if (r_last.isEmpty()) {
            last_name.setError("Please enter your last name.");
            last_name.requestFocus();
            return;
        }
        if (r_age.isEmpty()) {
            age.setError("Please enter your age.");
            age.requestFocus();
            return;
        }
        if (r_email.isEmpty()) {
            email.setError("Please enter your email address.");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(r_email).matches()) {
            email.setError("Please enter a valid email address.");
            email.requestFocus();
            return;
        }
        if (r_pass.isEmpty()) {
            password.setError("Please enter a password.");
            password.requestFocus();
            return;
        }
        if (r_pass.length() < 6) {
            password.setError("Password must have at least 6 characters.");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(r_email, r_pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            User user = new User(r_first, r_last, r_age, r_email, userid, r_gender, r_perm, r_curr);
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("numRating", "0");
                            hashMap.put("avgRating", "0.0");
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUserActivity.this, "Sign-up was successful!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(RegisterUserActivity.this, "Sign-up failed. Try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterUserActivity.this, "Sign-up failed. Try again.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void addNewUser(){
        String r_first = first_name.getText().toString().trim();
        String r_last = last_name.getText().toString().trim();
        String r_age = age.getText().toString().trim();
        String r_email = email.getText().toString().trim();
        String r_pass = password.getText().toString().trim();
        String r_gender = gender.getText().toString().trim();
        String r_permRes = perm_residence.getText().toString().trim();
        Point r_perm = perm_res;
        Point r_curr = curr_loc;


        if (r_first.isEmpty()) {
            first_name.setError("Please enter your first name.");
            first_name.requestFocus();
            return;
        }
        if (r_last.isEmpty()) {
            last_name.setError("Please enter your last name.");
            last_name.requestFocus();
            return;
        }
        if (r_age.isEmpty()) {
            age.setError("Please enter your age.");
            age.requestFocus();
            return;
        }
        if (r_email.isEmpty()) {
            email.setError("Please enter your email address.");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(r_email).matches()) {
            email.setError("Please enter a valid email address.");
            email.requestFocus();
            return;
        }
        if (r_pass.isEmpty()) {
            password.setError("Please enter a password.");
            password.requestFocus();
            return;
        }
        if (r_pass.length() < 6) {
            password.setError("Password must have at least 6 characters.");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(r_email, r_pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            User user = new User(r_first, r_last, r_age, r_email, userid, r_gender, r_perm, r_curr);
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("numRating", "0");
                            hashMap.put("avgRating", "0.0");
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUserActivity.this, "Sign-up was successful!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(RegisterUserActivity.this, "Sign-up failed. Try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterUserActivity.this, "Sign-up failed. Try again.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}