package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
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
    private Button src_btn, dest_btn;
    static final  int SRC_MAPACTIVITY = 1;
    static final  int DEST_MAPACTIVITY = 2;
    TextView srcLocation, destLocation;
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

    }
    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SRC_MAPACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    System.out.println("HAHAHA "+returnValue);
                    srcLocation.setText(returnValue);
                }
                break;
            }
            case (DEST_MAPACTIVITY) : {
                if (resultCode == Activity.RESULT_OK) {
                    String returnValue = data.getStringExtra("loc");
                    System.out.println("HAHAHA "+returnValue);
                    destLocation.setText(returnValue);
                }
                break;
            }
        }
    }






}