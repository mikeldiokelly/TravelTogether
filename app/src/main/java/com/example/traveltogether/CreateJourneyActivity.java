package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.example.traveltogether.Fragments.TimePickerFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import static com.example.traveltogether.Fragments.TimePickerFragment.journeyTime;

/**
 * Display {@link SymbolLayer} icons on the map.
 */
public class CreateJourneyActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 999;
    private static final String LOCATION_TO_SOURCE = "location_to_search";
    public ItemViewModel viewModel;
    static final int SRC_MAP_ACTIVITY = 1;
    static final int DEST_MAP_ACTIVITY = 2;
    TextView srcLocation, destLocation;
    private EditText srcSearchLocationText, destSearchLocationText;
    private String sourceLatLong, destLatLong;
    private FusedLocationProviderClient fusedLocationClient;
    private android.location.Location currentLocation;

    static final int CHOSEN_LOCATION_SRC = 101;
    static final int CHOSEN_LOCATION_DEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        updateLocation();

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, item -> {
            TextView startTimeText;
            startTimeText = findViewById(R.id.startTimeText);
            startTimeText.setText(journeyTime);
        });
        setContentView(R.layout.activity_create_journey);

        srcSearchLocationText = findViewById(R.id.src_location_search_text);
        Button srcLocationSearchButton = findViewById(R.id.src_location_search_button);
        srcLocationSearchButton.setOnClickListener(v -> startLocationSearchWith(CHOSEN_LOCATION_SRC, srcSearchLocationText.getText().toString()));

        destSearchLocationText = findViewById(R.id.dest_location_search_text);
        Button destLocationSearchButton = findViewById(R.id.dest_location_search_button);
        destLocationSearchButton.setOnClickListener(v -> startLocationSearchWith(CHOSEN_LOCATION_DEST, destSearchLocationText.getText().toString()));

        srcLocation = findViewById(R.id.srcLoc);
        destLocation = findViewById(R.id.destLoc);

        ImageButton src_btn = findViewById(R.id.srcBtn);
        src_btn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
            startActivityForResult(intent, SRC_MAP_ACTIVITY);
        });
        ImageButton dest_btn = findViewById(R.id.destBtn);
        dest_btn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
            startActivityForResult(intent, DEST_MAP_ACTIVITY);
        });

        Button send_btn = findViewById(R.id.send);
        send_btn.setOnClickListener(v -> {
            Intent intent = new Intent(CreateJourneyActivity.this, SearchResultActivity.class);
            intent.putExtra("SOURCE", sourceLatLong);
            intent.putExtra("DESTINATION", destLatLong);
            intent.putExtra("START_TIME", journeyTime);
            intent.putExtra("SOURCE_ADDRESS", srcLocation.getText().toString());
            intent.putExtra("DESTINATION_ADDRESS", destLocation.getText().toString());
            startActivity(intent);

        });

    }


    private void startLocationSearchWith(int searchCode, String searchKeyword) {
        updateLocation();
        Intent _searchList = new Intent(CreateJourneyActivity.this, LocationSearchActivity.class);
        _searchList.putExtra(LOCATION_TO_SOURCE, searchKeyword);
        _searchList.putExtra("current_location_lat", currentLocation.getLatitude());
        _searchList.putExtra("current_location_long", currentLocation.getLongitude());
        startActivityForResult(_searchList, searchCode);
    }

    private void updateLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> currentLocation = location);
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (SRC_MAP_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    String locationAddress = data.getStringExtra("loc_address");
                    sourceLatLong = returnValue.substring(42, returnValue.length() - 2);
                    srcLocation.setText(locationAddress);
                }
                break;
            }
            case (DEST_MAP_ACTIVITY): {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    String locationAddress = data.getStringExtra("loc_address");
                    destLatLong = returnValue.substring(42, returnValue.length() - 2);
                    destLocation.setText(locationAddress);
                }
                break;
            }
            case (CHOSEN_LOCATION_SRC): {
                if (resultCode == Activity.RESULT_OK) {
                    String locationName = data.getStringExtra("location_name");
                    String locationPoint = data.getStringExtra("location_point");
                    sourceLatLong = locationPoint.substring(42, locationPoint.length() - 2);
                    srcLocation.setText(locationName);                                                         //reverse geocode for other cases with location picker !
                }
                break;
            }
            case (CHOSEN_LOCATION_DEST): {
                if (resultCode == Activity.RESULT_OK) {
                    String locationName = data.getStringExtra("location_name");
                    String locationPoint = data.getStringExtra("location_point");
                    destLatLong = locationPoint.substring(42, locationPoint.length() - 2);
                    destLocation.setText(locationName);                                                         //reverse geocode for other cases..
                }
            }
        }
    }

}