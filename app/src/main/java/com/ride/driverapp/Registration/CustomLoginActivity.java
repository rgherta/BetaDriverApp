package com.ride.driverapp.Registration;

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
import com.ride.driverapp.Base.TrackingActivity;
import com.ride.driverapp.R;
import com.ride.driverapp.RideListActivity;

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
                .addOnCompleteListener(CustomLoginActivity.this, task -> {

                    if (!task.isSuccessful()) {
                        // there was an error
                        Toast.makeText(CustomLoginActivity.this, "Unable to login", Toast.LENGTH_LONG).show();

                    } else {

                        auth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(tokenTask -> {
                            if ( tokenTask.isSuccessful() ) {
                                String token = tokenTask.getResult().getToken();

                                SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                                preferencesEditor.putString("AuthToken", token);
                                preferencesEditor.commit();

                                Log.w("authtoken", token);
                            }
                        });


                        Intent intent = new Intent(this, RideListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });



    }


}
