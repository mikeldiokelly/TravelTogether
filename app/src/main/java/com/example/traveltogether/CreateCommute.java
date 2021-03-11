package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import com.example.traveltogether.Fragments.TimePickerFragment;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import java.util.List;

import javax.xml.transform.Source;

import static com.example.traveltogether.CreateJourneyActivity.currentRoute;
import static com.example.traveltogether.CreateJourneyActivity.endPoint;
import static com.example.traveltogether.CreateJourneyActivity.source;
import static com.example.traveltogether.Fragments.TimePickerFragment.journeyTime;

public class CreateCommute extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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