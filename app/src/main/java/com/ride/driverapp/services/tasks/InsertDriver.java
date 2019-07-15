package com.ride.driverapp.services.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.ride.driverapp.db.IDriverDao;
import com.ride.driverapp.model.DriverContract;

public class InsertDriver extends AsyncTask<DriverContract, Void, Void> {

    private IDriverDao driverDao;

    public InsertDriver(IDriverDao dao){
        driverDao = dao;
    }

    @Override
    protected Void doInBackground(DriverContract[] driverContracts) {
        driverDao.clearTable();
        driverDao.insert(driverContracts[0]);
        return null;
    }


}
