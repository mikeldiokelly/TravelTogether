package com.example.traveltogether;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.BitmapFactory;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.geometry.LatLng;
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

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.jetbrains.annotations.NotNull;

/**
 * Display {@link SymbolLayer} icons on the map.
 */
public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private MapboxMap mapboxMap;

    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Button button;
    private String source;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_navigation);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        source= getIntent().getStringExtra("journey_source");
        destination= getIntent().getStringExtra("journey_destination");
    }

    private Point convertStringToPoint(String value){
        double srcLong = Double.parseDouble( value.split("\\[")[1].split(",")[0]);
        double srcLat = Double.parseDouble( value.split("]")[0].split(" ")[1]);

        return Point.fromLngLat(srcLong, srcLat);
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {

            @Override
            public void onStyleLoaded(@NonNull Style style) {

                addDestinationIconSymbolLayer(style);

                Point origin = convertStringToPoint(source);
                Point destinationPoint = convertStringToPoint(destination);

                GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
                if (source != null) {
                    source.setGeoJson(Feature.fromGeometry(destinationPoint));
                }

                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(origin.latitude(), origin.longitude()))
                        .zoom(15)
                        .tilt(20)
                        .build();

                CameraUpdate update = mapboxMap1 -> position;

                mapboxMap.moveCamera(update);

                getRoute(origin, destinationPoint);

                button = findViewById(R.id.startButton);
                button.setOnClickListener(v -> {
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .build();
                    NavigationLauncher.startNavigation(NavigationActivity.this, options);
                });

                button.setEnabled(true);
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

    private void getRoute(Point origin, Point destination) {

        String profile = getIntent().getStringExtra("journey_profile").toLowerCase();

        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .profile(profile)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DirectionsResponse> call, @NotNull Response<DirectionsResponse> response) {

                        if (response.body() == null) {
                            return;
                        } else if (response.body().routes().size() < 1) {
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(@NotNull Call<DirectionsResponse> call, @NotNull Throwable throwable) {
                        System.out.println(TAG + ", Error: " + throwable.getMessage());
                    }
                });
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
    protected void onSaveInstanceState(@NotNull Bundle outState) {
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

}