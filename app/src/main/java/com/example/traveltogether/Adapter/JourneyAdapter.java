package com.example.traveltogether.Adapter;
import com.example.traveltogether.R;
import com.example.traveltogether.*;
import com.example.traveltogether.Model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JourneyAdapter extends RecyclerView.Adapter<com.example.traveltogether.Adapter.JourneyAdapter.ViewHolder> {
    private Context mContext;
    private List<Journey> mJourneys;

    public JourneyAdapter(Context mContext,  List<Journey> mJourneys) {
//        super(mContext,resource,textViewResourceId, mJourneys);
        this.mJourneys = mJourneys;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.journey_item, parent, false);
        return new com.example.traveltogether.Adapter.JourneyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Journey journey = mJourneys.get(position);
        holder.username.setText(String.valueOf(journey.getFirstUser()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // todo: send request to FireBase -> Firebase ask the other user to confirm
                //       if confirmed, update database, and send notification to this user

                // add the current user id to the userList of this Journey
                journey.addUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseDatabase.getInstance().getReference("Journeys")
                        .child(journey.getId())
                        .setValue(journey);
                // update my journey


                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mJourneys.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        Button btnJoin;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.journey_username);
            profile_image = itemView.findViewById(R.id.j_profile_image);

            btnJoin = (Button) itemView.findViewById(R.id.journeyItemJoinBtn);

        }
    }



}
