package com.example.traveltogether;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearchActivity extends AppCompatActivity {

    ListView listView;
    Location[] locationList;
    String[] locationNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        String search_keyword = getIntent().getStringExtra("location_to_search");
        double latitude = getIntent().getDoubleExtra("current_location_lat", 0);
        double longitude = getIntent().getDoubleExtra("current_location_long", 0);

        searchForLocation(search_keyword, latitude, longitude);

        listView = findViewById(R.id.search_location_list);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            final Location location = locationList[position];
            System.out.println(location);
            Intent _result = new Intent();
            _result.putExtra("location_name", location.locationName);
            _result.putExtra("location_point", (location.locationPoint).toString());
            setResult(Activity.RESULT_OK, _result);
            finish();
        });
    }

    private void searchForLocation(String location, double latitude, double longitude) {

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(location)
                .proximity(Point.fromLngLat(longitude, latitude))
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call<GeocodingResponse> call, @NotNull Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                int resultSize = results.size();

                if (results.size() > 0) {

                    locationNames = new String[resultSize];
                    locationList = new Location[resultSize];
                    int index = 0;
                    for (CarmenFeature r : results) {

                        locationList[index] = new Location(r.placeName(), r.center());
                        locationNames[index] = r.placeName();
                        index++;
                    }

                    listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, locationNames));


                } else {

                    // No result for your request were found.
                    System.out.println(" LocationSearchActivity, onResponse: No result found");

                }
            }

            @Override
            public void onFailure(@NotNull Call<GeocodingResponse> call, @NotNull Throwable t) {

            }
        });
    }

    static class Location {
        String locationName;
        Point locationPoint;

        Location(String locationName, Point locationPoint) {
            this.locationName = locationName;
            this.locationPoint = locationPoint;
        }
    }
}