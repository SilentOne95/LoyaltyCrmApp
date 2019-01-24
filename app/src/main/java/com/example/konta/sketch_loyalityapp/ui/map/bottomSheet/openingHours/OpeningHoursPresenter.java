package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;
import android.util.Log;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.pojo.map.OpenHour;
import com.example.konta.sketch_loyalityapp.ui.map.MapModel;
import com.example.konta.sketch_loyalityapp.ui.map.MapPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter{

    @Nullable
    private BottomSheetContract.OpeningHoursView view;
    private MapModel model;

    private static final String TAG = "OpeningHoursFragment";

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
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() { Log.d(TAG, "onComplete"); }
        };

        observable.subscribe(observer);
    }

    @Override
    public void formatOpenHoursData(List<Marker> markerList) {
        String monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        monday = tuesday = wednesday = thursday = friday = saturday = sunday = "Closed";
        String[] days;

        Marker marker = markerList.get(1);
        List<OpenHour> openHourList = marker.getOpenHours();

        for (OpenHour time : openHourList) {
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
