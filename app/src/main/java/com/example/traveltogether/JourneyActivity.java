package com.example.traveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveltogether.Model.Journey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JourneyActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 999;
    TextView journeySource, journeyDestination, journeyTime;
    Button routeToStart, ratingButton;
    String source;
    String destination;
    Button startJourneyBtn, endJourneyBtn;
    String hostID, journeyId, journeyStatus;
    ListView usersInJourney;
    DatabaseReference reference;
    List<String> userNameList;
    private FusedLocationProviderClient fusedLocationClient;
    private android.location.Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        updateLocation();

        journeyStatus = getIntent().getStringExtra("journey_status");
        journeyId = getIntent().getStringExtra("journey_id");

        source= getIntent().getStringExtra("journey_source");
        destination= getIntent().getStringExtra("journey_destination");

        journeySource = findViewById(R.id.journeySource);
        journeySource.setText(getIntent().getStringExtra("journey_source_address"));

        journeyDestination = findViewById(R.id.journeyDestination);
        journeyDestination.setText(getIntent().getStringExtra("journey_destination_address"));

        journeyTime = findViewById(R.id.journeyStartTime);
        journeyTime.setText(getIntent().getStringExtra("journey_time"));
        displayCompanionList();

        hostID = getIntent().getStringExtra("host_id");

        routeToStart = findViewById(R.id.journeyRouteToSource);
        routeToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLocationStr =
                        Point.fromLngLat(currentLocation.getLongitude(), currentLocation.getLatitude()).coordinates().toString(); ;

                displayMapWithRoute(currentLocationStr, destination);
            }
        });

        startJourneyBtn = findViewById(R.id.startJourneyBtn);
        startJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJourney();
            }
        });
        endJourneyBtn = findViewById(R.id.endJourneyBtn);
        endJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endJourney();
            }
        });

        ratingButton = findViewById(R.id.rateCompanions);
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("journey status in journey activity ---->");
                System.out.println(journeyStatus);
                System.out.println(Journey.JourneyStatus.FINISHED.toString());
                if(!journeyStatus.equals(Journey.JourneyStatus.FINISHED.toString())){
                    Toast.makeText(JourneyActivity.this, "Rating can not be done as journey has not finished yet", Toast.LENGTH_SHORT).show();
                }
                else{
                    showRatingActivity();
                }
            }
        });
    }

    private void displayMapWithRoute(String source, String destination) {
        Intent navigation = new Intent(JourneyActivity.this, NavigationActivity.class);
        navigation.putExtra("journey_source", source);
        navigation.putExtra("journey_destination", destination);
        startActivity(navigation);

        Log.d(" clicked ", "clicked on routeToSource ");
    }

    private void startJourney() {
        String jid = getIntent().getStringExtra("journey_id");
        //check if the current user is the host

        if (hostID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            FirebaseDatabase.getInstance().getReference("Journeys")
                    .child(jid).child("journeyStatus")
                    .setValue(Journey.JourneyStatus.ONGOING);
            Toast.makeText(JourneyActivity.this, "The journey has started", Toast.LENGTH_SHORT).show();
            displayMapWithRoute(source, destination);
        }
        else{
            Toast.makeText(JourneyActivity.this, "You are not the host", Toast.LENGTH_SHORT).show();
        }
    }

    private void endJourney() {
        String jid = getIntent().getStringExtra("journey_id");
        //check if the current user is the host

        if (hostID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            FirebaseDatabase.getInstance().getReference("Journeys")
                    .child(jid).child("journeyStatus")
                    .setValue(Journey.JourneyStatus.FINISHED);
            showRatingActivity();
        }
        else{
            Toast.makeText(JourneyActivity.this, "You are not the host", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRatingActivity() {
        Intent intent = new Intent(JourneyActivity.this, RatingActivity.class);
        intent.putExtra("journey_id", journeyId);
        startActivity(intent);
    }


    private void displayCompanionList() {
        String[] usersList = getIntent().getStringArrayExtra("users_in_journey");

        userNameList  = new ArrayList<>();

        for(String uId : usersList) {

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

                reference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userName = snapshot.child("first_name").getValue().toString();

                        if(uId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            userName = userName + " (HOST)";
                        }

                        userNameList.add(userName);
                        usersInJourney = findViewById(R.id.users_in_journey_list);
                        usersInJourney.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, userNameList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        }

    }

    private void updateLocation() {
        Log.d("current location -> ", "location: ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        Log.d("current location -> ", "location: " + location);
                        Log.d("current location -> ", "location lat: " + location.getLatitude());
                        Log.d("current location -> ", "location long: " + location.getLongitude());
                        currentLocation = location;
                    }
                });
    }

}