package com.sellger.konta.sketch_loyaltyapp.data.remote;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Concrete implementation of a data source as a network connection
 */
public class LoyaltyRemoteDataSource implements LoyaltyDataSource {

    private static LoyaltyRemoteDataSource INSTANCE;

    private CompositeDisposable disposable = new CompositeDisposable();

    public static LoyaltyRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoyaltyRemoteDataSource();
        }

        return INSTANCE;
    }

    // Prevent direct instantiation
    private LoyaltyRemoteDataSource() {}

    /**
     * Get all the data from server.
     *
     * Note: {@link LoadDataCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getMenu(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getAllProducts(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleProduct(int id, @NonNull GetSingleDataCallback callback) {

    }

    @Override
    public void getAllCoupons(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleCoupon(int id, @NonNull GetSingleDataCallback callback) {

    }

    @Override
    public void getAllMarkers(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleMarker(int id, @NonNull GetSingleDataCallback callback) {

    }

    @Override
    public void getAllOpenHours(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSingleOpenHour(int id, @NonNull GetSingleDataCallback callback) {

    }

    @Override
    public void getAllPages(@NonNull LoadDataCallback callback) {

    }

    @Override
    public void getSinglePage(int id, @NonNull GetSingleDataCallback callback) {

    }

    @Override
    public void saveData(String dataType, @NonNull List<?> dataList) {
        // Not required for the remote data source, because there is no option for saving data
        // anywhere besides SQLite DB using {@link LoyaltyLocalDataSource}
    }

    @Override
    public void deleteData(String dataType) {
        // Not required for the remote data source, because there is no option for deletin data
        // anywhere besides SQLite DB using {@link LoyaltyLocalDataSource}
    }

    @Override
    public void clearData() {
        // Clear disposables
    }
}
