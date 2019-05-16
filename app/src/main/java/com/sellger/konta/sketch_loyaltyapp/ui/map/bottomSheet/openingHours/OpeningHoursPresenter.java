package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    @Override
    public void setUpObservable() {
        Observable<Integer> observable = MapPresenter.getObservable();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer markerId) {
                Log.d(TAG, "onNext" + markerId);
                getSelectedMarker(markerId);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        observable.subscribe(observer);
    }

    @Override
    public void getSelectedMarker(int markerId) {
        loyaltyRepository.getSingleMarker(markerId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                formatMarkerData((Marker) object);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void formatMarkerData(Marker marker) {
        String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        monday = tuesday = wednesday = thursday = friday = saturday = sunday = "Closed";
        String[] days;

        List<OpenHour> openHourList = marker.getOpenHourList();

        for (OpenHour time : openHourList) {
            if (!TextUtils.isEmpty(time.getDayName())) {
                switch (time.getDayName()) {
                    case "monday":
                        monday = checkIfOpenHoursAreValid(time);
                        break;
                    case "tuesday":
                        tuesday = checkIfOpenHoursAreValid(time);
                        break;
                    case "wednesday":
                        wednesday = checkIfOpenHoursAreValid(time);
                        break;
                    case "thursday":
                        thursday = checkIfOpenHoursAreValid(time);
                        break;
                    case "friday":
                        friday = checkIfOpenHoursAreValid(time);
                        break;
                    case "saturday":
                        saturday = checkIfOpenHoursAreValid(time);
                        break;
                    case "sunday":
                        sunday = checkIfOpenHoursAreValid(time);
                        break;
                }
            }
        }

        days = new String[] {monday, tuesday, wednesday, thursday, friday, saturday, sunday};

        passDataToView(days);
    }

    @Override
    public String checkIfOpenHoursAreValid(OpenHour time) {
        String day;

        if (time.getOpenHour().isEmpty() || time.getOpenHour().equals("None")) {
            day = "Closed";
        } else if (time.getOpenHour().equals(time.getCloseHour())) {
            day = "Open 24/7";
        } else {
            day = time.getOpenHour() + ":" + time.getOpenMinute() + " - " +
                    time.getCloseHour() + ":" + time.getCloseMinute();
        }

        return day;
    }

    @Override
    public void passDataToView(String[] singleDayOpenHours) {
        view.setUpViewsWithData(singleDayOpenHours);
    }
}
