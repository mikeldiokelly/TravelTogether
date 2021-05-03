package com.example.traveltogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.traveltogether.Adapter.RateUserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RateUserAdapter rateUserAdapter;
    private List<String> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        recyclerView = findViewById(R.id.ratingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String jid = getIntent().getStringExtra("journey_id");

        usersList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Journeys").child(jid).child("userList");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String userId = snapshot1.getValue(String.class);
                    if(! userId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        usersList.add(userId);
                    }
                }
                rateUserAdapter = new RateUserAdapter(RatingActivity.this, usersList);
                recyclerView.setAdapter(rateUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button submitRatingBtn = findViewById(R.id.submitRatingBtn);
        submitRatingBtn.setOnClickListener(v -> {
            rate();
            Intent intent = new Intent(RatingActivity.this, MainActivity.class);
            startActivity(intent);

        });

    }

    public void rate() {
        List<Double> ratingList = rateUserAdapter.getRatingList();
        List<Double> oldRatingList = rateUserAdapter.getOldRatingList();
        List<Integer> numRatingList = rateUserAdapter.getNumRatingList();
        List<String> UserIDList = rateUserAdapter.getUserIDList();
        for (int i = 0; i < UserIDList.size(); i++) {
            int numRating = numRatingList.get(i);
            Double newRating = (oldRatingList.get(i) * numRating + ratingList.get(0)) / (numRating + 1);
            HashMap<String, Object> values = new HashMap<>();
            values.put("numRating", numRatingList.get(i) + 1);
            values.put("avgRating", newRating);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(UserIDList.get(i))
                    .updateChildren(values);
        }

    }

}