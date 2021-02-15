package com.example.traveltogether;

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

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

}
