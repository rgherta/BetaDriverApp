package com.ride.driverapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ride.driverapp.model.DriverContract;

@Database( entities = {DriverContract.class}, version=1, exportSchema = false)
public abstract class DriverAppDatabase extends RoomDatabase {

    public abstract IDriverDao driverDao();

    private static volatile  DriverAppDatabase INSTANCE;


    public static DriverAppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (DriverAppDatabase.class){
                if (INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext()
                            , DriverAppDatabase.class
                            , "database"
                    ).build();

                }
            }
        }

        return INSTANCE;

    }

}
