package com.ride.driverapp;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.ride.driverapp.Entities.RideContract;
import com.ride.driverapp.Repository.RidesRepository;
import com.ride.driverapp.Services.Api.ApiServiceGenerator;
import com.ride.driverapp.Services.Api.IApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidesViewModel extends AndroidViewModel {

    private static final String TAG = RegViewModel.class.getSimpleName();
    private RidesRepository ridesRepository;
    private IApiService apiService;

    private MutableLiveData<RideContract> adapterItem = new MutableLiveData<>();

    private MutableLiveData<String> duration = new MutableLiveData<>();
    private MutableLiveData<String> distance = new MutableLiveData<>();


    public RidesViewModel(@NonNull Application application) {
        super(application);
        ridesRepository = new RidesRepository(application);
        apiService = ApiServiceGenerator.createService(IApiService.class, application);
    }


    public MutableLiveData<RideContract> getAdapterItem() {
        return adapterItem;
    }

    public void setAdapterItem(RideContract adapterItem) {
        Log.w(TAG, "clicked: " + adapterItem);
        this.getAdapterItem().setValue(adapterItem);

        String distanceVal = TextUtils.concat(String.valueOf( Math.round ( adapterItem.getDistance() / 1000) ), " km").toString();
        String durationVal;
        if(adapterItem.getDistance() > 3600){
            durationVal = TextUtils.concat(String.valueOf( Math.round( adapterItem.getDistance() / 3600) ), " h").toString();
        } else {
            durationVal = TextUtils.concat(String.valueOf( Math.round( adapterItem.getDistance() )  ), " min").toString();
        }


        this.getDistance().setValue( distanceVal );
        this.getDuration().setValue( durationVal );
    }


    public MutableLiveData<String> getDuration() {
        return duration;
    }

    public void setDuration(MutableLiveData<String> duration) {
        this.duration = duration;
    }

    public MutableLiveData<String> getDistance() {
        return distance;
    }

    public void setDistance(MutableLiveData<String> distance) {
        this.distance = distance;
    }

    public MutableLiveData<Integer> getRegStatus() {
        return ridesRepository.getRegStatus();
    }

    public ArrayList<String> getStatusArray() {
        return ridesRepository.getStatusArray();
    }
    public void setRegStatus(int status){
        ridesRepository.getRegStatus().postValue(status);
    }





    public MutableLiveData<ArrayList<RideContract>> getRides(){
        return ridesRepository.getRides();
    }

    public void updateStatus(JsonObject newStatus){
        Call<JsonObject> call = apiService.updateStatus(newStatus);
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("response", response.toString());

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.w("responserror", t);
            }
        });
    }




}
