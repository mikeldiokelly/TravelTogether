package com.example.traveltogether;
//see
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
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

import java.io.IOException;
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


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
        // variables for adding location layer
        private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    // variables for calculating and drawing a route
    public static DirectionsRoute currentRoute;
    public  Point source;                                                // TODO: need a better way to do this...!
    public  Point endPoint;
    String endPointAddress;
    private static final String TAG = "DirectionsActivity";

    private NavigationMapRoute navigationMapRoute;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Mapbox.getInstance(this, getString(R.string.access_token));
            setContentView(R.layout.activity_map);
            mapView = findViewById(R.id.mapView2);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            // variables needed to initialize navigation

            Button button = findViewById(R.id.confirmSrcBtn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent _result = new Intent();
//                    _result.setData(Uri.parse(endPoint.toString()));
                    _result.putExtra("loc",endPoint.toString());
                    _result.putExtra("loc_address",endPointAddress);
                    setResult(Activity.RESULT_OK, _result);
                    finish();
                }
            });
        }

        @Override
        public void onMapReady(@NonNull final MapboxMap mapboxMap) {
            this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {

                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                    addDestinationIconSymbolLayer(style);
                    mapboxMap.addOnMapClickListener(MapActivity.this);

                }

            });
        }

        private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
            loadedMapStyle.addImage("destination-icon-id",
                    BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
            GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
            loadedMapStyle.addSource(geoJsonSource);
            SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
            destinationSymbolLayer.withProperties(
                    iconImage("destination-icon-id"),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true)
            );
            loadedMapStyle.addLayer(destinationSymbolLayer);
        }

        @SuppressWarnings( {"MissingPermission"})
        @Override
        public boolean onMapClick(@NonNull LatLng point) {
            endPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            MapboxGeocoding reverseGeocode = MapboxGeocoding.builder()
                    .accessToken(getString(R.string.access_token))
                    .query(endPoint)
                    .build();

            reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                    List<CarmenFeature> features = response.body().features();

                    String selectedAddress = "";
                    if(!features.isEmpty()){

                        CarmenFeature feature = features.get(0);

                        selectedAddress = feature.placeName();
                        endPointAddress = selectedAddress;
                        Log.d(" MapActivity ", " selectedAddress: feature " + feature);
                        Log.d(" MapActivity ", " selectedAddress: placeName " + feature.placeName());
                    }

                    TextView selectedLocationTextView = (TextView) findViewById(R.id.selectedLocationTextView);
                    selectedLocationTextView.setText(selectedAddress);

                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable t) {

                }
            });

            return true;
        }


        @SuppressWarnings( {"MissingPermission"})
        private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
            if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(this, loadedMapStyle);
                locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);
            } else {
                permissionsManager = new PermissionsManager(this);
                permissionsManager.requestLocationPermissions(this);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        @Override
        public void onExplanationNeeded(List<String> permissionsToExplain) {
            Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPermissionResult(boolean granted) {
            if (granted) {
                enableLocationComponent(mapboxMap.getStyle());
            } else {
                Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected void onStart() {
            super.onStart();
            mapView.onStart();
        }

        @Override
        protected void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mapView.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
            mapView.onStop();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            mapView.onSaveInstanceState(outState);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mapView.onDestroy();
        }

        @Override
        public void onLowMemory() {
            super.onLowMemory();
            mapView.onLowMemory();
        }

//        public  Point getSource() {
//            return source;
//        }
    }