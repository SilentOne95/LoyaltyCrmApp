package com.sellger.konta.sketch_loyaltyapp.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;

import java.util.List;

/**
 * Data Access Object for the hour table.
 */
@Dao
public interface OpenHourDao {

    /**
     * Select all open hours from the coupon table.
     *
     * @return all open hours.
     */
    @Query("SELECT * FROM hour_table")
    List<OpenHour> getAllOpenHours();

    /**
     * Select single open hour from the hour table.
     *
     * @return single open hour.
     */
    @Query("SELECT * FROM hour_table WHERE id = :id")
    OpenHour getSingleOpenHour(int id);

    /**
     * Insert all open hours from the hour table.
     */
    @Insert
    void insertAllOpenHours(List<OpenHour> openHourList);

    /**
     * Delete all open hours from the hour table.
     */
    @Query("DELETE FROM hour_table")
    void deleteAllOpenHours();

}
