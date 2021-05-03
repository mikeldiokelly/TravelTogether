package com.example.traveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.traveltogether.Model.Journey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MyJourneysActivity extends AppCompatActivity {

    ListView listView;
    Journey[] journeyList;
    String[] journeyTitles;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journeys);

        getMyJourneys();

        listView = findViewById(R.id.myJourneysList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showJourneyInterface(position);
            }
        });

    }

    private void showJourneyInterface(int position) {
        Journey journey = journeyList[position];
        Intent _journey = new Intent(MyJourneysActivity.this, JourneyActivity.class);

        String[] usersList = new String[journey.getUserList().size()];
        usersList = journey.getUserList().toArray(usersList);

        _journey.putExtra("journey_source", journey.getSource().toString());
        _journey.putExtra("journey_source_address", journey.getSourceAddress());
        _journey.putExtra("journey_destination_address", journey.getDestAddress());
        _journey.putExtra("journey_destination", journey.getDestination().toString());
        _journey.putExtra("journey_time", journey.getStartTime());
        _journey.putExtra("journey_id", journey.getId());
        _journey.putExtra("host_id", journey.getHost());
        _journey.putExtra("users_in_journey", usersList);
        _journey.putExtra("journey_status", journey.getJourneyStatus().toString());
        startActivity(_journey);
    }

    private void getMyJourneys() {

        reference = FirebaseDatabase.getInstance().getReference().child("Journeys");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int index = 0;
                journeyTitles = new String[(int) snapshot.getChildrenCount()];
                journeyList = new Journey[(int) snapshot.getChildrenCount()];

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Journey j = snapshot1.getValue(Journey.class);

                    journeyList[index] = j;
                    journeyTitles[index] = getJourneyTitleFromJourney(j);
                    index++;
                }

                listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, journeyTitles));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    String getJourneyTitleFromJourney(Journey journey){
        String title = "";
        title += journey.getSourceAddress() + " -> " + journey.getDestAddress();
        return title;
    }

}