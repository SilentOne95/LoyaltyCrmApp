package com.sellger.konta.sketch_loyaltyapp.data.local;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.CouponDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MarkerDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MenuDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.OpenHourDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.PageDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.ProductDao;
import com.sellger.konta.sketch_loyaltyapp.utils.AppExecutors;

/**
 * Concrete implementation of a data source as a db
 */
public class LoyaltyLocalDataSource implements LoyaltyDataSource {

    private static volatile LoyaltyLocalDataSource INSTANCE;

    private MenuDao mMenuDao;
    private ProductDao mProductDao;
    private CouponDao mCouponDao;
    private MarkerDao mMarkerDao;
    private OpenHourDao mOpenHourDao;
    private PageDao mPageDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation
    private LoyaltyLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull MenuDao menuDao,
                                   @NonNull ProductDao productDao, @NonNull CouponDao couponDao,
                                   @NonNull MarkerDao markerDao, @NonNull OpenHourDao openHourDao,
                                   @NonNull PageDao pageDao) {
        mAppExecutors = appExecutors;
        mMenuDao = menuDao;
        mProductDao = productDao;
        mCouponDao = couponDao;
        mMarkerDao = markerDao;
        mOpenHourDao = openHourDao;
        mPageDao = pageDao;
    }

    public static LoyaltyLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull MenuDao menuDao,
                                                     @NonNull ProductDao productDao, @NonNull CouponDao couponDao,
                                                     @NonNull MarkerDao markerDao, @NonNull OpenHourDao openHourDao,
                                                     @NonNull PageDao pageDao) {
        if (INSTANCE == null) {
            synchronized (LoyaltyLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoyaltyLocalDataSource(appExecutors, menuDao, productDao,
                            couponDao, markerDao, openHourDao, pageDao);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * Get all the data from SQLite database.
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
    public void deleteData() {

    }
}
