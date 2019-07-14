package com.ride.driverapp.ui.registration;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.ui.MainActivity;
import com.ride.driverapp.R;
import com.ride.driverapp.viewmodel.RegViewModel;
import com.ride.driverapp.services.api.ApiServiceGenerator;
import com.ride.driverapp.services.api.IApiService;
import com.ride.driverapp.databinding.ActivityRegistrationExtraBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationExtraActivity extends TrackingActivity implements View.OnClickListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private FirebaseAuth auth;


    RegViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra);

        auth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(RegViewModel.class);
        ActivityRegistrationExtraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_extra);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);

        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        String[] CURRENCIES = this.getResources().getStringArray(R.array.curr_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.dropdown_menu_popup_item, CURRENCIES);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.txt_driver_cur_value);
        editTextFilledExposedDropdown.setAdapter(adapter);

        //TODO: safeunbox, Boolean/boolean
        viewModel.getRegPayCash().setValue(new Boolean(true));
        viewModel.getRegPayPal().setValue(new Boolean(false));

        String authToken = sharedPreferences.getString("AuthToken", "");
        Log.w("mytoken", authToken);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                goBack();
                break;
            case R.id.btn_register:
                goReg();
                break;
        }
    }



    private void goBack() {
        finish();
    }


    private void goReg() {

        if( !validFields() ){
            Toast.makeText(this, "Some fields are incomplete", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(viewModel.getRegEmail().getValue(), viewModel.getRegPassword().getValue()).addOnSuccessListener(this, task ->{

            Toast.makeText(RegistrationExtraActivity.this, "createUserWithEmail:onComplete:successfull", Toast.LENGTH_SHORT).show();
            auth.getCurrentUser().getIdToken(false).addOnSuccessListener(this, tokenTask -> {
                String token = tokenTask.getToken();
                sharedPreferences.edit().putString("AuthToken", token).commit();
                Log.w("authtoken", token);
                saveUserDB();

            });

        }).addOnFailureListener(this, task -> {
            Toast.makeText(RegistrationExtraActivity.this, "createUserWithEmail:failure", Toast.LENGTH_SHORT).show();
        });
    }


    private boolean validFields() {

        return viewModel.hasValidCity() && viewModel.hasValidVehicle() && viewModel.hasValidPlate() && viewModel.hasValidPpk() && viewModel.hasValidCurr();
    }


     private void saveUserDB(){

                                String fcmToken = sharedPreferences.getString("FcmToken", null);
                                HashMap<String, Boolean> payment = new HashMap<>();
                                payment.put("cash", viewModel.getRegPayCash().getValue());
                                payment.put("paypal", viewModel.getRegPayPal().getValue());

                                HashMap<String, Double> loc = new HashMap<>();
                                loc.put("lat", new Double(0));
                                loc.put("long", new Double(0));


                                DriverContract driver = new DriverContract( auth.getUid(), fcmToken, viewModel.getRegFullname().getValue(), 7
                                        , viewModel.getRegEmail().getValue(), viewModel.getRegVehicle().getValue(), viewModel.getRegPlate().getValue()
                                        , viewModel.getRegPhone().getValue()
                                        , payment, viewModel.getRegCity().getValue(), Double.valueOf(viewModel.getRegPpk().getValue()), viewModel.getRegCurr().getValue()
                                        , 0, 0, 0, loc
                                );


                                viewModel.sendDriverData(driver, this);

            goMain();

         }


         private void goMain(){
             viewModel.clearData();
             Intent intent = new Intent(RegistrationExtraActivity.this, MainActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(intent);
             finish();
         }
         

}
