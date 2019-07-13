package com.ride.driverapp.services.api;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class ApiService extends IntentService {
//TODO: move apiService interface here if possible

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ApiService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



    }
}
