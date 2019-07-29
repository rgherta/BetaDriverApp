package com.ride.driverapp.ui;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ride.driverapp.R;
import com.ride.driverapp.ui.base.TrackingActivity;
import com.ride.driverapp.viewmodel.RegViewModel;

public class RideConfirmationActivity extends TrackingActivity implements OnMapReadyCallback {

    private static final String TAG = RegViewModel.class.getSimpleName();
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_info);
        mapFragment.onCreate(null);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng( mLastknownLocation.getLatitude(), mLastknownLocation.getLongitude() ), DEFAULT_ZOOM));



    }

    public void openDrawer(View v){

    }

}
