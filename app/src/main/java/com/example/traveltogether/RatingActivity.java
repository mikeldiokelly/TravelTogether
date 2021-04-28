package com.example.traveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.traveltogether.Adapter.JourneyAdapter;
import com.example.traveltogether.Adapter.RateUserAdapter;
import com.example.traveltogether.Model.Journey;
import com.example.traveltogether.Model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RateUserAdapter rateUserAdapter;
    String jid;

    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;
    Button returnToHomeBtn;

//    private List<String> usersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

//        recyclerView = findViewById(R.id.partnerList);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        jid = getIntent().getStringExtra("journey_id");
//
//        mUsers = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference().child("Journeys");
//

    }

}