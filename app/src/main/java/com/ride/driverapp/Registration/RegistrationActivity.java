package com.ride.driverapp.Registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.ride.driverapp.Base.TrackingActivity;
import com.ride.driverapp.RegViewModel;
import com.ride.driverapp.R;
import com.ride.driverapp.databinding.ActivityCustomRegistrationBinding;

import org.w3c.dom.Text;

import java.util.regex.Pattern;


public class RegistrationActivity extends TrackingActivity implements View.OnClickListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    RegViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_registration);

        //Obtain viewmodel from ViewModelProviders through lifecycle library
        viewModel = ViewModelProviders.of(this).get(RegViewModel.class);
        //Obtain binding
            ActivityCustomRegistrationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_registration);
            binding.setLifecycleOwner(this);
        //Bind layout with viewmodel
            binding.setVm(viewModel);

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

        //IApiService api = ApiServiceGenerator.createService(IApiService.class, "na");

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                goBack();
                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private boolean validFields() {

        return viewModel.hasValidEmail() && viewModel.hasValidPassword()
                && viewModel.hasValidFullName() && viewModel.hasValidPhone();

    }

    private void goBack() {
        finish();
    }

    private void goNext() {

        if( validFields() ){

            Intent intent = new Intent(this, RegistrationExtraActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Some fields are incomplete", Toast.LENGTH_LONG).show();
        }
    }




}
