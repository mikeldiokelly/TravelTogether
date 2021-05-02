package com.example.traveltogether.Model;

import com.example.traveltogether.R;
import com.example.traveltogether.*;
import com.google.android.gms.common.internal.Objects;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.geojson.Point;

import java.util.Collections;
import java.util.List;

public class User {

    public String firstName, lastName, age, email, id, gender;
    //private String id;
    Point perm_res, currLoc;
    Double avgRating;
    int numRating;
    List<Journey> journeys;

    public User() {
    }

    public User(String first_name, String last_name, String age, String email, String id, String gender, com.mapbox.geojson.Point perm_res, com.mapbox.geojson.Point curr_loc) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.gender = gender;
        this.perm_res = perm_res;
        this.currLoc = curr_loc;
        this.numRating = 0;
        this.avgRating = (Double) 0.0;
        this.journeys = Collections.emptyList();
    }

    public String getUsername(){
        return this.firstName;
    }
    public void setUsername(String name){ this.firstName = name;}

    public void addJourney(Journey journey){
        journeys.add(journey);
    };

    public List<Journey> getJourneys(){
        return journeys;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return "default";
    }
    public Double getAvgRating(){return avgRating;}

    public Double updateRating(int newRating){
        avgRating = (avgRating*numRating + newRating)/(numRating+1);
        numRating +=1;
        return avgRating;
    }
}
