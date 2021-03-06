package com.example.traveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.traveltogether.Adapter.JourneyAdapter;
import com.example.traveltogether.Model.Journey;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JourneyAdapter journeyAdapter;
    private List<Journey> mJourneys;
    private String sourceLatLong, destLatLong, startTime, sourceAddress, destinationAddress;

    FirebaseUser fUser;
    DatabaseReference reference;
    Button returnToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        sourceLatLong = getIntent().getStringExtra("SOURCE");
        destLatLong = getIntent().getStringExtra("DESTINATION");
        startTime = getIntent().getStringExtra("START_TIME");
        sourceAddress = getIntent().getStringExtra("SOURCE_ADDRESS");
        destinationAddress = getIntent().getStringExtra("DESTINATION_ADDRESS");

        recyclerView = findViewById(R.id.list1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mJourneys = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("Journeys");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Journey j = snapshot1.getValue(Journey.class);
                    mJourneys.add(j);
                }
                journeyAdapter = new JourneyAdapter(SearchResultActivity.this, mJourneys);
                recyclerView.setAdapter(journeyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        returnToHomeBtn = findViewById(R.id.returnToHomeBtn);
        returnToHomeBtn.setOnClickListener(v -> {
            if(!sourceAddress.equals("src location") && !sourceAddress.equals("")) {
                addNewJourney(sourceLatLong, destLatLong, sourceAddress, destinationAddress);
                Intent intent = new Intent(SearchResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Snackbar.make( returnToHomeBtn ,"Go back and select source and destination", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void addNewJourney(String sourceLatLong, String destLatLong, String sourceAddress, String destinationAddress) {

        FirebaseUser userN = FirebaseAuth.getInstance().getCurrentUser();
        List<String> users = new ArrayList();
        users.add(userN.getUid());

        List<Double> src = new ArrayList();
        List<Double> dest = new ArrayList();
        Double srcLong = Double.parseDouble(sourceLatLong.substring(0, sourceLatLong.indexOf(",")));
        String temp = sourceLatLong.substring(sourceLatLong.indexOf(",") + 1);
        Double srcLat = Double.parseDouble(temp);
        System.out.println(srcLong);
        System.out.println(srcLat);
        src.add(srcLong);
        src.add(srcLat);
        Double destLong = Double.parseDouble(destLatLong.substring(0, destLatLong.indexOf(",")));
        Double destLat = Double.parseDouble(destLatLong.substring(destLatLong.indexOf(",") + 1));
        dest.add(destLong);
        dest.add(destLat);
        System.out.println(destLong);
        System.out.println(destLat);


        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Journeys");
        DatabaseReference newPostRef = dbr.push();
        // update journey ID
        Journey journey = new Journey(newPostRef.getKey(), users, src, dest, startTime, true, "", sourceAddress, destinationAddress);

        // todo: keep a mJourneyList

        newPostRef.setValue(journey);
    }


}