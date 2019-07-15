package com.ride.driverapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.ride.driverapp.R;
import com.ride.driverapp.model.DriverContract;
import com.ride.driverapp.ui.adapters.RidesAdapter;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.model.RideContract;
import com.ride.driverapp.ui.registration.RegistrationExtraActivity;
import com.ride.driverapp.viewmodel.RegViewModel;
import com.ride.driverapp.databinding.ActivityRideListBinding;
import com.ride.driverapp.viewmodel.RidesViewModel;

import java.util.ArrayList;
import java.util.List;

public class RideListActivity extends TrackingActivity implements OnMapReadyCallback {

    private static final String TAG = RegViewModel.class.getSimpleName();

    RidesViewModel viewModel;
    private GoogleMap mMap;
    private MapView mapView;

    //Recycler
    private RecyclerView recyclerView;
    private RecyclerView.Adapter ridesAdapter;
    private RecyclerView.LayoutManager ridesLayoutManager;
    private ArrayList<RideContract> ridesArray;

    private DriverContract accountData;


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


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {

                    menuItem.setChecked(true);

                    switch (menuItem.getItemId()) {
                        case R.id.signout:
                            signOut();
                            break;
                    }

                    return true;
                });


                viewModel.getAccountData().observe( this, driver -> {
                        accountData = driver;
;                });




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
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.END);
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


}
