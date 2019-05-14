package com.sellger.konta.sketch_loyaltyapp.data.remote;

import android.support.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Concrete implementation of a data source as a network connection.
 */
public class LoyaltyRemoteDataSource implements LoyaltyDataSource {

    private static LoyaltyRemoteDataSource INSTANCE;

    private Api api = RetrofitClient.getInstance().create(Api.class);
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
        disposable.add(api
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getAllProducts(@NonNull LoadDataCallback callback) {
        disposable.add(api
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getSingleProduct(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(api
                .getSingleProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getAllCoupons(@NonNull LoadDataCallback callback) {
        disposable.add(api
                .getAllCoupons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getSingleCoupon(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(api
                .getSingleCoupon(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getAllMarkers(@NonNull LoadDataCallback callback) {
        disposable.add(api
                .getAllMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getSingleMarker(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(api
                .getSingleMarker(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getAllOpenHours(@NonNull LoadDataCallback callback) {
        // This data is nested in Marker's info, so it's necessary to fetch and refactor Markers data
        disposable.add(api
                .getAllMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getSingleOpenHour(int id, @NonNull GetSingleDataCallback callback) {
        // This data is nested in Marker's info, so it's necessary to fetch and refactor it's data
        disposable.add(api
                .getSingleMarker(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getAllPages(@NonNull LoadDataCallback callback) {
        disposable.add(api
                .getAllPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void getSinglePage(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(api
                .getStaticPage(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded));
    }

    @Override
    public void saveData(String dataType, @NonNull List<?> dataList) {
        // Not required for the remote data source, because there is no option for saving data
        // anywhere besides SQLite DB using {@link LoyaltyLocalDataSource}
    }

    @Override
    public void clearTableData(String dataType) {
        // Not required for the remote data source, because there is no option for refreshing data
        // anywhere besides SQLite DB using {@link LoyaltyLocalDataSource}
    }

    /**
     * Clear disposable.
     */
    @Override
    public void clearData() {
        disposable.clear();
    }
}
