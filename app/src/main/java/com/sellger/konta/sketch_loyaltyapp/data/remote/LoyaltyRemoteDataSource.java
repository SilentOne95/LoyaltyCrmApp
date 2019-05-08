package com.sellger.konta.sketch_loyaltyapp.data.remote;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;

/**
 * Concrete implementation of a data source as a network connection
 */
public class LoyaltyRemoteDataSource implements LoyaltyDataSource {

    private static LoyaltyRemoteDataSource INSTANCE;

    public static LoyaltyRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoyaltyRemoteDataSource();
        }

        return INSTANCE;
    }

    // Prevent direct instantiation
    private LoyaltyRemoteDataSource() {}

    // Get data
    @Override
    public void getMenu(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getAllProducts(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleProduct(@NonNull LoadDataCallback callback, int id) {

    }

    @Override
    public void getAllCoupons(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleCoupon(@NonNull LoadDataCallback callback, int id) {

    }

    @Override
    public void getAllMarkers(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleMarker(@NonNull LoadDataCallback callback, int id) {

    }

    @Override
    public void getStaticPage(@NonNull LoadDataCallback callback, int id) {

    }
}
