package com.ride.driverapp.ui.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ride.driverapp.BuildConfig;
import com.ride.driverapp.R;
import com.ride.driverapp.viewmodel.RegViewModel;

import java.security.Permissions;

public class TrackingActivity extends FragmentActivity {

    private static final String TAG = RegViewModel.class.getSimpleName();

    protected boolean mLocationPermissionGranted = false;
    protected SharedPreferences sharedPreferences;
    protected String mPackageName;
    protected int mPackageCode;

    protected FusedLocationProviderClient mFusedLocationProviderClient;
    protected LocationRequest locationRequest;
    protected static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000, LOCATION_CHANGE = 10;

    protected LatLng mDefaultLocation = new LatLng(44.439663, 26.096306);
    //protected Location mLastknownLocation;
    protected MutableLiveData<Location> mLastknownLocation = new MutableLiveData<>();
    protected static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            mPackageName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            mPackageCode = (int) PackageInfoCompat.getLongVersionCode(this.getPackageManager().getPackageInfo(this.getPackageName(), 0));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        sharedPreferences = getSharedPreferences( getString(R.string.pref_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ApplicationId", BuildConfig.APPLICATION_ID );
        editor.putString("PackageName", mPackageName);
        editor.putInt("PackageCode", mPackageCode);
        editor.commit();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);


    }

    @Override
    protected void onStart() {
        super.onStart();

        getLocationPermission();
        statusCheck();
        getDeviceLocation();
        //startLocationUpdates();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }



    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }



    private void getDeviceLocation() {

        try {
            //TODO: if no permissions
            if (mLocationPermissionGranted) {
                    mFusedLocationProviderClient.getLastLocation()

                     .addOnSuccessListener(this, location -> {

                        if (location != null) {
                           mLastknownLocation.setValue(location);
                            Log.w(TAG, "lastloc: " + mLastknownLocation.getValue().toString());
                        } else {
                            Location newLocation = new Location("myprovider");
                            newLocation.setLatitude( mDefaultLocation.latitude );
                            newLocation.setLongitude( mDefaultLocation.longitude );
                            mLastknownLocation.setValue(newLocation);

                            Log.w(TAG, "lastlocnull: " + mLastknownLocation.getValue().toString());
                        }

                     })

                     .addOnFailureListener(this, e -> {
                         Log.e("Failure: %s", e.getMessage());
                     });
            }

        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

    };

    protected LocationCallback locCallback(){
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data

                    if(location.distanceTo(mLastknownLocation.getValue()) >= LOCATION_CHANGE) {
                        mLastknownLocation.setValue(location);
                        //Log.w(TAG, "locupdate: " + mLastknownLocation.getValue().toString());
                    }

                }
            };
        };

    }

    protected void startLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locCallback(),null /* Looper */);
    }

    protected void stopLocationUpdates() {
        if(mFusedLocationProviderClient != null)
        mFusedLocationProviderClient.removeLocationUpdates(locCallback());
    }




}
