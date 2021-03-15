package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.example.traveltogether.Fragments.TimePickerFragment;

public class CreateCommute extends AppCompatActivity {

    public ItemViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        // viewModel listeners
        viewModel.getSelectedItem().observe(this, item -> {
            System.out.println("heres our item !!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(item);
        });
        setContentView(R.layout.activity_create_commute2);

        System.out.println("in new activity ...........!!!!!!!!!!!!!!");
//        System.out.println(source);
//        System.out.println(endPoint);


    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}