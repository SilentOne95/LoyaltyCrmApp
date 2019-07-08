package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.ui.map.GoogleMapFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.sellger.konta.sketch_loyaltyapp.Constants.CLOSED_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.ERROR_NONE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FRIDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MONDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.OPEN_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SATURDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SUNDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THURSDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TUESDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.WEDNESDAY_STRING;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter {

    private static final String TAG = OpeningHoursPresenter.class.getSimpleName();

    @NonNull
    private BottomSheetContract.OpeningHoursView view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    OpeningHoursPresenter(@NonNull BottomSheetContract.OpeningHoursView view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link OpeningHoursFragment#onViewCreated(View, Bundle)} to set up Observable which
     * listen which item was clicked in {@link GoogleMapFragment} and then pass selected {@link Marker} object.
     */
    @Override
    public void setUpObservable() {
        Observable<Integer> observable = MapPresenter.getObservable();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer markerId) {
                getSelectedMarker(markerId);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        observable.subscribe(observer);
    }

    /**
     * Called from {@link #setUpObservable()} to fetch necessary data about selected marker.
     *
     * @param markerId of selected marker
     */
    private void getSelectedMarker(int markerId) {
        loyaltyRepository.getSingleMarker(markerId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                formatMarkerData((Marker) object);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link #getSelectedMarker(int)} to get needed data from {@link Marker} object.
     *
     * @param marker object
     */
    private void formatMarkerData(Marker marker) {
        String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        monday = tuesday = wednesday = thursday = friday = saturday = sunday = CLOSED_STRING;
        String[] days;

        List<OpenHour> openHourList = marker.getOpenHourList();

        for (OpenHour time : openHourList) {
            if (!TextUtils.isEmpty(time.getDayName())) {
                switch (time.getDayName()) {
                    case MONDAY_STRING:
                        monday = checkIfOpenHoursAreValid(time);
                        break;
                    case TUESDAY_STRING:
                        tuesday = checkIfOpenHoursAreValid(time);
                        break;
                    case WEDNESDAY_STRING:
                        wednesday = checkIfOpenHoursAreValid(time);
                        break;
                    case THURSDAY_STRING:
                        thursday = checkIfOpenHoursAreValid(time);
                        break;
                    case FRIDAY_STRING:
                        friday = checkIfOpenHoursAreValid(time);
                        break;
                    case SATURDAY_STRING:
                        saturday = checkIfOpenHoursAreValid(time);
                        break;
                    case SUNDAY_STRING:
                        sunday = checkIfOpenHoursAreValid(time);
                        break;
                }
            }
        }

        days = new String[]{monday, tuesday, wednesday, thursday, friday, saturday, sunday};

        passDataToView(days);
    }

    /**
     * Called from {@link #formatMarkerData(Marker)} to check if opening hours data is valid.
     *
     * @param time is {@link OpenHour} object
     * @return string that contains opening hours for a single day
     */
    private String checkIfOpenHoursAreValid(OpenHour time) {
        String day;

        if (time.getOpenHour().isEmpty() || time.getOpenHour().equals(ERROR_NONE_STRING)) {
            day = CLOSED_STRING;
        } else if (time.getOpenHour().equals(time.getCloseHour())) {
            day = OPEN_STRING;
        } else {
            day = time.getOpenHour() + ":" + time.getOpenMinute() + " - " +
                    time.getCloseHour() + ":" + time.getCloseMinute();
        }

        return day;
    }

    /**
     * Called from {@link #formatMarkerData(Marker)} to pass prepared data to view.
     *
     * @param singleDayOpenHours list of strings retrieved from {@link Marker} object
     */
    private void passDataToView(String[] singleDayOpenHours) {
        view.setUpViewsWithData(singleDayOpenHours);
    }
}
