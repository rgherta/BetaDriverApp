package com.ride.driverapp.ui.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.R;
import com.ride.driverapp.ui.RideListActivity;

public class CustomLoginActivity extends TrackingActivity implements View.OnClickListener {

    FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);

        auth = FirebaseAuth.getInstance();


        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                finish();
                break;

            case R.id.btn_login:
                goOrders();
                break;

            case R.id.btn_reset:
                goReset();
                break;
        }
    }

    private void goReset() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void goOrders() {

        TextInputEditText mEmail = findViewById(R.id.txt_email_value);
        TextInputEditText mPassword = findViewById(R.id.txt_password_value);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();


        if( TextUtils.isEmpty( email ) || !Patterns.EMAIL_ADDRESS.matcher( email ).matches()
                || TextUtils.isEmpty( password ) || password.length() < 6
        ){
            mEmail.setError("Please enter a valid email or password");
            return;
        };


        auth.signInWithEmailAndPassword(email, password )
                .addOnSuccessListener(this, task -> {

                    auth.getInstance().getCurrentUser().getIdToken(false).addOnSuccessListener(this, tokenTask ->{
                        String token = tokenTask.getToken();
                        sharedPreferences.edit().putString("AuthToken", token).commit();
                        //TODO: request db data and save locally

                        Intent intent = new Intent(this, RideListActivity.class);
                        startActivity(intent);
                        finish();

                    });

                })
                .addOnFailureListener(this, task -> {
                    Toast.makeText(CustomLoginActivity.this, "Unable to login", Toast.LENGTH_LONG).show();
                });

    }

}
