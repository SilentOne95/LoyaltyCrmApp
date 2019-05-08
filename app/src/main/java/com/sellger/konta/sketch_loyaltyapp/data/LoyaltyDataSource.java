package com.sellger.konta.sketch_loyaltyapp.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface LoyaltyDataSource {

    interface LoadDataCallback {

        void onDataLoaded(List<?> data);

        void onDataNotAvailable();
    }

    void getMenu(@NonNull final LoadDataCallback callback);

    void getAllProducts(@NonNull final LoadDataCallback callback);

    void getSingleProduct(@NonNull final LoadDataCallback callback, int id);

    void getAllCoupons(@NonNull final LoadDataCallback callback);

    void getSingleCoupon(@NonNull final LoadDataCallback callback, int id);

    void getAllMarkers(@NonNull final LoadDataCallback callback);

    void getSingleMarker(@NonNull final LoadDataCallback callback, int id);

    void getStaticPage(@NonNull final LoadDataCallback callback, int id);
}
