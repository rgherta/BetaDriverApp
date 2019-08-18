package com.ride.driverapp.services.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ride.driverapp.model.AcceptanceContract;
import com.ride.driverapp.model.DriverContract;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface IApiService {

    @Headers("Content-Type: application/json")
    @POST("users/add")
    Call<DriverContract> addUser(@Body DriverContract driver);

    @Headers("Content-Type: application/json")
    @GET("users/driver")
    Call<DriverContract> getDriver(@Query("uid") String uid);


    @Headers("Content-Type: application/json")
    @POST("users/drivers/status")
    Call<JsonObject>  updateStatus(@Body JsonObject status);


    @Headers("Content-Type: application/json")
    @GET("rides")
    Call<JsonArray> getRides();

    @Headers("Content-Type: application/json")
    @POST("rides/driver/accept")
    Call<AcceptanceContract> acceptRide(@Body AcceptanceContract contract);




}
