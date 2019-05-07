package com.sellger.konta.sketch_loyaltyapp.data;

public interface LoyaltyDataSource {

    void getMenu();

    void getAllProducts();

    void getAllCoupons();

    void getAllMarkers();

    void getStaticPage(int id);
}
