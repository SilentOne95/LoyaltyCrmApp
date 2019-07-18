package com.ecommercelab.loyaltyapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecommercelab.loyaltyapp.data.entity.MenuComponent;

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
