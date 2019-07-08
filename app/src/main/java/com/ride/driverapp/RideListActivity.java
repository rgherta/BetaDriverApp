package com.ride.driverapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;
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
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.ride.driverapp.Adapters.RidesAdapter;
import com.ride.driverapp.Base.TrackingActivity;
import com.ride.driverapp.Entities.RideContract;
import com.ride.driverapp.databinding.ActivityRideListBinding;

import java.util.ArrayList;
import java.util.List;

public class RideListActivity extends TrackingActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = RegViewModel.class.getSimpleName();

    RidesViewModel viewModel;
    private GoogleMap mMap;
    private MapView mapView;

    //Recycler
    private RecyclerView recyclerView;
    private RecyclerView.Adapter ridesAdapter;
    private RecyclerView.LayoutManager ridesLayoutManager;
    private ArrayList<RideContract> ridesArray;


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);


        viewModel.getAdapterItem().observe(this, position -> {

            RideContract ride = ridesArray.get(position);
            Log.w(TAG, "clicked: " + ride.toString());
            List<LatLng> pointsArray = PolyUtil.decode( ride.getPoints() );

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


}
