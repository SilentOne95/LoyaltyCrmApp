package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.data.map.Friday;
import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.data.map.Monday;
import com.example.konta.sketch_loyalityapp.data.map.OpenHours;
import com.example.konta.sketch_loyalityapp.data.map.Saturday;
import com.example.konta.sketch_loyalityapp.data.map.Sunday;
import com.example.konta.sketch_loyalityapp.data.map.Thursday;
import com.example.konta.sketch_loyalityapp.data.map.Tuesday;
import com.example.konta.sketch_loyalityapp.data.map.Wednesday;
import com.example.konta.sketch_loyalityapp.ui.map.MapContract;
import com.example.konta.sketch_loyalityapp.ui.map.MapPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter,
        BaseCallbackListener.ListItemsOnFinishListener<Marker> {

    @Nullable
    private BottomSheetContract.OpeningHoursView view;
    private MapContract.Model model;

    private static final String TAG = "OpeningHoursFragment";
    private SparseArray<String> list = new SparseArray<>();

    OpeningHoursPresenter(@Nullable BottomSheetContract.OpeningHoursView view, MapContract.Model model) {
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
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        observable.subscribe(observer);
    }

    @Override
    public void formatOpenHoursData(List<Marker> markerList) {
        OpenHours open;
        String openHour, openMinute, closeHour, closeMinute, day;
        int index = 0;

        // For the test purpose get static value
        Marker marker = markerList.get(1);
        open = marker.getOpenHours();

        // TODO: Simplify
        if (open.getMonday() != null) {
            Monday monday = open.getMonday();

            openHour = monday.getOpenHour();
            openMinute = monday.getOpenMinute();
            closeHour = monday.getCloseHour();
            closeMinute = monday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;
        } else {
            day = "Closed";
            list.append(index, day);
            index++;
        }

        if (open.getTuesday() != null) {
            Tuesday tuesday = open.getTuesday();

            openHour = tuesday.getOpenHour();
            openMinute = tuesday.getOpenMinute();
            closeHour = tuesday.getCloseHour();
            closeMinute = tuesday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;

        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
            index++;
        }

        if (open.getWednesday() != null) {
            Wednesday wednesday = open.getWednesday();

            openHour = wednesday.getOpenHour();
            openMinute = wednesday.getOpenMinute();
            closeHour = wednesday.getCloseHour();
            closeMinute = wednesday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;

        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
            index++;
        }

        if (open.getThursday() != null) {
            Thursday thursday = open.getThursday();

            openHour = thursday.getOpenHour();
            openMinute = thursday.getOpenMinute();
            closeHour = thursday.getCloseHour();
            closeMinute = thursday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;

        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
            index++;
        }

        if (open.getFriday() != null) {
            Friday friday = open.getFriday();

            openHour = friday.getOpenHour();
            openMinute = friday.getOpenMinute();
            closeHour = friday.getCloseHour();
            closeMinute = friday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;
        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
            index++;
        }

        if (open.getSaturday() != null) {
            Saturday saturday = open.getSaturday();

            openHour = saturday.getOpenHour();
            openMinute = saturday.getOpenMinute();
            closeHour = saturday.getCloseHour();
            closeMinute = saturday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);
            index++;

        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
            index++;
        }

        if (open.getSunday() != null) {
            Sunday sunday = open.getSunday();

            openHour = sunday.getOpenHour();
            openMinute = sunday.getOpenMinute();
            closeHour = sunday.getCloseHour();
            closeMinute = sunday.getCloseMinute();

            day = checkIfOpenHoursAreValid(openHour, openMinute, closeHour, closeMinute);
            list.append(index, day);

        } else {
            day = checkIfOpenHoursAreValid("", "", "", "");
            list.append(index, day);
        }

        if (view != null) {
            view.setUpViewsWithData(list);
        }
    }

    @Override
    public String checkIfOpenHoursAreValid(String openHour, String openMinute, String closeHour, String closeMinute) {
        String day;

        if (openHour.isEmpty() || openHour.equals("None")) {
            day = "Closed";
        } else if (openHour.equals(closeHour)) {
            day = "Open 24/7";
        } else {
            day = openHour + ":" + openMinute + " - " + closeHour + ":" + closeMinute;
        }
        return day;
    }

    @Override
    public void fetchData() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Marker> listOfItems) {
        formatOpenHoursData(listOfItems);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
