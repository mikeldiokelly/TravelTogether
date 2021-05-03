package com.example.traveltogether.Model;

import com.mapbox.geojson.Point;

import java.util.Collections;
import java.util.List;

public class User {

    public String firstName, lastName, age, email, id, gender;
    double avgRating;
    int numRating;

    public User() {
    }

    public User(String first_name, String last_name, String age, String email, String id, String gender) {
        this.firstName = first_name;
        this.lastName = last_name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.gender = gender;
//        this.currLoc = curr_loc;

        this.numRating = 0;
        this.avgRating = 0;

    }

    public String getUsername() {
        return this.firstName;
    }

    public void setUsername(String name) {
        this.firstName = name;
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

    // getters required by firebase
    public int getNumRating() {
        return numRating;
    }

    public double getAvgRating() {
        return avgRating;
    }
}
