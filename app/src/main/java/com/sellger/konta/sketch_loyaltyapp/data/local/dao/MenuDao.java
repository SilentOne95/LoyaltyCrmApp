package com.sellger.konta.sketch_loyaltyapp.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.util.List;

/**
 * Data Access Object for the menu table.
 */

@Dao
public interface MenuDao {

    /**
     * Select all menu components from the menu table.
     *
     * @return all menu components.
     */
    @Query("SELECT * FROM menu_table")
    List<MenuComponent> getMenu();

    /**
     * Insert all menu components from the menu table.
     */
    @Insert
    void insertMenu(List<MenuComponent> menuComponentList);

    /**
     * Delete all menu components from the menu table.
     */
    @Query("DELETE FROM menu_table")
    void deleteAllMenu();
}
