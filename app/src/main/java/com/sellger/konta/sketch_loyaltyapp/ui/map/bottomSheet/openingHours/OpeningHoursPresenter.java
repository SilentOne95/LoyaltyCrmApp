package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapModel;
import com.sellger.konta.sketch_loyaltyapp.ui.map.MapPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter{

    @Nullable
    private BottomSheetContract.OpeningHoursView view;
    private MapModel model;

    private static final String TAG = "OpeningHoursFragment";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int selectedMarkerId;

    OpeningHoursPresenter(@Nullable BottomSheetContract.OpeningHoursView view, MapModel model) {
        this.view = view;
        this.model = model;
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
                Log.d(TAG, "onNext" + String.valueOf(markerId));
                selectedMarkerId = markerId;
                getMarkerList();
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
    public void getMarkerList() {
       Disposable disposable = model.fetchDataFromServer(this);
       compositeDisposable.add(disposable);
    }

    @Override
    public void formatOpenHoursData(List<Marker> markerList) {
        String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        monday = tuesday = wednesday = thursday = friday = saturday = sunday = "Closed";
        String[] days;
        int markerPosition = 0;

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getId().equals(selectedMarkerId)) {
                markerPosition = i;
                break;
            }
        }

        Marker marker = markerList.get(markerPosition);
        List<OpenHour> openHourList = marker.getOpenHours();

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

        if (view != null) {
            view.setUpViewsWithData(days);
        }
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
}
