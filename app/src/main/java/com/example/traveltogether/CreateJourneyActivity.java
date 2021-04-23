package com.example.traveltogether;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.example.traveltogether.Fragments.TimePickerFragment;
import com.example.traveltogether.Model.Journey;
import com.example.traveltogether.Model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

import com.mapbox.mapboxsdk.location.modes.CameraMode;

import org.w3c.dom.Text;


/**
 * Display {@link SymbolLayer} icons on the map.
 */
public class CreateJourneyActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 999;
    private static final String LOCATION_TO_SOURCE = "location_to_search";
    public ItemViewModel viewModel;
    static final int SRC_MAPACTIVITY = 1;
    static final int DEST_MAPACTIVITY = 2;
    TextView srcLocation, destLocation;
    private DatabaseReference mDatabase;
    private EditText srcSearchLocationText, destSearchLocationText;
    private String souceLatLong, destLatLong;
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
            System.out.println("heres our item !!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(item);
        });
        setContentView(R.layout.activity_create_journey);

        srcSearchLocationText = findViewById(R.id.src_location_search_text);
        Button srcLocationSearchButton = findViewById(R.id.src_location_search_button);
        srcLocationSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationSearchWith(CHOSEN_LOCATION_SRC, srcSearchLocationText.getText().toString());
            }
        });

        destSearchLocationText = findViewById(R.id.dest_location_search_text);
        Button destLocationSearchButton = findViewById(R.id.dest_location_search_button);
        destLocationSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationSearchWith(CHOSEN_LOCATION_DEST, destSearchLocationText.getText().toString());
            }
        });

        srcLocation = (TextView) findViewById(R.id.srcLoc);
        destLocation = (TextView) findViewById(R.id.destLoc);


        Button src_btn = findViewById(R.id.srcBtn);
        src_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
                startActivityForResult(intent, SRC_MAPACTIVITY);
            }
        });
        Button dest_btn = findViewById(R.id.destBtn);
        dest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
                startActivityForResult(intent, DEST_MAPACTIVITY);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button send_btn = findViewById(R.id.send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                writeNewLoc();
                Intent intent = new Intent(CreateJourneyActivity.this, SearchResultActivity.class);
                intent.putExtra("SOURCE", souceLatLong);
                intent.putExtra("DESTINATION", destLatLong);
                startActivity(intent);

            }
        });

    }

    private void startLocationSearchWith(int searchCode, String searchKeyword) {
        updateLocation();
        Intent _searchList = new Intent(CreateJourneyActivity.this, LocationSearch.class);
        _searchList.putExtra(LOCATION_TO_SOURCE, searchKeyword);
        _searchList.putExtra("current_location_lat", currentLocation.getLatitude());
        _searchList.putExtra("current_location_long", currentLocation.getLongitude());
        startActivityForResult(_searchList, searchCode);
    }

    private void updateLocation() {
        Log.d("current location -> ", "location: ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        Log.d("current location -> ", "location: " + location);
                        Log.d("current location -> ", "location lat: " + location.getLatitude());
                        Log.d("current location -> ", "location long: " + location.getLongitude());
                        currentLocation = location;
                    }
    });
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    @IgnoreExtraProperties
    public class Location {

        public String source;
        public String destination;

        public Location() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Location(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SRC_MAPACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    String locationAddress = data.getStringExtra("loc_address");
                    souceLatLong=returnValue.substring(42,returnValue.length()-2);
                    srcLocation.setText(locationAddress);
                }
                break;
            }
            case (DEST_MAPACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    String locationAddress = data.getStringExtra("loc_address");
                    destLatLong=returnValue.substring(42,returnValue.length()-2);
                    destLocation.setText(locationAddress);
                }
                break;
            }
            case (CHOSEN_LOCATION_SRC) : {
                if (resultCode == Activity.RESULT_OK) {
                    String locationName = data.getStringExtra("location_name");
                    String locationPoint = data.getStringExtra("location_point");
                    souceLatLong = locationPoint.substring(42, locationPoint.length()-2);
                    srcLocation.setText(locationName);                                                         //reverse geocode for other cases with location picker !
                }
                break;
            }
                case (CHOSEN_LOCATION_DEST) : {
                    if (resultCode == Activity.RESULT_OK){
                        String locationName = data.getStringExtra("location_name");
                        String locationPoint = data.getStringExtra("location_point");
                        souceLatLong = locationPoint.substring(42, locationPoint.length()-2);
                        destLocation.setText(locationName);                                                         //reverse geocode for other cases..
                    }
            }
        }
    }


//    public void writeNewLoc() {
//
//        FirebaseUser userN = FirebaseAuth.getInstance().getCurrentUser();
//        List<String> users = new ArrayList();
//        users.add(userN.getUid());
//
//        List<Double> src = new ArrayList();List<Double> dest = new ArrayList();
//        Double srcLong = Double.parseDouble( souceLatLong.substring(0,souceLatLong.indexOf(",")));
//        String temp = souceLatLong.substring(souceLatLong.indexOf(",")+1);
//        Double srcLat = Double.parseDouble( temp);
//        System.out.println(srcLong);
//        System.out.println(srcLat);
//        src.add(srcLong);src.add(srcLat);
//        Double destLong = Double.parseDouble( destLatLong.substring(0,destLatLong.indexOf(",")));
//        Double destLat = Double.parseDouble( destLatLong.substring(destLatLong.indexOf(",")+1));
//        dest.add(destLong);dest.add(destLat);
//        System.out.println(destLong);
//        System.out.println(destLat);
//
//
//        DatabaseReference dbr =FirebaseDatabase.getInstance().getReference("Journeys");
//        DatabaseReference newPostRef =  dbr.push();
//        // update journey ID
//        journey = new Journey(newPostRef.getKey(), users, src, dest ,"journeyTime", true, "");
//        mJourneyID = newPostRef.getKey();
//        // todo: keep a mJourneyList
//
//
//        newPostRef.setValue(journey);
//    }





}