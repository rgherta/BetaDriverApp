package com.ride.driverapp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.util.StringUtil;

import com.google.gson.JsonObject;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.services.repository.RidesRepository;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;
import com.ride.driverapp.ui.RideListActivity;

import java.sql.Driver;
import java.util.ArrayList;

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
    private MutableLiveData<String> estimation = new MutableLiveData<>();
    private LiveData<DriverContract> accountData = new MutableLiveData<>();

    private MutableLiveData<Boolean> fabVisibility = new MutableLiveData<>();
    private MutableLiveData<Boolean> listVisibility = new MutableLiveData<>();


    public RidesViewModel(@NonNull Application application) {
        super(application);
        ridesRepository = new RidesRepository(application);
        apiService = ApiServiceGenerator.createService(IApiService.class, application);
        this.fabVisibility.postValue(false);
        this.listVisibility.postValue(true);
    }


    public MutableLiveData<RideContract> getAdapterItem() {
        return adapterItem;
    }

    public void setAdapterItem(RideContract adapterItem) {
        Log.w(TAG, "clicked: " + adapterItem);
        this.adapterItem.setValue(adapterItem);

        String distanceVal = TextUtils.concat(String.valueOf( Math.round ( adapterItem.getDistance() / 1000) ), " km").toString();
        String durationVal;
        if(adapterItem.getDuration() >= 3600){
            durationVal = TextUtils.concat(String.valueOf( Math.round( adapterItem.getDuration() / 3600) ), " h").toString();
        } else if(adapterItem.getDuration() >= 60) {
            durationVal = TextUtils.concat(String.valueOf( Math.round( adapterItem.getDuration() / 60) ), " min").toString();
        } else {
            durationVal = TextUtils.concat(String.valueOf( Math.round( adapterItem.getDuration() ) ), " sec").toString();
        }


        this.getDistance().setValue( distanceVal );
        this.getDuration().setValue( durationVal );

        String accountCurrency = this.accountData.getValue().getCurr();
        Double accountPpk = this.accountData.getValue().getPpk();
        String estimatedPrice = (String) TextUtils.concat( String.valueOf(  Math.round( ( adapterItem.getDistance() / 1000) * accountPpk) ) , " ", accountCurrency);
        this.getEstimation().setValue( estimatedPrice );

        this.getFabVisibility().postValue(true);
    }


    public MutableLiveData<String> getDuration() {
        return duration;
    }

    public MutableLiveData<String> getEstimation() {
        return estimation;
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

    public LiveData<DriverContract> getAccountData(){
        accountData = ridesRepository.getAccountData();
        return accountData;
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


    public MutableLiveData<Boolean> getFabVisibility() {
        return fabVisibility;
    }

    public void setFabVisibility(MutableLiveData<Boolean> fabVisibility) {
        this.fabVisibility = fabVisibility;
    }

    public MutableLiveData<Boolean> getListVisibility() { return listVisibility; }

    public void setListVisibility(MutableLiveData<Boolean> listVisibility) {
        this.listVisibility = listVisibility;
    }

}
