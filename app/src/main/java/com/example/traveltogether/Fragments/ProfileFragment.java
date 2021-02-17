package com.example.traveltogether.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.traveltogether.Model.User;
import com.example.traveltogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {
    ImageView image_profile;
    TextView username;

    DatabaseReference dbr ;
    FirebaseUser fuser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.prof_image);
        username = view.findViewById(R.id.username);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
//        dbr = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
//
//        dbr.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
//                User user = datasnapshot.getValue(User.class);
//                username.setText(user.getUsername());
//                if(user.getImageURL().equals("default")){
//                    Toast.makeText(getContext(), "use default profile",Toast.LENGTH_SHORT);
//                    image_profile.setImageResource(R.mipmap.ic_launcher);
//                }else{
////                    Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        return view;
    }
}