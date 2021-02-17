package com.example.traveltogether.Model;
import com.example.traveltogether.R;
import com.example.traveltogether.*;
import com.google.android.gms.common.internal.Objects;

public class User {

    public String first_name, last_name, age, email, id;
    //private String id;


    public User() {
    }

    public User(String first_name, String last_name, String age, String email, String id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.age = age;
        this.email = email;
        this.id = id;
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
