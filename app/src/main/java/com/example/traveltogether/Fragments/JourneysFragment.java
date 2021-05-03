package com.example.traveltogether.Fragments;

import com.example.traveltogether.*;
import com.example.traveltogether.Adapter.*;
import com.example.traveltogether.R;
import com.example.traveltogether.Model.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JourneysFragment extends Fragment {
    Button create_journey, p2pBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journeys, container, false);
        create_journey = view.findViewById(R.id.CreateJourney);
        create_journey.setOnClickListener(v -> {
            if (v.getId() == R.id.CreateJourney) {
                Intent intent1 = new Intent(JourneysFragment.this.getActivity(), CreateJourneyActivity.class);
                startActivity(intent1);
            }
        });

        p2pBtn = view.findViewById(R.id.p2pBtn);
        p2pBtn.setOnClickListener(v -> startActivity(new Intent(JourneysFragment.this.getActivity(), PeerToPeerActivity.class)));
        return view;
    }
}