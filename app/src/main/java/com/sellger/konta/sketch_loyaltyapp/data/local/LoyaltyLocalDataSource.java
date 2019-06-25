package com.sellger.konta.sketch_loyaltyapp.data.local;

import android.database.Cursor;

import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.CouponDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MarkerDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MenuDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.OpenHourDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.PageDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.ProductDao;
import com.sellger.konta.sketch_loyaltyapp.utils.AppExecutors;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_COUPON;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_MARKER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_MENU;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_OPEN_HOUR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_PAGE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_PRODUCT;

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
        Runnable getRunnable = () -> {
            final List<MenuComponent> menuComponentList = mMenuDao.getMenu();

            mAppExecutors.mainThread().execute(() -> {
                if (!menuComponentList.isEmpty()) {
                    callback.onDataLoaded(menuComponentList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getAllProducts(@NonNull LoadDataCallback callback) {
        Runnable getRunnable = () -> {
            final List<Product> productList = mProductDao.getAllProducts();

            mAppExecutors.mainThread().execute(() -> {
                if (!productList.isEmpty()) {
                    callback.onDataLoaded(productList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getSingleProduct(int id, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final Product product = mProductDao.getSingleProduct(id);

            mAppExecutors.mainThread().execute(() -> {
                if (product != null) {
                    callback.onDataLoaded(product);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getAllCoupons(@NonNull LoadDataCallback callback) {
        Runnable getRunnable = () -> {
            final List<Coupon> couponList = mCouponDao.getAllCoupons();

            mAppExecutors.mainThread().execute(() -> {
                if (!couponList.isEmpty()) {
                    callback.onDataLoaded(couponList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getSingleCoupon(int id, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final Coupon coupon = mCouponDao.getSingleCoupon(id);

            mAppExecutors.mainThread().execute(() -> {
                if (coupon != null) {
                    callback.onDataLoaded(coupon);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getAllMarkers(@NonNull LoadDataCallback callback) {
        Runnable getRunnable = () -> {
            final List<Marker> markerList = mMarkerDao.getAllMarkers();

            mAppExecutors.mainThread().execute(() -> {
                if (!markerList.isEmpty()) {
                    callback.onDataLoaded(markerList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getSingleMarker(int id, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final Marker marker = mMarkerDao.getSingleMarker(id);

            mAppExecutors.mainThread().execute(() -> {
                if (marker != null) {
                    callback.onDataLoaded(marker);
                } else {
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }


    @Override
    public void getCursorMarker(String providedText, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final Cursor cursor = mMarkerDao.getCursorMarker("%" + providedText + "%");

            mAppExecutors.mainThread().execute(() -> {
                if (cursor != null && cursor.getColumnCount() != 0) {
                    callback.onDataLoaded(cursor);
                } else {
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getAllOpenHours(@NonNull LoadDataCallback callback) {
        Runnable getRunnable = () -> {
            final List<OpenHour> openHourList = mOpenHourDao.getAllOpenHours();

            mAppExecutors.mainThread().execute(() -> {
                if (!openHourList.isEmpty()) {
                    callback.onDataLoaded(openHourList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getSingleOpenHour(int id, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final OpenHour openHour = mOpenHourDao.getSingleOpenHour(id);

            mAppExecutors.mainThread().execute(() -> {
                if (openHour != null) {
                    callback.onDataLoaded(openHour);
                } else {
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getAllPages(@NonNull LoadDataCallback callback) {
        Runnable getRunnable = () -> {
            final List<Page> pageList = mPageDao.getAllPages();

            mAppExecutors.mainThread().execute(() -> {
                if (!pageList.isEmpty()) {
                    callback.onDataLoaded(pageList);
                } else {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    @Override
    public void getSinglePage(int id, @NonNull GetSingleDataCallback callback) {
        Runnable getRunnable = () -> {
            final Page page = mPageDao.getSinglePage(id);

            mAppExecutors.mainThread().execute(() -> {
                if (page != null) {
                    callback.onDataLoaded(page);
                } else {
                    callback.onDataNotAvailable();
                }
            });
        };

        mAppExecutors.diskIO().execute(getRunnable);
    }

    /**
     * Call relevant insert statement
     *
     * @param dataType of fetched data, it's necessary to trigger relevant insert statement
     * @param dataList of data, which is going to be stored in local db
     */
    @Override
    public void saveData(String dataType, @NonNull List<?> dataList) {
        checkNotNull(dataList);
        Runnable saveRunnable = null;

        switch (dataType) {
            case TYPE_MENU:
                saveRunnable = () -> mMenuDao.insertMenu((List<MenuComponent>) dataList);
                break;
            case TYPE_PRODUCT:
                saveRunnable = () -> mProductDao.insertAllProducts((List<Product>) dataList);
                break;
            case TYPE_COUPON:
                saveRunnable = () -> mCouponDao.insertAllCoupons((List<Coupon>) dataList);
                break;
            case TYPE_MARKER:
                saveRunnable = () -> mMarkerDao.insertAllMarkers((List<Marker>) dataList);
                break;
            case TYPE_OPEN_HOUR:
                saveRunnable = () -> mOpenHourDao.insertAllOpenHours((List<OpenHour>) dataList);
                break;
            case TYPE_PAGE:
                saveRunnable = () -> mPageDao.insertAllPages((List<Page>) dataList);
                break;
        }

        mAppExecutors.diskIO().execute(saveRunnable);
    }

    /**
     * Delete data from the table.
     *
     * @param dataType of fetched data, it's necessary to trigger relevant delete statement
     */
    @Override
    public void clearTableData(String dataType) {
        Runnable deleteRunnable = null;

        switch (dataType) {
            case TYPE_MENU:
                deleteRunnable = () -> mMenuDao.deleteAllMenu();
                break;
            case TYPE_PRODUCT:
                deleteRunnable = () -> mProductDao.deleteAllProducts();
                break;
            case TYPE_COUPON:
                deleteRunnable = () -> mCouponDao.deleteAllCoupons();
                break;
            case TYPE_MARKER:
                deleteRunnable = () -> mMarkerDao.deleteAllMarkers();
                break;
            case TYPE_OPEN_HOUR:
                deleteRunnable = () -> mOpenHourDao.deleteAllOpenHours();
                break;
            case TYPE_PAGE:
                deleteRunnable = () -> mPageDao.deleteAllPages();
                break;
        }

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    /**
     * Delete data when app is killed and should be fetched from server again.
     */
    @Override
    public void clearData() {
        Runnable deleteRunnable = () -> mCouponDao.deleteAllCoupons();

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
