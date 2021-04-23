package com.example.traveltogether.Model;
import android.os.Build;

import com.mapbox.geojson.Point;

import java.time.LocalDate;
import java.util.List;

import androidx.annotation.RequiresApi;

//@RequiresApi(api = Build.VERSION_CODES.O)
public class Journey {
    String Id;
    List<String> Users;
    List<Double> Source, Destination;
    String startTime;
    String transport;
    boolean repeatWeakly;

    public Journey() {
    }
    public Journey(String Id, List<String> Users, List<Double> Source, List<Double> Destination, String time, boolean repeatWeakly, String transport){

        this.Id = Id;
        this.Destination = Destination;
        this.Source = Source;
        this.Users = Users;
        this.startTime = time;
        this.repeatWeakly = repeatWeakly;
        this.transport = transport;
    }

    public void addUser(String user){ this.Users.add(user); }
    public void setId(String Id){ this.Id = Id; }
    public void setUserList(List<String> Users){ this.Users = Users; }
    public void setSource(List<Double> Source){ this.Source = Source; }
    public void setDestination(List<Double> Destination){ this.Destination = Destination; }

    public String getId(){ return Id; }
    public List<Double> getSource(){ return Source; }
    public List<Double> getDestination(){ return Destination; }
    public List<String> getUserList(){ return Users; }
    public String getHost() {
        return Users.get(0);
    }
    public String getStartTime(){return startTime;}
    public boolean getRepeatWeekly(){return repeatWeakly;}

}