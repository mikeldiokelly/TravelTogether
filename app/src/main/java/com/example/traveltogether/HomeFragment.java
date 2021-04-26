package com.example.traveltogether;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HomeFragment extends Fragment {

    private static final String TAG = "DirectionsActivity";
    private Button createJourneyBtn, createCommuteBtn, createRealTimeBtn, myJourneysBtn;

    public HomeFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        createJourneyBtn = view.findViewById(R.id.CreateJourneyBtn);
        createJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateJourneyActivity.class);
                startActivity(intent);
            }
        });
        createCommuteBtn = view.findViewById(R.id.CreateCommuteBtn);
        createCommuteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateCommuteActivity.class);
                startActivity(intent);
            }
        });

        createRealTimeBtn = view.findViewById(R.id.createRealTimeBtn);
        createRealTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WifiPeerToPeer.class);
                startActivity(intent);
            }
        });

        myJourneysBtn = view.findViewById(R.id.myJourneysBtn);
        myJourneysBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyJourneys.class));
            }
        });


        Button mapsBtn = view.findViewById(R.id.cartoTypeMaps);
        mapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartoTypeMaps.class));
            }
        });

        return view;
    }



}