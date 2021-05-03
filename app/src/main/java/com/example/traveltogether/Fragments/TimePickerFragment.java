package com.example.traveltogether.Fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static String journeyTime;
    ItemViewModel viewModel;

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // viewModel to communicate between fragments and activity
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        journeyTime = "" + hourOfDay + " : " + minute;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println(user);

        //sending into viewModel
        viewModel.selectItem(journeyTime);
    }

}

