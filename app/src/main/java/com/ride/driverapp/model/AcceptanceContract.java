package com.ride.driverapp.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AcceptanceContract {

    @SerializedName("ride")
    private String ride;

    @SerializedName("driver")
    private String driver;

    @SerializedName("arrival")
    private String estimatedArrival;

    @SerializedName("price")
    private String estimatedPrice;

    public AcceptanceContract(String ride, String driver, String arrival, String price ){
        this.ride = ride;
        this.driver = driver;
        this.estimatedArrival = arrival;
        this.estimatedPrice = price;
    }


    @Override
    public String toString() {
        return (String) TextUtils.concat(
                this.ride, this.driver, this.estimatedArrival, this.estimatedPrice
        );
    }
}
