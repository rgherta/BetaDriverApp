package com.ride.driverapp.viewmodel;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ride.driverapp.R;
import com.ride.driverapp.services.repository.FormRepository;

import java.util.regex.Pattern;

public class RegViewModel extends ViewModel {

    private static final String TAG = RegViewModel.class.getSimpleName();


    public MutableLiveData<String> getRegEmail() { return FormRepository.getInstance().getEmail(); }

    public void setRegEmail(MutableLiveData<String> regEmail) {
        FormRepository.getInstance().setEmail(regEmail);
    }

    public MutableLiveData<String> getRegPassword() {
        return FormRepository.getInstance().getPassword();
    }

    public void setRegPassword(MutableLiveData<String> regPassword) {
        FormRepository.getInstance().setPassword(regPassword);
    }

    public MutableLiveData<String> getRegPassword2() {
        return FormRepository.getInstance().getRegPassword2();
    }

    public void setRegPassword2(MutableLiveData<String> regPassword2) {
        FormRepository.getInstance().setRegPassword2(regPassword2);
    }

    public MutableLiveData<String> getRegFullname() {
        return FormRepository.getInstance().getRegFullname();
    }

    public void setRegFullname(MutableLiveData<String> regFullname) {
        FormRepository.getInstance().setRegFullname(regFullname);
    }

    public MutableLiveData<String> getRegPhone() {
        return FormRepository.getInstance().getRegPhone();
    }

    public void setRegPhone(MutableLiveData<String> regPhone) {
        FormRepository.getInstance().setRegPhone(regPhone);
    }


    public MutableLiveData<String> getRegCity() {
        return FormRepository.getInstance().getRegCity();
    }

    public void setRegCity(MutableLiveData<String> regCity) {
        FormRepository.getInstance().setRegCity(regCity);
    }


    public MutableLiveData<String> getRegVehicle() {
        return FormRepository.getInstance().getRegVehicle();
    }

    public void setRegVehicle(MutableLiveData<String> regVehicle) {
        FormRepository.getInstance().setRegVehicle(regVehicle);
    }

    public MutableLiveData<String> getRegPlate() {
        return FormRepository.getInstance().getRegPlate();
    }

    public void setRegPlate(MutableLiveData<String> regPlate) {
        FormRepository.getInstance().setRegPlate(regPlate);
    }

    public MutableLiveData<String> getRegPpk() {
        return FormRepository.getInstance().getRegPpk();
    }

    public void setRegPpk(MutableLiveData<String> regPpk) {
        FormRepository.getInstance().setRegPpk(regPpk);
    }

    public MutableLiveData<String> getRegCurr() {
        return FormRepository.getInstance().getRegCurr();
    }

    public void setRegCurr(MutableLiveData<String> regCurr) {
        FormRepository.getInstance().setRegCurr(regCurr);
    }

    public MutableLiveData<Boolean> getRegPayCash() {
        return FormRepository.getInstance().getRegPayCash();
    }

    public void setRegPayCash(MutableLiveData<Boolean> regPayCash) {
        FormRepository.getInstance().setRegPayCash(regPayCash);
    }

    public MutableLiveData<Boolean> getRegPayPal() {
        return FormRepository.getInstance().getRegPayPal();
    }

    public void setRegPayPal(MutableLiveData<Boolean> regPayPal) {
        FormRepository.getInstance().setRegPayPal(regPayPal);
    }

