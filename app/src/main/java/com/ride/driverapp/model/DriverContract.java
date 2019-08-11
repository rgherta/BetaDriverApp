package com.ride.driverapp.model;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.util.StringUtil;

import com.google.gson.annotations.SerializedName;
import com.ride.driverapp.services.GeohashUtils;

import java.util.HashMap;

@Entity(tableName = "driver_table")
public class DriverContract {

    @PrimaryKey
    @NonNull
    @SerializedName("uid")
    private String uid;

    @SerializedName("fcm_token")
    private String fcmToken;

    @SerializedName("name")
    private String fullName;

    @SerializedName("category")
    private int category;

    @SerializedName("email")
    private String email;

    @SerializedName("vehicle")
    private String carModel;

    @SerializedName("plate")
    private String regPlate;

    @SerializedName("phone")
    private String phone;

    @SerializedName("city")
    private String city;

    @SerializedName("ppk")
    private Double ppk;

    @SerializedName("cur")
    private String curr;

    @Embedded(prefix = "pay_")
    @SerializedName("payment")
    private HashMap<String, Boolean> payment;

    @SerializedName("rating")
    private int rating;

    @SerializedName("rides")
    private int rides;

    @SerializedName("status")
    private int status;

    @Embedded(prefix = "loc_")
    @SerializedName("loc")
    private HashMap<String,Double> loc;

    @SerializedName("hashloc")
    private String hashLocation;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getRegPlate() {
        return regPlate;
    }

    public void setRegPlate(String regPlate) {
        this.regPlate = regPlate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Boolean> getPayment() {
        return payment;
    }

    public void setPayment(HashMap<String, Boolean> payment) {
        this.payment = payment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getPpk() {
        return ppk;
    }

    public void setPpk(Double ppk) {
        this.ppk = ppk;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRides() {
        return rides;
    }

    public void setRides(int rides) {
        this.rides = rides;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, Double> getLoc() {
        return loc;
    }

    public void setLoc(HashMap<String, Double> loc) {
        this.loc = loc;
    }


    public String getHashLocation() {
        return hashLocation;
    }

    public void setHashLocation(String hashLocation) {
        this.hashLocation = hashLocation;
    }

    public DriverContract(String uid, String fcmToken, String fullName, int category, String email, String carModel, String regPlate, String phone
            , HashMap<String, Boolean> payment, String city, Double ppk, String curr, int rating, int rides, int status, HashMap<String, Double> loc ) {
        this.uid = uid;
        this.fcmToken = fcmToken;
        this.fullName = fullName;
        this.category = category;
        this.email = email;
        this.carModel = carModel;
        this.regPlate = regPlate;
        this.phone = phone;
        this.payment = payment;
        this.city = city;
        this.ppk = ppk;
        this.curr = curr;
        this.rating = rating;
        this.rides = rides;
        this.status = status;
        this.loc = loc;
        this.hashLocation="";
       // this.hashLocation = GeohashUtils.encode(loc.get("lat"), loc.get("long"));
    }

    public String toString(){
        return (String) TextUtils.concat(
                this.uid
                , this.fcmToken
                , this.fullName
                , String.valueOf(this.category)
                , this.email
                , this.carModel
                , this.regPlate
                , this.phone
                , this.city
                , String.valueOf(this.ppk)
                , this.curr
                , String.valueOf(this.rating)
                , String.valueOf(this.rides)
                , String.valueOf(this.status)

        );
    }




}
