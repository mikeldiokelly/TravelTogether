package com.example.traveltogether.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveltogether.MainActivity;
import com.example.traveltogether.Model.Journey;
import com.example.traveltogether.Model.User;
import com.example.traveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RateUserAdapter extends RecyclerView.Adapter<com.example.traveltogether.Adapter.RateUserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference ref;

    public RateUserAdapter(Context mContext,  List<User> mUsers) {
//        super(mContext,resource,textViewResourceId, mJourneys);
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.journey_item, parent, false);
        return new com.example.traveltogether.Adapter.RateUserAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        fuser = FirebaseAuth.getInstance().getCurrentUser();



    }







    @Override
    public int getItemCount() {
        return mUsers.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        RatingBar ratingbar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.journey_username);
            profile_image = itemView.findViewById(R.id.j_profile_image);
            ratingbar = itemView.findViewById(R.id.ratingBar);

        }
    }



}
