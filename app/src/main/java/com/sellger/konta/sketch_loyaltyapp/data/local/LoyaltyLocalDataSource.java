package com.sellger.konta.sketch_loyaltyapp.data.local;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.utils.AppExecutors;

/**
 * Concrete implementation of a data source as a db
 */
public class LoyaltyLocalDataSource implements LoyaltyDataSource {

    private static volatile LoyaltyLocalDataSource INSTANCE;

    private LoyaltyDao mLoyaltyDao;
    private AppExecutors mAppExecutors;

    // Prevent direct instantiation
    private LoyaltyLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull LoyaltyDao loyaltyDao) {
        mAppExecutors = appExecutors;
        mLoyaltyDao = loyaltyDao;
    }

    public static LoyaltyLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull LoyaltyDao loyaltyDao) {
        if (INSTANCE == null) {
            synchronized (LoyaltyLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoyaltyLocalDataSource(appExecutors, loyaltyDao);
                }
            }
        }

        return INSTANCE;
    }

    /**
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
