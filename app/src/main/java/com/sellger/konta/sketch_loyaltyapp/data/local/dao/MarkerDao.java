package com.sellger.konta.sketch_loyaltyapp.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;

import java.util.List;

/**
 * Data Access Object for the markers table.
 */
@Dao
public interface MarkerDao {

    /**
     * Select all markers from the marker table.
     *
     * @return all markers.
     */
    @Query("SELECT * FROM marker_table")
    List<Marker> getAllMarkers();

    /**
     * Select single marker from the marker table.
     *
     * @return single marker.
     */
    @Query("SELECT * FROM marker_table WHERE id = :id")
    Marker getSingleMarker(int id);

    /**
     * Insert all markers from the marker table.
     */
    @Insert
    void insertAllMarkers(List<Marker> markerList);

    /**
     * Delete all markers from the marker table.
     */
    @Query("DELETE FROM marker_table")
    void deleteAllMarkers();
}
