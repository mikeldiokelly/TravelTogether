package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.traveltogether.Model.Journey;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.FirebaseDatabase;

public class JourneyActivity extends AppCompatActivity {

    TextView journeySource, journeyDestination, journeyTime;
    Button routeToStart, startNavigation;
    String source;
    String destination;
    Button startJourneyBtn, endJourneyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        journeySource = findViewById(R.id.journeySource);
        source= getIntent().getStringExtra("journey_source");
        journeySource.setText(source);

        journeyDestination = findViewById(R.id.journeyDestination);
        destination= getIntent().getStringExtra("journey_destination");
        journeyDestination.setText(destination);

        journeyTime = findViewById(R.id.journeyStartTime);
        journeyTime.setText(getIntent().getStringExtra("journey_time"));


        routeToStart = findViewById(R.id.journeyRouteToSource);
        routeToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(" clicked ", "clicked on routeToSource ");
            }
        });

        startNavigation = findViewById(R.id.startJourney);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigation = new Intent(JourneyActivity.this, NavigationActivity.class);
                navigation.putExtra("journey_source", source);
                navigation.putExtra("journey_destination", destination);
                startActivity(navigation);
            }
        });
        startJourneyBtn = findViewById(R.id.startJourneyBtn);
        startJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set firebase journey status
                String jid = getIntent().getStringExtra("journey_id");
                FirebaseDatabase.getInstance().getReference("Journeys")
                        .child(jid).child("journeyStatus")
                        .setValue(Journey.JourneyStatus.ONGOING);
            }
        });
        endJourneyBtn = findViewById(R.id.endJourneyBtn);
        endJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String jid = getIntent().getStringExtra("journey_id");

                FirebaseDatabase.getInstance().getReference("Journeys")
                        .child(jid).child("journeyStatus")
                        .setValue(Journey.JourneyStatus.FINISHED);
                Intent intent = new Intent(JourneyActivity.this, RatingActivity.class);
                intent.putExtra("journey_id", jid);
                startActivity(intent);

            }
        });
    }
}