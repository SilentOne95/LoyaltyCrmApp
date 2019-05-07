package com.sellger.konta.sketch_loyaltyapp.data.remote;

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
    public void getMenu() {

    }

    @Override
    public void getAllProducts() {

    }

    @Override
    public void getAllCoupons() {

    }

    @Override
    public void getAllMarkers() {

    }

    @Override
    public void getStaticPage(int id) {

    }
}
