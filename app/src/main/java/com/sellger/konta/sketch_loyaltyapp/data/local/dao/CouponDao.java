package com.sellger.konta.sketch_loyaltyapp.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import java.util.List;

/**
 * Data Access Object for the coupons table.
 */
@Dao
public interface CouponDao {

    /**
     * Select all coupons from the coupon table.
     *
     * @return all coupons.
     */
    @Query("SELECT * FROM coupon_table")
    List<Coupon> getAllCoupons();

    /**
     * Select single coupon from the coupon table.
     *
     * @return single coupon.
     */
    @Query("SELECT * FROM coupon_table WHERE id = :id")
    Coupon getSingleCoupon(int id);

    /**
     * Insert all coupons from the coupon table.
     */
    @Insert
    void insertAllCoupons(List<Coupon> couponList);

    /**
     * Delete all coupons from the coupon table.
     */
    @Query("DELETE FROM coupon_table")
    void deleteAllCoupons();
}
