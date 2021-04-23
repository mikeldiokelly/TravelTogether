package com.example.traveltogether;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationSearch extends AppCompatActivity {

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

        System.out.println("In LocationSearch ................. ");
        searchForLocation(search_keyword, latitude, longitude);

        listView = findViewById(R.id.search_location_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Location location = locationList[position];
                System.out.println(location);
                Intent _result = new Intent();
//                    _result.setData(Uri.parse(endPoint.toString()));
                _result.putExtra("location_name",location.locationName);
                _result.putExtra("location_point",(location.locationPoint).toString());
                setResult(Activity.RESULT_OK, _result);
                finish();
            }
        });
    }

    private void searchForLocation(String location, double latitude, double longitude){


        Log.d(" currentLocationIs ", "result: " + latitude + " : " + longitude);

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(location)
                .proximity(Point.fromLngLat(longitude, latitude))
                .build();                                          //add co-ordinates here once you have current location !!!!!
        //

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            private static final String TAG = "SEARCH_LOCATION";

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                int resultSize = results.size();

                if (results.size() > 0) {

                    // Log the first results Point.
                    Point firstResultPoint = results.get(0).center();
                    System.out.println(results);

                    locationNames = new String[resultSize];
                    locationList = new Location[resultSize];
                    int index = 0;
                    for(CarmenFeature r : results){

                        Log.d(TAG, "result: " + r);
                        Log.d(TAG, "matchingPlaceName: " + r.matchingPlaceName());
                        Log.d(TAG, "id " + r.id());
                        locationList[index] = new Location(r.placeName(), r.center());
                        locationNames[index] = r.placeName();
                        index ++;
                    }

                    listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, locationNames));


                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });
    }

    static class Location{
        String locationName;
        Point locationPoint;

        Location(String locationName, Point locationPoint){
            this.locationName = locationName;
            this.locationPoint = locationPoint;
        }
    }
}