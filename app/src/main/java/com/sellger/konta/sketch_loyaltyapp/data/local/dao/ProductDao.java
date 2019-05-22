package com.sellger.konta.sketch_loyaltyapp.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.List;

/**
 * Data Access Object for the products table.
 */
@Dao
public interface ProductDao {

    /**
     * Select all products from the product table.
     *
     * @return all products.
     */
    @Query("SELECT * FROM product_table")
    List<Product> getAllProducts();

    /**
     * Select single product from the product table.
     *
     * @return single product.
     */
    @Query("SELECT * FROM product_table WHERE id = :id")
    Product getSingleProduct(int id);

    /**
     * Insert all products from the product table.
     */
    @Insert
    void insertAllProducts(List<Product> productList);

    /**
     * Delete all products from the product table.
     */
    @Query("DELETE FROM product_table")
    void deleteAllProducts();
}
