package com.sellger.konta.sketch_loyaltyapp.data;

import static com.google.common.base.Preconditions.checkNotNull;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.data.local.TestDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class LoyaltyRepository implements LoyaltyDataSource {

    private static LoyaltyRepository INSTANCE = null;

    private final LoyaltyDataSource mLoyaltyRemoteDataSource;
    private final LoyaltyDataSource mLoyaltyLocalDataSource;
    private TestDao mTestDao;

    /**
     * Those variables has package local visibility so it can be accessed from tests.
     */
    Map<String, MenuComponent> mCachedMenu;
    Map<String, Product> mCachedProduct;
    Map<String, Coupon> mCachedCoupon;
    Map<String, Marker> mCachedMarker;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheMenuIsDirty = false;
    boolean mCacheProductIsDirty = false;
    boolean mCacheCouponIsDirty = false;
    boolean mCacheMarkerIsDirty = false;

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
            // TODO:
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getMenu(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
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
            // TODO:
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllProducts(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            });
        }
    }

    @Override
    public void getSingleProduct(@NonNull LoadDataCallback callback, int id) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedProduct != null && !mCacheProductIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedProduct.values()));
            return;
        }

        if (mCacheProductIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            // TODO:
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleProduct(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            }, id);
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
            // TODO:
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllCoupons(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            });
        }
    }

    @Override
    public void getSingleCoupon(@NonNull LoadDataCallback callback, int id) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedCoupon != null && !mCacheCouponIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedCoupon.values()));
            return;
        }

        if (mCacheCouponIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            // TODO:
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleCoupon(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            }, id);
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
            // TODO: Call necessary method
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getAllMarkers(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            });
        }
    }

    @Override
    public void getSingleMarker(@NonNull LoadDataCallback callback, int id) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedMarker != null && !mCacheMarkerIsDirty) {
            callback.onDataLoaded(new ArrayList<>(mCachedMarker.values()));
            return;
        }

        if (mCacheMarkerIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            // TODO: Call necessary method
        } else {
            // Query the local storage if available, but if not, query the network
            mLoyaltyLocalDataSource.getSingleMarker(new LoadDataCallback() {
                @Override
                public void onDataLoaded(List<?> data) {
                    // TODO:
                }

                @Override
                public void onDataNotAvailable() {
                    // TODO:
                }
            }, id);
        }
    }

    @Override
    public void getStaticPage(@NonNull final LoadDataCallback callback, int id) {
        checkNotNull(callback);

        // Query the local storage if available, but if not, query the network
        mLoyaltyLocalDataSource.getStaticPage(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        }, id);
    }

    /**
     * Remote data source.
     */
    private void getMenuFromRemoteDataSource(@Nonnull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getMenu(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        });
    }

    private void getProductsFromRemoteDataSource(@Nonnull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllProducts(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        });
    }

    private void getSingleProductFromRemoteDataSource(@Nonnull final LoadDataCallback callback, int id) {
        mLoyaltyRemoteDataSource.getSingleProduct(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        }, id);
    }

    private void getCouponsFromRemoteDataSource(@Nonnull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllCoupons(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        });
    }

    private void getSingleCouponFromRemoteDataSource(@Nonnull final LoadDataCallback callback, int id) {
        mLoyaltyRemoteDataSource.getSingleProduct(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        }, id);
    }

    private void getMarkersFromRemoteDataSource(@Nonnull final LoadDataCallback callback) {
        mLoyaltyRemoteDataSource.getAllMarkers(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        });
    }

    private void getSingleMarkerFromRemoteDataSource(@Nonnull final LoadDataCallback callback, int id) {
        mLoyaltyRemoteDataSource.getSingleMarker(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        }, id);
    }

    private void getPageFromRemoteDataSource(@Nonnull final LoadDataCallback callback, int id) {
        mLoyaltyRemoteDataSource.getStaticPage(new LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                // TODO:
            }

            @Override
            public void onDataNotAvailable() {
                // TODO:
            }
        }, id);
    }
}
