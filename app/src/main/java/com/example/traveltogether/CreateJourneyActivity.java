package com.example.traveltogether;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.traveltogether.Communicator.ItemViewModel;
import com.example.traveltogether.Fragments.TimePickerFragment;
import com.example.traveltogether.Model.Journey;
import com.example.traveltogether.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
    public ItemViewModel viewModel;
    private Button src_btn, dest_btn, send_btn;
    static final  int SRC_MAPACTIVITY = 1;
    static final  int DEST_MAPACTIVITY = 2;
    TextView srcLocation, destLocation;
    private DatabaseReference mDatabase;

    private  String souceLatLong, destLatLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getSelectedItem().observe(this, item -> {
            System.out.println("heres our item !!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(item);
        });
        setContentView(R.layout.activity_create_journey);

        srcLocation = (TextView)findViewById(R.id.srcLoc);
        destLocation = (TextView)findViewById(R.id.destLoc);


        src_btn= findViewById(R.id.srcBtn);
        src_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
                startActivityForResult(intent, SRC_MAPACTIVITY);
            }
        });
        dest_btn= findViewById(R.id.destBtn);
        dest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateJourneyActivity.this, MapActivity.class);
                startActivityForResult(intent, DEST_MAPACTIVITY);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        send_btn= findViewById(R.id.send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                writeNewLoc();
                Intent intent =new Intent(CreateJourneyActivity.this, SearchResultActivity.class);
                intent.putExtra("SOURCE",souceLatLong);
                intent.putExtra("DESTINATION",destLatLong);
                startActivity(intent);

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
                    souceLatLong=returnValue.substring(42,returnValue.length()-2);
                    srcLocation.setText(souceLatLong);
                }
                break;
            }
            case (DEST_MAPACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    destLatLong=returnValue.substring(42,returnValue.length()-2);
                    destLocation.setText(destLatLong);
                }
                break;
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