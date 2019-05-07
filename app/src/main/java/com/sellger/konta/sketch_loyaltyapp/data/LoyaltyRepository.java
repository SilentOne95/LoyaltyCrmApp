package com.sellger.konta.sketch_loyaltyapp.data;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.local.TestDao;

public class LoyaltyRepository implements LoyaltyDataSource {

    private static LoyaltyRepository INSTANCE = null;

    private final LoyaltyDataSource mLoyaltyRemoteDataSource;
    private final LoyaltyDataSource mLoyaltyLocalDataSource;

    private TestDao mTestDao;

    // Prevent direct instantiation
    private LoyaltyRepository(@NonNull LoyaltyDataSource loyaltyRemoteDataSource,
                              @NonNull LoyaltyDataSource loyaltyLocalDataSource) {
        mLoyaltyRemoteDataSource = loyaltyRemoteDataSource;
        mLoyaltyLocalDataSource = loyaltyLocalDataSource;
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param loyaltyRemoteDataSource the backend data source
     * @param loyaltyLocalDataSource  the device storage data source
     * @return the {@link LoyaltyRepository} instance
     */
    public static LoyaltyRepository getInstance(LoyaltyDataSource loyaltyRemoteDataSource,
                                                LoyaltyDataSource loyaltyLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new LoyaltyRepository(loyaltyRemoteDataSource, loyaltyLocalDataSource);
        }
        return INSTANCE;
    }

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
