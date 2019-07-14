package com.ride.driverapp.services.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ride.driverapp.db.IDriverDao;
import com.ride.driverapp.model.DriverContract;

public class InsertDriver extends AsyncTask<DriverContract, Void, Void> {
    private static final String TAG = InsertDriver.class.getSimpleName();

    private IDriverDao driverDao;

    public InsertDriver(IDriverDao dao){
        driverDao = dao;
    }

    @Override
    protected Void doInBackground(DriverContract[] driverContracts) {
        driverDao.insert(driverContracts[0]);
        Log.i("thisisimportant", driverContracts[0].toString());
        return null;
    }


}
