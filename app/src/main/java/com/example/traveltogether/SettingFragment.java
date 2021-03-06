package com.example.traveltogether;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView logout = view.findViewById(R.id.setting_logout);
        logout.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        TextView journeyPreference = view.findViewById(R.id.setting_userPreference);
        journeyPreference.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PreferenceActivity.class);
            startActivity(intent);
        });

        return view;
    }

}