package com.ride.driverapp.ui.base;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.pm.PackageInfoCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.ride.driverapp.BuildConfig;
import com.ride.driverapp.R;

public class TrackingActivity extends FragmentActivity {

    protected boolean mLocationPermissionGranted = false;
    protected SharedPreferences sharedPreferences;
    protected String mPackageName;
    protected int mPackageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        getLocationPermission();


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

        statusCheck();
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

}
