package com.sellger.konta.sketch_loyaltyapp.data.remote;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_FETCHING_DATA;

/**
 * Concrete implementation of a data source as a network connection.
 */
public class LoyaltyRemoteDataSource implements LoyaltyDataSource {

    private static LoyaltyRemoteDataSource INSTANCE;

    private Api api = RetrofitClient.getApi();
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
     * Zip with timer all requests to display loading indicator at least for 1 sec to prevent from
     * undesirable blink effect.
     *
     * @param observable data request
     * @param <T> generic type of parameter is going to be passed
     * @return Single observable zipped with timer - disposable
     */
    private static <T> Single<T> zipWithTimer(Single<T> observable) {
        return Single.zip(observable, Single.timer(DELAY_FETCHING_DATA, TimeUnit.MILLISECONDS),
                (data, timerValue) -> data);
    }

    /**
     * Get all the data from server.
     *
     * Note: {@link LoadDataCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getMenu(@NonNull LoadDataCallback callback) {
        disposable.add(zipWithTimer(api.getMenuComponents())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getAllProducts(@NonNull LoadDataCallback callback) {
        disposable.add(zipWithTimer(api.getAllProducts())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getSingleProduct(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(zipWithTimer(api.getSingleProduct(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getAllCoupons(@NonNull LoadDataCallback callback) {
        disposable.add(zipWithTimer(api.getAllCoupons())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getSingleCoupon(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(zipWithTimer(api.getSingleCoupon(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getAllMarkers(@NonNull LoadDataCallback callback) {
        disposable.add(zipWithTimer(api.getAllMarkers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getSingleMarker(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(zipWithTimer(api.getSingleMarker(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getCursorMarker(String providedText, @NonNull GetSingleDataCallback callback) {
        // Not required for the remote data source, because there is no option for retrieving Cursor data
        // anywhere besides SQLite DB using {@link LoyaltyLocalDataSource}
    }

    @Override
    public void getAllOpenHours(@NonNull LoadDataCallback callback) {
        // This data is nested in Marker's info, so it's necessary to fetch and refactor Markers data
        disposable.add(zipWithTimer(api.getAllMarkers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getSingleOpenHour(int id, @NonNull GetSingleDataCallback callback) {
        // This data is nested in Marker's info, so it's necessary to fetch and refactor it's data
        disposable.add(zipWithTimer(api.getSingleMarker(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getAllPages(@NonNull LoadDataCallback callback) {
        disposable.add(zipWithTimer(api.getAllPages())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
    }

    @Override
    public void getSinglePage(int id, @NonNull GetSingleDataCallback callback) {
        disposable.add(zipWithTimer(api.getSinglePage(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onDataLoaded, throwable -> callback.onDataNotAvailable()));
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
