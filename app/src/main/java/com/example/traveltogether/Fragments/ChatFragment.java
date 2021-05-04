package com.example.traveltogether.Fragments;

import com.example.traveltogether.*;
import com.example.traveltogether.R;
import com.example.traveltogether.Adapter.*;
import com.example.traveltogether.Model.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.traveltogether.Fragments.ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fUser;
    DatabaseReference dbr;

    private List<String> usersList;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static com.example.traveltogether.Fragments.ChatFragment newInstance(String param1, String param2) {
        com.example.traveltogether.Fragments.ChatFragment fragment = new com.example.traveltogether.Fragments.ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        dbr = FirebaseDatabase.getInstance().getReference("Journeys");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Journey j = snapshot.getValue(Journey.class);
                    List<String> list = j.getUserList();
                    boolean inJourney = false;
                    for (String member : list) {
                        if (member.equals(fUser.getUid())) {
                            inJourney = true;
                        }
                    }
                    for (String member : list) {
                        if (inJourney == true) {
                            usersList.add(member);
                        }
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        dbr = FirebaseDatabase.getInstance().getReference("Users");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    // display 1 user from chats
                    for (String id : usersList) {
                        boolean inlist = false;
                        assert user != null;
                        if (user.getId() != null) {
                            if (!id.equals(fUser.getUid())) {
                                if (user.getId().equals(id)) {
                                    if (mUsers.size() != 0) {
                                        for (User userl : mUsers) {
                                            if (user.getId().equals(userl.getId())) {
                                                inlist = true;
                                                break;
                                            }
                                        }
                                        if (!inlist) {
                                            //if not already added
                                            mUsers.add(user);
                                        }
                                    } else {
                                        mUsers.add(user);
                                    }
                                }
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}