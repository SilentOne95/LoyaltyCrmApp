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

    void getAllProducts(@NonNull final LoadDataCallback callback);

    void getSingleProduct(int id, @NonNull final GetSingleDataCallback callback);

    void getAllCoupons(@NonNull final LoadDataCallback callback);

    void getSingleCoupon(int id, @NonNull final GetSingleDataCallback callback);

    void getAllMarkers(@NonNull final LoadDataCallback callback);

    void getSingleMarker(int id, @NonNull final GetSingleDataCallback callback);

    void getAllOpenHours(@NonNull final LoadDataCallback callback);

    void getSingleOpenHour(int id, @NonNull final GetSingleDataCallback callback);

    void getAllPages(@NonNull final LoadDataCallback callback);

    void getSinglePage(int id, @NonNull final GetSingleDataCallback callback);

    void deleteData();
}
