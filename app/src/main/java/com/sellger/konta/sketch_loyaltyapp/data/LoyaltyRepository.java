package com.sellger.konta.sketch_loyaltyapp.data;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoyaltyRepository implements LoyaltyDataSource {

    private static LoyaltyRepository INSTANCE = null;

    private final LoyaltyDataSource mLoyaltyRemoteDataSource;
    private final LoyaltyDataSource mLoyaltyLocalDataSource;

    /**
     * Cached data maps.
     */
    private Map<String, MenuComponent> mCachedMenu;
    private Map<String, Product> mCachedProduct;
    private Map<String, Coupon> mCachedCoupon;
    private Map<String, Marker> mCachedMarker;
    private Map<String, OpenHour> mCachedOpenHour;
    private Map<String, Page> mCachedPage;

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
            // TODO:
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
                    // TODO:
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
            // TODO:
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
                    // TODO:
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
            // TODO:
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
                    // TODO:
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
            // TODO:
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
                    // TODO:
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
            // TODO:
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
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        }
    }

    /**
     * Remove local data to get new one next time when app is launched and clear disposable.
     */
    @Override
    public void deleteData() {
        mLoyaltyLocalDataSource.deleteData();
        mLoyaltyRemoteDataSource.deleteData();
    }

    /**
     * Remote data source.
     */
    private void getMenuFromRemoteDataSource(@NonNull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getMenu(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
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
                // TODO:
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
                callback.onDataLoaded(data);
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
                // TODO:
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
                callback.onDataLoaded(data);
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
                // TODO:
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
                callback.onDataLoaded(data);
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
                // TODO:
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
                callback.onDataLoaded(data);
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
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
