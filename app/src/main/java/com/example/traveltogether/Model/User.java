package com.example.traveltogether.Model;

import com.example.traveltogether.R;
import com.example.traveltogether.*;
import com.google.android.gms.common.internal.Objects;
import com.mapbox.geojson.Point;

public class User {

    public String first_name, last_name, age, email, id, gender;
    //private String id;
    Point perm_res, curr_loc;

    public User() {
    }

    public User(String first_name, String last_name, String age, String email, String id, String gender, com.mapbox.geojson.Point perm_res, com.mapbox.geojson.Point curr_loc) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.email = email;
        this.id = id;
        this.gender = gender;
        this.perm_res = perm_res;
        this.curr_loc = curr_loc;
    }

    public String getUsername(){
        return this.first_name;
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
}
