package com.ride.driverapp.services.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.ride.driverapp.db.DriverAppDatabase;
import com.ride.driverapp.db.IDriverDao;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.services.GeohashUtils;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;
import com.ride.driverapp.services.tasks.InsertDriver;
import com.ride.driverapp.ui.registration.RegistrationExtraActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormRepository {

    //singleton code
    private static FormRepository instance;

    private FormRepository(){

    }

    public static synchronized FormRepository getInstance(){
        if(instance == null){
            instance = new FormRepository();
        }
        return instance;
    }

    //data

    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> regPassword2 =  new MutableLiveData<>();
    private MutableLiveData<String> regFullname = new MutableLiveData<>();
    private MutableLiveData<String> regPhone = new MutableLiveData<>();
    private MutableLiveData<String> regCity = new MutableLiveData<>();
    private MutableLiveData<String> regVehicle = new MutableLiveData<>();
    private MutableLiveData<String> regPlate = new MutableLiveData<>();
    private MutableLiveData<String> regPpk = new MutableLiveData<>();
    private MutableLiveData<String> regCurr = new MutableLiveData<>();
    private MutableLiveData<Boolean> regPayCash = new MutableLiveData<>();
    private MutableLiveData<Boolean> regPayPal = new MutableLiveData<>();



    public void sendDriverData(DriverContract driver, Context ctx){
        driver.setHashLocation( GeohashUtils.encode(driver.getLoc().get("lat"), driver.getLoc().get("long")) );

        IApiService apiService = ApiServiceGenerator.createService(IApiService.class, ctx);
        Call<DriverContract> call = apiService.addUser(driver);
        call.enqueue(new Callback<DriverContract>() {

            @Override
            public void onResponse(Call<DriverContract> call, Response<DriverContract> response) {
                Log.w("response", response.toString());
                DriverAppDatabase db = DriverAppDatabase.getDatabase(ctx);
                new InsertDriver(db.driverDao()).execute(driver);


            }

            @Override
            public void onFailure(Call<DriverContract> call, Throwable t) {
                Log.w("responserror", t);
            }
        });

    }


    public void getDriverData(String uid, String fcm, Context ctx){

        IApiService apiService = ApiServiceGenerator.createService(IApiService.class, ctx);
        Call<DriverContract> call = apiService.getDriver(uid);
        call.enqueue(new Callback<DriverContract>() {

            @Override
            public void onResponse(Call<DriverContract> call, Response<DriverContract> response) {
                Log.w("response", response.toString());
                DriverContract responseData = (DriverContract) response.body();
                DriverAppDatabase db = DriverAppDatabase.getDatabase(ctx);
                if(response.isSuccessful()) {
                    if(!responseData.getFcmToken().equals(fcm)) updateFcm(uid, fcm, ctx);
                    new InsertDriver(db.driverDao()).execute(responseData);
                }

            }

            @Override
            public void onFailure(Call<DriverContract> call, Throwable t) {
                Log.w("responserror", t);
            }
        });

    }



    public void updateFcm(String uid, String fcm, Context ctx){

        IApiService apiService = ApiServiceGenerator.createService(IApiService.class, ctx);
        JsonObject payload = new JsonObject();
        payload.addProperty("uid",uid);
        payload.addProperty("fcm", fcm);
        Call<JsonObject> call = apiService.updateFcm(payload);
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




    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(MutableLiveData<String> email) {
        this.email = email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(MutableLiveData<String> password) {
        this.password = password;
    }

    public MutableLiveData<String> getRegPassword2() {
        return regPassword2;
    }

    public void setRegPassword2(MutableLiveData<String> regPassword2) {
        this.regPassword2 = regPassword2;
    }

    public MutableLiveData<String> getRegFullname() {
        return regFullname;
    }

    public void setRegFullname(MutableLiveData<String> regFullname) {
        this.regFullname = regFullname;
    }

    public MutableLiveData<String> getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(MutableLiveData<String> regPhone) {
        this.regPhone = regPhone;
    }

    public MutableLiveData<String> getRegCity() {
        return regCity;
    }

    public void setRegCity(MutableLiveData<String> regCity) {
        this.regCity = regCity;
    }

    public MutableLiveData<String> getRegVehicle() {
        return regVehicle;
    }

    public void setRegVehicle(MutableLiveData<String> regVehicle) {
        this.regVehicle = regVehicle;
    }

    public MutableLiveData<String> getRegPlate() {
        return regPlate;
    }

    public void setRegPlate(MutableLiveData<String> regPlate) {
        this.regPlate = regPlate;
    }

    public MutableLiveData<String> getRegPpk() {
        return regPpk;
    }

    public void setRegPpk(MutableLiveData<String> regPpk) {
        this.regPpk = regPpk;
    }

    public MutableLiveData<String> getRegCurr() {
        return regCurr;
    }

    public void setRegCurr(MutableLiveData<String> regCurr) {
        this.regCurr = regCurr;
    }

    public MutableLiveData<Boolean> getRegPayCash() {
        return regPayCash;
    }

    public void setRegPayCash(MutableLiveData<Boolean> regPayCash) {
        this.regPayCash = regPayCash;
    }

    public MutableLiveData<Boolean> getRegPayPal() {
        return regPayPal;
    }

    public void setRegPayPal(MutableLiveData<Boolean> regPayPal) {
        this.regPayPal = regPayPal;
    }



}