    //custom validation
    @BindingAdapter("android:validation")
    public static void setValidation(TextInputLayout inputLayout, String errorCase) {
        FrameLayout flayout = (FrameLayout) inputLayout.getChildAt(0); //material design is rendering TextInputEditText inside a framelayout
        TextInputEditText presValue = (TextInputEditText) flayout.getChildAt(0);

        presValue.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                switch (errorCase){
                    case "email":
                        if( !isValidEmail( presValue.getText().toString() ) ) {
                            inputLayout.setError("Enter valid email");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "full_name":
                        if( !isValidFullName( presValue.getText().toString() ) ) {
                            inputLayout.setError("Min 5 char");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "phone":
                        if( !isValidPhone( presValue.getText().toString() ) ) {
                            inputLayout.setError("Not a valid phone");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "password":
                        if( !isValidPassword( presValue.getText().toString() ) ) {
                            inputLayout.setError("Min 6 chars alphanumeric");
                        } else {
                            inputLayout.setError(null);
                        };
                        break;

                    case "password2":
                        View parent = (View) inputLayout.getParent();
                        TextInputEditText pwd = parent.findViewById(R.id.txt_password_value);
                        Log.w(TAG, String.valueOf(pwd.getText()));

                        if( !TextUtils.equals(presValue.getText(), pwd.getText() ) ) {
                            inputLayout.setError("Passwords do not match");
                        } else {
                            inputLayout.setError(null);
                        };
                        break;

                    case "city":
                        if( !isValidCity( presValue.getText().toString() ) ) {
                            inputLayout.setError("Required");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "vehicle":
                        if( !isValidVehicle( presValue.getText().toString() ) ) {
                            inputLayout.setError("Required");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "plate":
                        if( !isValidPlate( presValue.getText().toString() ) ) {
                            inputLayout.setError("Required");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                            inputLayout.setEndIconDrawable(R.drawable.ic_check_black_24dp);
                        };
                        break;

                    case "ppk":
                        if( !isValidPpk( presValue.getText().toString() ) ) {
                            inputLayout.setError("Required");
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        } else {
                            inputLayout.setError(null);
                            inputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                        };
                        break;


                }
            }
        });

    }

    //validation static helper methods
    public static boolean isValidEmail(String email){
        return !TextUtils.isEmpty( email ) && Patterns.EMAIL_ADDRESS.matcher( email ).matches() ;
    }

    public static boolean isValidFullName(String fullName){
        return !TextUtils.isEmpty( fullName ) && fullName.length() >= 5 ;
    }

    public static boolean isValidPhone(String phoneNumber){
        return  !TextUtils.isEmpty( phoneNumber ) && Patterns.PHONE.matcher( phoneNumber ).matches();
    }

    public static boolean isValidPassword(String password){
        return Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{6,50}$").matcher( password ).matches() ;
    }

    public static boolean isValidCity(String city){
        return !TextUtils.isEmpty(city);
    }

    public static boolean isValidVehicle(String vehicle){
        return !TextUtils.isEmpty(vehicle);
    }

    public static boolean isValidPlate(String plate){
        return !TextUtils.isEmpty(plate);
    }

    public static boolean isValidPpk(String ppk){
        return !TextUtils.isEmpty(ppk.toString());
    }

    public static boolean isValidCurr(String curr){
        return !TextUtils.isEmpty(curr);
    }






    public boolean hasValidEmail(){
        return isValidEmail( this.getRegEmail().getValue() );
    }

    public boolean hasValidPassword(){
        return isValidPassword( this.getRegPassword().getValue() ) && TextUtils.equals(this.getRegPassword().getValue(), this.getRegPassword2().getValue());
    }

    public boolean hasValidFullName(){
        return isValidFullName( this.getRegFullname().getValue() );
    }

    public boolean hasValidPhone(){
        return isValidPhone( this.getRegPhone().getValue() );
    }

    public boolean hasValidCity() {
        return isValidCity( this.getRegCity().getValue() ) ;
    }

    public boolean hasValidVehicle() {
        return isValidCity( this.getRegVehicle().getValue() ) ;
    }

    public boolean hasValidPlate() {
        return isValidCity( this.getRegPlate().getValue() ) ;
    }

    public boolean hasValidPpk() {
        return isValidPpk( this.getRegPpk().getValue() ) ;
    }

    public boolean hasValidCurr() {
        return isValidCurr( this.getRegCurr().getValue() ) ;
    }




    //TODO: finish up
    public void clearData(){
        this.setRegEmail(null);
        this.setRegPassword(null);
        this.setRegPassword2(null);
        this.setRegFullname(null);
        this.setRegPhone(null);
        this.setRegCity(null);
        this.setRegVehicle(null);
        this.setRegPlate(null);
        this.setRegPpk(null);
        this.setRegCurr(null);
        this.setRegPayCash(null);
        this.setRegPayPal(null);
    }



}
