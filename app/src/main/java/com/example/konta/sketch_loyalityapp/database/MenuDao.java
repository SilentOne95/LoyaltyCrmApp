package com.example.konta.sketch_loyalityapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MenuDao {

    @Insert
    void insert(Menu menu);

    @Query("DELETE FROM menu_table")
    void deleteAll();

    @Query("SELECT * FROM menu_table")
    LiveData<List<Menu>> getAllMenus();
}
