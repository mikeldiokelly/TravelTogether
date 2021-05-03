package com.example.traveltogether;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button createJourneyBtn = view.findViewById(R.id.CreateJourneyBtn);
        createJourneyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateJourneyActivity.class);
            startActivity(intent);
        });

        Button peerToPeerBtn = view.findViewById(R.id.peerToPeerBtn);
        peerToPeerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PeerToPeerActivity.class);
            startActivity(intent);
        });

        Button myJourneysBtn = view.findViewById(R.id.myJourneysBtn);
        myJourneysBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyJourneysActivity.class)));

        return view;
    }

}