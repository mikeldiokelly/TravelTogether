package com.example.traveltogether.Adapter;

import com.example.traveltogether.R;
import com.example.traveltogether.*;
import com.example.traveltogether.Model.*;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JourneyAdapter extends RecyclerView.Adapter<com.example.traveltogether.Adapter.JourneyAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Journey> mJourneys;
    List<String> mUsers;
    FirebaseUser fuser;

    public JourneyAdapter(Context mContext, List<Journey> mJourneys) {
        this.mJourneys = mJourneys;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.journey_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journey journey = mJourneys.get(position);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        holder.username.setText(String.format("%s -> %s", journey.getSourceAddress(), journey.getDestination()));


        holder.btnJoin.setOnClickListener(view -> joinJourney(journey, holder.btnJoin));
    }

    private void joinJourney(Journey journey, ImageButton btnJoin) {
        // TODO: send request to FireBase -> Firebase ask the other user to confirm
        //       if confirmed, update database, and send notification to this user

        // check whether the user is in the user list
        mUsers = journey.getUserList();

        boolean joined = false;
        for (String newuser : mUsers) {
            if (fuser.getUid().equals(newuser))
                joined = true;
        }

        if (!joined) {
            // add user
            journey.addUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            FirebaseDatabase.getInstance().getReference("Journeys")
                    .child(journey.getId())
                    .setValue(journey);
            // delete
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        } else {
            Snackbar.make(btnJoin,"You already joined this", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return mJourneys.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        ImageButton btnJoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.journey_username);
            profile_image = itemView.findViewById(R.id.j_profile_image);
            btnJoin = itemView.findViewById(R.id.journeyItemJoinBtn);
        }
    }


}
