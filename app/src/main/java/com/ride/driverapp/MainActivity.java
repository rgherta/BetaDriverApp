package com.ride.driverapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ride.driverapp.Base.TrackingActivity;
import com.ride.driverapp.Registration.CustomLoginActivity;
import com.ride.driverapp.Registration.RegistrationActivity;

public class MainActivity extends TrackingActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrieveFCMToken();


        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_signin).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                goRegistration();
                break;
            case R.id.btn_signin:
                goCustomSignin();
                break;

        }
    }

    private void goCustomSignin() {
        Intent intent = new Intent(this, CustomLoginActivity.class);
        startActivity(intent);
    }

    private void goRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    //TODO: handle token refresh  https://stackoverflow.com/questions/37454501/firebase-fcm-force-ontokenrefresh-to-be-called
    private void retrieveFCMToken(){
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                    preferencesEditor.putString("FcmToken", token);
                    preferencesEditor.apply();

                    // Log and toast
                    String msg = token;
                    Log.w(TAG, msg);
                });
        // [END retrieve_current_token]


    }




}
