package com.ride.driverapp.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ride.driverapp.Base.TrackingActivity;
import com.ride.driverapp.Entities.DriverContract;
import com.ride.driverapp.MainActivity;
import com.ride.driverapp.R;
import com.ride.driverapp.RegViewModel;
import com.ride.driverapp.Services.Api.ApiServiceGenerator;
import com.ride.driverapp.Services.Api.IApiService;
import com.ride.driverapp.databinding.ActivityRegistrationExtraBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationExtraActivity extends TrackingActivity implements View.OnClickListener {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private FirebaseAuth auth;
    private RegistrationExtraActivity.UserRegistrationTask mAuthTask = null;


    RegViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra);

        auth = FirebaseAuth.getInstance();


        //Obtain viewmodel from ViewModelProviders through lifecycle library
        viewModel = ViewModelProviders.of(this).get(RegViewModel.class);
        //Obtain binding
        ActivityRegistrationExtraBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_extra);
        binding.setLifecycleOwner(this);
        //Bind layout with viewmodel
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

        mAuthTask = new UserRegistrationTask(viewModel.getRegEmail().getValue(), viewModel.getRegPassword().getValue());
        mAuthTask.execute((Void) null);


    }



    private boolean validFields() {

        return viewModel.hasValidCity() && viewModel.hasValidVehicle()
                && viewModel.hasValidPlate() && viewModel.hasValidPpk()
                && viewModel.hasValidCurr();

    }



    //TODO: move to ViewModel/Repo/Service
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegistrationTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            auth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(RegistrationExtraActivity.this, task -> {
                        Toast.makeText(RegistrationExtraActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            //showProgress(false);
                            Toast.makeText(getBaseContext(), "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            if(isNew){
                            //TODO: call API
                                String fcmToken = sharedPreferences.getString("FcmToken", null);
                                HashMap<String, Boolean> payment = new HashMap<>();
                                payment.put("cash", viewModel.getRegPayCash().getValue());
                                payment.put("paypal", viewModel.getRegPayPal().getValue());

                                HashMap<String, Double> loc = new HashMap<>();
                                loc.put("lat", new Double(0));
                                loc.put("long", new Double(0));


                                DriverContract driver = new DriverContract(
                                    auth.getUid(), fcmToken
                                        , viewModel.getRegFullname().getValue()
                                        , 7
                                        , viewModel.getRegEmail().getValue()
                                        , viewModel.getRegVehicle().getValue()
                                        , viewModel.getRegPlate().getValue()
                                        , viewModel.getRegPhone().getValue()
                                        , payment
                                        , viewModel.getRegCity().getValue()
                                        , Double.valueOf(viewModel.getRegPpk().getValue())
                                        , viewModel.getRegCurr().getValue()
                                        , 0
                                        , 0
                                        , 0
                                        , loc
                                );


                                IApiService apiService = ApiServiceGenerator.createService(IApiService.class, getBaseContext());
                                Call<DriverContract> call = apiService.addUser(driver);
                                call.enqueue(new Callback<DriverContract>() {

                                    @Override
                                    public void onResponse(Call<DriverContract> call, Response<DriverContract> response) {
                                        Log.w("response", response.toString());

                                    }

                                    @Override
                                    public void onFailure(Call<DriverContract> call, Throwable t) {
                                        Log.w("responserror", t);
                                    }
                                });


                            }
                        }


                         FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnCompleteListener(tokenTask -> {
                            if ( tokenTask.isSuccessful() ) {
                                String token = tokenTask.getResult().getToken();


                                sharedPreferences.edit().putString("AuthToken", token);
                                sharedPreferences.edit().commit();

                                Log.w("authtoken", token);
                            }
                        });



                        //TODO: redirects to main activity
                        viewModel.clearData();
                        Intent intent = new Intent(RegistrationExtraActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    });


            return true;

        }




        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mAuthTask = null;
            //TODO: redirect to list activity
        }
    }

}
