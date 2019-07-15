package com.ride.driverapp.services.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ride.driverapp.db.DriverAppDatabase;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidesRepository {

    private IApiService apiService;
    private MutableLiveData<Integer> regStatus = new MutableLiveData<Integer>();
    private MutableLiveData<ArrayList<RideContract>> rides = new MutableLiveData<>();
    private final WeakReference<Application> application;


    public RidesRepository(Application application){
       this.application = new WeakReference<Application>(application);
    }





    private ArrayList<String> statusArray = new ArrayList<String>(){
        {
            add( "Available");
            add( "Busy");
            add( "Offline" );

        }
    };


    public MutableLiveData<Integer> getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(MutableLiveData<Integer> regStatus) {

        this.regStatus = regStatus;
    }


    public ArrayList<String> getStatusArray() {
        return statusArray;
    }



    public MutableLiveData<ArrayList<RideContract>> getRides(){
        apiService = ApiServiceGenerator.createService(IApiService.class, this.application.get());
        Call<JsonArray> call = apiService.getRides();
        call.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                ArrayList<RideContract> ridesArray = new Gson().fromJson(response.body(), new TypeToken<List<RideContract>>(){}.getType());
                rides.postValue(ridesArray);


            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.w("responserror", t);
            }
        });

        return rides;
    }


    public LiveData<DriverContract> getAccountData(){

        DriverAppDatabase db = DriverAppDatabase.getDatabase(application.get());
        return db.driverDao().getDriver();



    }


}
