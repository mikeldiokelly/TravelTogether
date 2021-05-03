package com.example.traveltogether.Model;

import java.util.List;

public class Journey {
    public enum JourneyStatus {
        PENDING,
        ONGOING,
        FINISHED
    }

    String Id;
    List<String> Users;
    List<Double> Source, Destination;
    String sourceAddress, destAddress;
    String startTime;
    String transport;
    boolean repeatWeakly;
    JourneyStatus journeyStatus;

    public Journey() {

    }

    public Journey(String Id, List<String> Users, List<Double> Source, List<Double> Destination, String time, boolean repeatWeakly, String transport, String sourceAddress, String destAddress) {

        this.Id = Id;
        this.Destination = Destination;
        this.Source = Source;
        this.Users = Users;
        this.startTime = time;
        this.repeatWeakly = repeatWeakly;
        this.transport = transport;
        this.journeyStatus = JourneyStatus.PENDING;
        this.sourceAddress = sourceAddress;
        this.destAddress = destAddress;
    }

    public void addUser(String user) {
        this.Users.add(user);
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public List<Double> getSource() {
        return Source;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public List<Double> getDestination() {
        return Destination;
    }

    public List<String> getUserList() {
        return Users;
    }

    public String getHost() {
        return Users.get(0);
    }

    public String getStartTime() {
        return startTime;
    }

    public JourneyStatus getJourneyStatus() {
        return journeyStatus;
    }

    public void setUserList(List<String> Users){ this.Users = Users; }

    public void setSource(List<Double> Source){ this.Source = Source; }

    public void setDestination(List<Double> Destination){ this.Destination = Destination; }
}