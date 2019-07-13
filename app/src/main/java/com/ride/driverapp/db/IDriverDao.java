package com.ride.driverapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ride.driverapp.model.DriverContract;

@Dao
public interface IDriverDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DriverContract driver);

    @Query("DELETE FROM driver_table")
    void clearTable();

    @Query("SELECT * FROM driver_table LIMIT 1")
    DriverContract getDriver();

}
