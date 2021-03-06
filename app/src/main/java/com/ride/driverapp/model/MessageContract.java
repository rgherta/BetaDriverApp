package com.ride.driverapp.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MessageContract {

    @SerializedName("message")
    private String message;

    @SerializedName("sender")
    private String sender;

    @SerializedName("receiver")
    private String receiver;

    @SerializedName("timestamp")
    private Date timestamp;

    @SerializedName("ride")
    private String ride;




    public String mType = "outgoing";

    public String getRide() {
        return ride;
    }

    public void setRide(String ride) {
        this.ride = ride;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return  " message: " + this.getMessage() +
                " sender: " + this.getSender() +
                " receiver: " + this.getReceiver() +
                " timestamp: " + this.getTimestamp() +
                " ride: " + this.getRide();
    }

    public MessageContract(String message, String sender, String receiver, Date stamp, String ride){
        this.setMessage(message);
        this.setSender(sender);
        this.setReceiver(receiver);
        this.setTimestamp(stamp);
        this.setRide(ride);

    }
}