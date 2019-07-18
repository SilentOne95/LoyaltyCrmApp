package com.ecommercelab.loyaltyapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ecommercelab.loyaltyapp.data.entity.Page;

import java.util.List;

/**
 * Data Access Object for the page table.
 */
@Dao
public interface PageDao {

    /**
     * Select all pages from the page table.
     *
     * @return all pages.
     */
    @Query("SELECT * FROM page_table")
    List<Page> getAllPages();

    /**
     * Select single page from the page table.
     *
     * @return single page.
     */
    @Query("SELECT * FROM page_table WHERE id = :id")
    Page getSinglePage(int id);

    /**
     * Insert all pages from the page table.
     */
    @Insert
    void insertAllPages(List<Page> pageList);

    /**
     * Delete all pages from the page table.
     */
    @Query("DELETE FROM page_table")
    void deleteAllPages();
}
