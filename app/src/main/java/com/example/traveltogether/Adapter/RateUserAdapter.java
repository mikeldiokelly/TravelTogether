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
import com.example.traveltogether.RatingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RateUserAdapter extends RecyclerView.Adapter<com.example.traveltogether.Adapter.RateUserAdapter.ViewHolder> {
    private Context mContext;
    private List<String> userIDList;
    private List<String> userNameList;
    private List<Double> ratingList,oldRatingList;
    private List<Integer> numRatingList;

    DatabaseReference reference;

    public RateUserAdapter(Context mContext,  List<String> mUsers) {
//        super(mContext,resource,textViewResourceId, mJourneys);
        this.userIDList = mUsers;
        this.mContext = mContext;
        this.userNameList = new ArrayList<>();
        this.oldRatingList = new ArrayList<>();
        this.numRatingList = new ArrayList<>();
        for(int i =0;i<mUsers.size();i++)
                userNameList.add("No Name");
        this.ratingList = new ArrayList<>();
        for(int i =0;i<mUsers.size();i++)
            ratingList.add((Double)5.0);
    }

    public List<Double> getRatingList(){
        return ratingList;
    }
    public List<Double> getOldRatingList(){
        return oldRatingList;
    }

    public List<Integer> getNumRatingList(){
        return numRatingList;
    }
    public List<String> getUserIDList(){
        return userIDList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rate_user_item, parent, false);
        return new com.example.traveltogether.Adapter.RateUserAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userID = userIDList.get(position);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userName = snapshot.child("first_name").getValue().toString();
                    userNameList.add(userName);
                    holder.username.setText(userName);

                    int numRating = Integer.parseInt(snapshot.child("numRating").getValue().toString());
                    Double oldRating = Double.parseDouble(snapshot.child("avgRating").getValue().toString());
                    numRatingList.add(numRating);
                    oldRatingList.add(oldRating);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        holder.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingList.set(position, (double) rating);
            }
        });

    }







    @Override
    public int getItemCount() {
        return userIDList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;
        RatingBar ratingbar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.rating_username);
            profile_image = itemView.findViewById(R.id.rating_profile);
            ratingbar = itemView.findViewById(R.id.ratingBar);

        }
    }



}
