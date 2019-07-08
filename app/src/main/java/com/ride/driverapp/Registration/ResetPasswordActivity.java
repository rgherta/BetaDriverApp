package com.ride.driverapp.Registration;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.ride.driverapp.R;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                finish();
                break;
            case R.id.btn_reset:
                resetPassword();
                break;
        }

    }

    private void resetPassword() {

        TextInputEditText mEmail = findViewById(R.id.txt_email_value);
        String email = mEmail.getText().toString();

        if( TextUtils.isEmpty( email ) || !Patterns.EMAIL_ADDRESS.matcher( email ).matches() ){
            mEmail.setError("Please enter a valid email");
            return;
        };


        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
            };

            finish();

        });
    }



}
