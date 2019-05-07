package com.sellger.konta.sketch_loyaltyapp.data.local;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.utils.AppExecutors;

/**
 * Concrete implementation of a data source as a db
 */
public class LoyaltyLocalDataSource implements LoyaltyDataSource {

    private static volatile LoyaltyLocalDataSource INSTANCE;

    private TestDao mTestDao;
    private AppExecutors mAppExecutors;

    // Prevent direct instantiation
    private LoyaltyLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull TestDao testDao) {
        mAppExecutors = appExecutors;
        mTestDao = testDao;
    }

    public static LoyaltyLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull TestDao testDao) {
        if (INSTANCE == null) {
            synchronized (LoyaltyLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoyaltyLocalDataSource(appExecutors, testDao);
                }
            }
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
