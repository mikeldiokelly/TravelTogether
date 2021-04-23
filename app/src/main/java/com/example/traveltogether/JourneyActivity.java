package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JourneyActivity extends AppCompatActivity {

    TextView journeySource, journeyDestination, journeyTime;
    Button routeToStart, startJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey);

        journeySource = findViewById(R.id.journeySource);
        journeySource.setText(getIntent().getStringExtra("journey_source"));

        journeyDestination = findViewById(R.id.journeyDestination);
        journeyDestination.setText(getIntent().getStringExtra("journey_destination"));

        journeyTime = findViewById(R.id.journeyStartTime);
        journeyTime.setText(getIntent().getStringExtra("journey_time"));

        routeToStart = findViewById(R.id.journeyRouteToSource);
        routeToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(" clicked ", "clicked on routeToSource ");
            }
        });

        startJourney = findViewById(R.id.startJourney);
        startJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(" clicked ", "clicked on startJourney ");
            }
        });
    }
}