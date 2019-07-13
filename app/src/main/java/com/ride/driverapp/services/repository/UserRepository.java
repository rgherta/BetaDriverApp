package com.ride.driverapp.services.repository;

import androidx.lifecycle.MutableLiveData;

public class UserRepository {

    //singleton code
    private static UserRepository instance;
    private UserRepository(){}
    public static synchronized UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
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