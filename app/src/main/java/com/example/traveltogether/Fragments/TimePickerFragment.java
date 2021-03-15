package com.example.traveltogether.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.example.traveltogether.Model.Journey;
import com.example.traveltogether.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.traveltogether.CreateJourneyActivity.endPoint;
import static com.example.traveltogether.CreateJourneyActivity.source;

import java.util.Calendar;
import java.util.List;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static String journeyTime;           //TODO: make this better...
    private List<User> users;

    private FirebaseAuth mAuth;                 //TODO: organize
    ItemViewModel viewModel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        mAuth = FirebaseAuth.getInstance();

        // viewModel to communicate between fragments and activity
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        journeyTime = ""+ hourOfDay + " : " + minute;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();  //TODO: get stuff from this user
        System.out.println(user);


        //sending into viewModel
        viewModel.selectItem(journeyTime);


        Journey journey = new Journey(1, users, source.coordinates(), endPoint.coordinates(), journeyTime, true, "");
        FirebaseDatabase.getInstance().getReference("Journeys")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(journey).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("done");
                }
                else {
                    System.out.println("......");
                }
            }
        });
    }
}

