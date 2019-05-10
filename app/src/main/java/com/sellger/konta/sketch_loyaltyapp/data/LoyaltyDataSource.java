package com.sellger.konta.sketch_loyaltyapp.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface LoyaltyDataSource {

    interface LoadDataCallback {

        void onDataLoaded(List<?> data);

        void onDataNotAvailable();
    }

    interface GetSingleDataCallback<T> {

        void onDataLoaded(T object);

        void onDataNotAvailable();
    }

    void getMenu(@NonNull final LoadDataCallback callback);

    void saveMenu(@NonNull final List<?> menuComponentsList);

    void getAllProducts(@NonNull final LoadDataCallback callback);

    void saveAllProducts(@NonNull final List<?> productsList);

    void getSingleProduct(int id, @NonNull final GetSingleDataCallback callback);

    void getAllCoupons(@NonNull final LoadDataCallback callback);

    void saveAllCoupons(@NonNull final List<?> couponsList);

    void getSingleCoupon(int id, @NonNull final GetSingleDataCallback callback);

    void getAllMarkers(@NonNull final LoadDataCallback callback);

    void saveAllMarkers(@NonNull final List<?> markersList);

    void getSingleMarker(int id, @NonNull final GetSingleDataCallback callback);

    void getAllOpenHours(@NonNull final LoadDataCallback callback);

    void saveAllOpenHours(@NonNull final List<?> openHoursList);

    void getSingleOpenHour(int id, @NonNull final GetSingleDataCallback callback);

    void getAllPages(@NonNull final LoadDataCallback callback);

    void saveAllPages(@NonNull final List<?> pagesList);

    void getSinglePage(int id, @NonNull final GetSingleDataCallback callback);

    void deleteData();
}
