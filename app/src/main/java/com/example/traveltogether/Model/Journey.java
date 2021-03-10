package com.example.traveltogether.Model;
import android.os.Build;

import com.mapbox.geojson.Point;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Journey {
    String Id;
    List<User> Users;
    Point Source, Destination;
    LocalDate time;
    Journey(String Id, List<User> Users, Point Source, Point Destination, LocalDate time){
        this.Id = Id;
        this.Destination = Destination;
        this.Source = Source;
        this.Users = Users;
        this.time = time;
    }
    public void addUser(User user){ this.Users.add(user); }
    public void setId(String Id){ this.Id = Id; }
    public void setUserList(List<User> Users){ this.Users = Users; }
    public void setSource(Point Source){ this.Source = Source; }
    public void setDestination(Point Destination){ this.Destination = Destination; }

    public String getId(){ return Id; }
    public Point getSource(){ return Source; }
    public Point getDestination(){ return Destination; }
    public List<User> getUserList(){ return Users; }
}