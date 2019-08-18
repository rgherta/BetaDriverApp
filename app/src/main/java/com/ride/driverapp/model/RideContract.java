package com.ride.driverapp.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class RideContract {

    @SerializedName("uid")
    private String uid;

    @SerializedName("customer")
    private String customer;

    @SerializedName("customer_name")
    private String customer_name;

    @SerializedName("pickup")
    private HashMap<String, Double> pickup;

    @SerializedName("pickup_str")
    private String pickupStr;

    @SerializedName("destination")
    private HashMap<String, Double> destination;

    @SerializedName("points")
    private String points;

    @SerializedName("distance")
    private int distance;

    @SerializedName("duration")
    private int duration;

    @SerializedName("destination_str")
    private String destinationStr;

    @SerializedName("payment")
    private int payment;

    @SerializedName("status")
    private int status;

    @SerializedName("conversation")
    private String conversation = "";

    @SerializedName("driver")
    private String driver;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDestinationStr() {
        return destinationStr;
    }

    public void setDestinationStr(String destinationStr) {
        this.destinationStr = destinationStr;
    }

    public String getPickupStr() {
        return pickupStr;
    }

    public void setPickupStr(String pickupStr) {
        this.pickupStr = pickupStr;
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public HashMap<String, Double> getPickup() {
        return pickup;
    }

    public void setPickup(HashMap<String, Double> pickup) {
        this.pickup = pickup;
    }

    public HashMap<String, Double> getDestination() {
        return destination;
    }

    public void setDestination(HashMap<String, Double> destination) {
        this.destination = destination;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String toString(){
        return (String) TextUtils.concat(this.getCustomer()
                , this.getCustomer_name(), this.getPickup().toString()
                , this.getDestination().toString()
                , this.getPoints()
                , String.valueOf( this.getDistance() )
                , String.valueOf( this.getDuration() )
                , String.valueOf(this.getPayment()), String.valueOf(this.getStatus())
                , this.getDriver()
                , this.getDestinationStr()
                , this.getPickupStr()
        );

    }
}
