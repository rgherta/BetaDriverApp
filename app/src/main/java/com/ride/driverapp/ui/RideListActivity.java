package com.ride.driverapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.ride.driverapp.R;
import com.ride.driverapp.model.AcceptanceContract;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.services.MyFirebaseMessagingService;
import com.ride.driverapp.ui.adapters.RidesAdapter;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.ui.fragments.TimePickerFragment;
import com.ride.driverapp.viewmodel.RegViewModel;
import com.ride.driverapp.databinding.ActivityRideListBinding;
import com.ride.driverapp.viewmodel.RidesViewModel;

import java.util.ArrayList;
import java.util.List;

public class RideListActivity extends TrackingActivity implements OnMapReadyCallback{

    private static final String TAG = RegViewModel.class.getSimpleName();

    RidesViewModel viewModel;
    private GoogleMap mMap;
    private MapView mapView;

    //Recycler
    private RecyclerView recyclerView;
    private RecyclerView.Adapter ridesAdapter;
    private RecyclerView.LayoutManager ridesLayoutManager;
    private ArrayList<RideContract> ridesArray;

    protected DriverContract accountData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_list);

        //Obtain viewmodel from ViewModelProviders through lifecycle library
        viewModel = ViewModelProviders.of(this).get(RidesViewModel.class);
        //Obtain binding
        ActivityRideListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ride_list);
        binding.setLifecycleOwner(this);
        //Bind layout with viewmodel
        binding.setVm(viewModel);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapView = findViewById(R.id.map_info);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(
//                menuItem -> {
//
//                    menuItem.setChecked(true);
//
//                    switch (menuItem.getItemId()) {
//                        case R.id.signout:
//                            signOut();
//                            break;
//                    }
//
//                    return true;
//                });

        TextInputEditText til = findViewById(R.id.txt_driver_timeline_value);
        til.setOnFocusChangeListener((view, isFocused) -> {
            if (isFocused) showTimePickerDialog(view);
        });

                viewModel.getAccountData().observe( this, driver -> {
                        this.accountData = driver;
                        //Log.w("MYDRIVER", this.accountData.toString())
;                });

                viewModel.getEstimatedArrival().observe(this, time -> {
                    Log.w("mainact", time);
                    clearInputs();

                });


        MyFirebaseMessagingService.systemBus.observe(this, o -> {
            Log.w(TAG, "Notification REFRESH: ");
            if(o == MyFirebaseMessagingService.ride.REFRESH) getAvailableRides();
        });

        MyFirebaseMessagingService.rideBus.observe(this, o -> {
            Log.w(TAG, "Notification START: ");
            Intent intent = new Intent(this, RideConfirmationActivity.class);
            startActivity(intent);
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, viewModel.getStatusArray());
        AppCompatSpinner mySpinner = findViewById(R.id.driver_status_value);
        mySpinner.setAdapter(adapter);

        viewModel.setRegStatus(adapter.getCount() - 1);
        viewModel.getRegStatus().observe(this, status -> {
            status += 5;
            JsonObject newStatus = new JsonObject();
            newStatus.addProperty("status", status);
            viewModel.updateStatus(newStatus);

        });

        getAvailableRides();



        viewModel.getRegStatus().observe(this, o -> {
            getAvailableRides();
            Log.w(TAG, "Changed status: refreshing list");
        });


    }

    public void getAvailableRides() {
        recyclerView = findViewById(R.id.recycler_ride_list);
        recyclerView.setHasFixedSize(true);
        ridesLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(ridesLayoutManager);
        ridesArray = new ArrayList<>();
        ridesAdapter = new RidesAdapter(ridesArray, viewModel);
        recyclerView.setAdapter(ridesAdapter);

        viewModel.getRides().observe(this, rideContracts -> {
            Log.w(TAG, rideContracts.toString());
            ridesArray.clear();
            ridesArray.addAll(rideContracts);
            ridesAdapter.notifyDataSetChanged();
        });
    }



    public void openDrawer(View v){
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void clickSpinner(View v){
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        viewModel.getAdapterItem().observe(this, myRide -> {
            mMap.clear();

            List<LatLng> pointsArray = PolyUtil.decode( myRide.getPoints() );
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(LatLng poi : pointsArray){
                builder.include(poi);
            }
            LatLngBounds bounds = builder.build();

            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(pointsArray);

            mMap.addPolyline(lineOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));


        });

    }


    private void signOut() {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void goNext(View v){
        viewModel.rideAccept().observe( this, success -> {
            if(success){
//                Intent intent = new Intent(this, RideConfirmationActivity.class);
//                startActivity(intent);
                clearInputs();
                viewModel.getShowDetails().setValue(false);
                viewModel.getEstimatedArrival().setValue("");
                viewModel.getEstimatedPrice().setValue("");

                Toast.makeText(this, "Ride Accepted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(viewModel.getEstimatedArrival());
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void closeDetails(View v){
        clearInputs();
        viewModel.getShowDetails().setValue(false);
        viewModel.getEstimatedArrival().setValue("");
        viewModel.getEstimatedPrice().setValue("");
    }


     private void clearInputs(){
         TextInputEditText editText1 = findViewById(R.id.txt_driver_timeline_value);
         editText1.clearFocus();
         TextInputEditText editText2 = findViewById(R.id.txt_driver_ppk_value);
         editText2.clearFocus();
     }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        clearInputs();
        viewModel.getShowDetails().setValue(false);
        viewModel.getEstimatedArrival().setValue("");
        viewModel.getEstimatedPrice().setValue("");
    }
}
