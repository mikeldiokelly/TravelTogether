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
    TextView first, last, age;

    DatabaseReference dbr ;
    FirebaseUser fuser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        first = view.findViewById(R.id.first);
        last = view.findViewById(R.id.last);
        age = view.findViewById(R.id.p_age);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        dbr = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        getProfile();

        return view;
    }

   public void getProfile(){
       dbr.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot datasnapshot) {
               User user = datasnapshot.getValue(User.class);
               if (user!=null) {
                   first.setText(user.first_name);
                   last.setText(user.last_name);
                   age.setText(user.age + " years old");
                   if (user.getImageURL().equals("default")) {
                       Toast.makeText(getContext(), "use default profile",Toast.LENGTH_SHORT);
                       image_profile.setImageResource(R.drawable.user_dp);
                   }
                   else {
                       Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

   }
}