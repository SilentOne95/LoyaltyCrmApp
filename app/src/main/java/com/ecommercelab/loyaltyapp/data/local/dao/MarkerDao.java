package com.ecommercelab.loyaltyapp.data.local.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecommercelab.loyaltyapp.data.entity.Marker;

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

    /**
     * Get cursor when using SearchView.
     *
     * @return Cursor with data that contains provided text inside title.
     */
    @Query("SELECT * FROM marker_table WHERE title LIKE :providedText")
    Cursor getCursorMarker(String providedText);
}
