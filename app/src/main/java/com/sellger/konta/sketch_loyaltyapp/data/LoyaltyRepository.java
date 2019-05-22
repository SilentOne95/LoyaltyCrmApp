package com.sellger.konta.sketch_loyaltyapp.data;

import androidx.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_COUPON;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_MARKER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_MENU;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_OPEN_HOUR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_PAGE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TYPE_PRODUCT;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.data.local.LoyaltyLocalDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.remote.LoyaltyRemoteDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoyaltyRepository implements LoyaltyDataSource {

    private static LoyaltyRepository INSTANCE = null;

    private final LoyaltyDataSource mLoyaltyRemoteDataSource;
    private final LoyaltyDataSource mLoyaltyLocalDataSource;

    /**
     * Cached data maps.
     */
    private Map<Integer, MenuComponent> mCachedMenu;
    private Map<Integer, Product> mCachedProduct;
    private Map<Integer, Coupon> mCachedCoupon;
    private Map<Integer, Marker> mCachedMarker;
    private Map<Integer, OpenHour> mCachedOpenHour;
    private Map<Integer, Page> mCachedPage;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested.
     */
    private boolean mCacheMenuIsDirty = true;
    private boolean mCacheProductIsDirty = true;
    private boolean mCacheCouponIsDirty = true;
    private boolean mCacheMarkerIsDirty = true;
    private boolean mCacheOpenHourIsDirty = true;
    private boolean mCachePageIsDirty = true;

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

    /**
     * Gets tasks from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     * Note: {@link LoadDataCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getMenu(@NonNull final LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedMenu != null && !mCacheMenuIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedMenu.values()));
            return;
        }

        if (mCacheMenuIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getMenuFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getMenu(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_MENU, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedMenu.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getMenuFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getAllProducts(@NonNull final LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedProduct != null && !mCacheProductIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedProduct.values()));
            return;
        }

        if (mCacheProductIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getProductsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllProducts(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_PRODUCT, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedProduct.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getSingleProduct(int id, @NonNull GetSingleDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedProduct != null && !mCacheProductIsDirty) {
            callback.onDataLoaded(mCachedProduct.get(id));
            return;
        }

        if (mCacheProductIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getSingleProductFromRemoteDataSource(id, callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleProduct(id, new GetSingleDataCallback() {
                @Override
                public void onDataLoaded(Object object) {
                    callback.onDataLoaded(object);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getAllCoupons(@NonNull final LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedCoupon != null && !mCacheCouponIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedCoupon.values()));
            return;
        }

        if (mCacheCouponIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getCouponsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllCoupons(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_COUPON, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedCoupon.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getSingleCoupon(int id, @NonNull GetSingleDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedCoupon != null && !mCacheCouponIsDirty) {
            callback.onDataLoaded(mCachedCoupon.get(id));
            return;
        }

        if (mCacheCouponIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getSingleCouponFromRemoteDataSource(id, callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleCoupon(id, new GetSingleDataCallback() {
                @Override
                public void onDataLoaded(Object object) {
                    callback.onDataLoaded(object);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getAllMarkers(@NonNull final LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedMarker != null && !mCacheMarkerIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedMarker.values()));
            return;
        }

        if (mCacheMarkerIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getMarkersFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllMarkers(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_MARKER, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedMarker.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getSingleMarker(int id, @NonNull GetSingleDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedMarker != null && !mCacheMarkerIsDirty) {
            callback.onDataLoaded(mCachedMarker.get(id));
            return;
        }

        if (mCacheMarkerIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getSingleMarkerFromRemoteDataSource(id, callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleMarker(id, new GetSingleDataCallback() {
                @Override
                public void onDataLoaded(Object object) {
                    callback.onDataLoaded(object);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getAllOpenHours(@NonNull LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedOpenHour != null && !mCacheOpenHourIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedOpenHour.values()));
            return;
        }

        if (mCacheOpenHourIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getOpenHoursFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllOpenHours(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_OPEN_HOUR, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedOpenHour.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getSingleOpenHour(int id, @NonNull GetSingleDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedOpenHour != null && !mCacheOpenHourIsDirty) {
            callback.onDataLoaded(mCachedOpenHour.get(id));
            return;
        }

        if (mCacheOpenHourIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getSingleOpenHourFromRemoteDataSource(id, callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleOpenHour(id, new GetSingleDataCallback() {
                @Override
                public void onDataLoaded(Object object) {
                    callback.onDataLoaded(object);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getAllPages(@NonNull LoadDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedPage != null && !mCachePageIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedPage.values()));
            return;
        }

        if (mCachePageIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getPagesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllPages(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    refreshCache(TYPE_PAGE, data);
                    callback.onDataLoaded(new ArrayList<>(mCachedPage.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    @Override
    public void getSinglePage(int id, @NonNull GetSingleDataCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedPage != null && !mCachePageIsDirty) {
            callback.onDataLoaded(mCachedPage.get(id));
            return;
        }

        if (mCachePageIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getSinglePageFromRemoteDataSource(id, callback);
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSinglePage(id, new GetSingleDataCallback() {
                @Override
                public void onDataLoaded(Object object) {
                    callback.onDataLoaded(object);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    /**
     * Pass fetched data from {@link LoyaltyRemoteDataSource} to {@link LoyaltyLocalDataSource}
     * to let store these in SQLite db.
     *
     * @param dataType of fetched data, it's necessary to trigger relevant insert statement
     * @param dataList of data, which is going to be stored in local db
     */
    @Override
    public void saveData(String dataType, @NonNull List<?> dataList) {
        checkNotNull(dataList);

        // Clear local data before storing new one to ensure storing list up to date
        clearTableData(dataType);
        mLoyaltyLocalDataSource.saveData(dataType, dataList);
    }

    @Override
    public void clearTableData(String dataType) {
        mLoyaltyLocalDataSource.clearTableData(dataType);
    }

    /**
     * Remove local data to get new one next time when app is launched and clear disposable.
     */
    @Override
    public void clearData() {
        mLoyaltyLocalDataSource.clearData();
        mLoyaltyRemoteDataSource.clearData();
    }

    /**
     * Remote data source.
     */
    private void getMenuFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getMenu(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_MENU, data);
                saveData(TYPE_MENU, data);
                callback.onDataLoaded(new ArrayList<>(mCachedMenu.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getProductsFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllProducts(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_PRODUCT, data);
                saveData(TYPE_PRODUCT, data);
                callback.onDataLoaded(new ArrayList<>(mCachedProduct.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getSingleProductFromRemoteDataSource(int id, @NonNull GetSingleDataCallback callback) {
        mLoyaltyRemoteDataSource.getSingleProduct(id, new GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                callback.onDataLoaded(object);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getCouponsFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllCoupons(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_COUPON, data);
                saveData(TYPE_COUPON, data);
                callback.onDataLoaded(new ArrayList<>(mCachedCoupon.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getSingleCouponFromRemoteDataSource(int id, @NonNull GetSingleDataCallback callback) {
        mLoyaltyRemoteDataSource.getSingleProduct(id, new GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                callback.onDataLoaded(object);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getMarkersFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllMarkers(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_MARKER, data);
                saveData(TYPE_MARKER, data);
                callback.onDataLoaded(new ArrayList<>(mCachedMarker.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getSingleMarkerFromRemoteDataSource(int id, @NonNull GetSingleDataCallback callback) {
        mLoyaltyRemoteDataSource.getSingleMarker(id, new GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                callback.onDataLoaded(object);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getOpenHoursFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllOpenHours(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_OPEN_HOUR, data);
                saveData(TYPE_OPEN_HOUR, data);
                callback.onDataLoaded(new ArrayList<>(mCachedOpenHour.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getSingleOpenHourFromRemoteDataSource(int id, @NonNull GetSingleDataCallback callback) {
        mLoyaltyRemoteDataSource.getSingleOpenHour(id, new GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                callback.onDataLoaded(object);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getPagesFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllPages(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refreshCache(TYPE_PAGE, data);
                saveData(TYPE_PAGE, data);
                callback.onDataLoaded(new ArrayList<>(mCachedPage.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void getSinglePageFromRemoteDataSource(int id, @NonNull GetSingleDataCallback callback) {
        mLoyaltyRemoteDataSource.getSinglePage(id, new GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                callback.onDataLoaded(object);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    /**
     * Store fetched data in cache to prevent triggering multiple SQLite statements.
     *
     * @param dataType of the passed dataList
     * @param dataList of data, which is going to be cached
     */
    private void refreshCache(String dataType, List<?> dataList) {
        switch (dataType) {
            case TYPE_MENU:
                if (mCachedMenu == null) {
                    mCachedMenu = new LinkedHashMap<>();
                }
                mCachedMenu.clear();
                for (Object menuComponent : dataList) {
                    mCachedMenu.put(((MenuComponent) menuComponent).getId(), ((MenuComponent) menuComponent));
                }
                mCacheMenuIsDirty = false;
                break;
            case TYPE_PRODUCT:
                if (mCachedProduct == null) {
                    mCachedProduct = new LinkedHashMap<>();
                }
                mCachedProduct.clear();
                for (Object product : dataList) {
                    mCachedProduct.put(((Product) product).getId(), ((Product) product));
                }
                mCacheProductIsDirty = false;
                break;
            case TYPE_COUPON:
                if (mCachedCoupon == null) {
                    mCachedCoupon = new LinkedHashMap<>();
                }
                mCachedCoupon.clear();
                for (Object coupon : dataList) {
                    mCachedCoupon.put(((Coupon) coupon).getId(), ((Coupon) coupon));
                }
                mCacheCouponIsDirty = false;
                break;
            case TYPE_MARKER:
                if (mCachedMarker == null) {
                    mCachedMarker = new LinkedHashMap<>();
                }
                mCachedMarker.clear();
                for (Object marker : dataList) {
                    mCachedMarker.put(((Marker) marker).getId(), ((Marker) marker));
                }
                mCacheMarkerIsDirty = false;
                break;
            case TYPE_OPEN_HOUR:
                if (mCachedOpenHour == null) {
                    mCachedOpenHour = new LinkedHashMap<>();
                }
                mCachedOpenHour.clear();
                for (Object openHour : dataList) {
                    mCachedOpenHour.put(((OpenHour) openHour).getId(), ((OpenHour) openHour));
                }
                mCacheOpenHourIsDirty = false;
                break;
            case TYPE_PAGE:
                if (mCachedPage == null) {
                    mCachedPage = new LinkedHashMap<>();
                }
                mCachedPage.clear();
                for (Object page : dataList) {
                    mCachedPage.put(((Page) page).getId(), ((Page) page));
                }
                mCachePageIsDirty = false;
                break;
        }
    }
}
