package com.ride.driverapp.Services.Api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ride.driverapp.Entities.DriverContract;
import com.ride.driverapp.Entities.RideContract;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface IApiService {

    @Headers("Content-Type: application/json")
    @POST("users/add")
    Call<DriverContract> addUser(@Body DriverContract driver);


    @Headers("Content-Type: application/json")
    @POST("users/drivers/status")
    Call<JsonObject>  updateStatus(@Body JsonObject status);


    @Headers("Content-Type: application/json")
    @GET("rides")
    Call<JsonArray> getRides();


}
