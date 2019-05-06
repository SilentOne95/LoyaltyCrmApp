package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.text.TextUtils;

import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.contactInfo.ContactInfoPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours.OpeningHoursPresenter;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MapModel implements MapContract.Model {

    private MapPresenter presenter;
    private OpeningHoursPresenter openingHoursPresenter;
    private ContactInfoPresenter contactInfoPresenter;

    private List<Marker> listOfMarkers = null;
    private int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


    @Override
    public Disposable fetchDataFromServer(MapPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserver());
    }

    private Single<List<Marker>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllMarkers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<List<Marker>> getObserver() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                listOfMarkers = markerList;
                presenter.passDataToCluster(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    private DisposableSingleObserver<List<Marker>> getObserverOpening() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                openingHoursPresenter.formatOpenHoursData(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    private DisposableSingleObserver<List<Marker>> getObserverContact() {
        return new DisposableSingleObserver<List<Marker>>() {
            @Override
            public void onSuccess(List<Marker> markerList) {
                contactInfoPresenter.formatContactInfoData(markerList);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    @Override
    public Disposable fetchDataFromServer(OpeningHoursPresenter openingHoursPresenter) {
        this.openingHoursPresenter = openingHoursPresenter;
        return getObservable().subscribeWith(getObserverOpening());
    }

    @Override
    public Disposable fetchDataFromServer(ContactInfoPresenter contactInfoPresenter) {
        this.contactInfoPresenter = contactInfoPresenter;
        return getObservable().subscribeWith(getObserverContact());
    }

    @Override
    public void getMarkerData(int markerId) {
        int markerPosition = 0;

        for (int i = 0; i < listOfMarkers.size(); i++) {
            if (listOfMarkers.get(i).getId().equals(markerId)) {
                markerPosition = i;
                break;
            }
        }

        Marker selectedMarker = listOfMarkers.get(markerPosition);

        String title, address, openHours;
        title = address = openHours = "Unable to display";

        if (!TextUtils.isEmpty(selectedMarker.getTitle())) {
            title = selectedMarker.getTitle();
        }

        if (!TextUtils.isEmpty(selectedMarker.getAddress()) && !TextUtils.isEmpty(selectedMarker.getPostCode())
                && !TextUtils.isEmpty(selectedMarker.getCity())) {
            address = selectedMarker.getAddress() + ", " +
                    selectedMarker.getPostCode() + " " +
                    selectedMarker.getCity();
        }

        if (selectedMarker.getOpenHours() != null && selectedMarker.getOpenHours().get(0).getDayName() != null) {
            for (int i = 0; i < selectedMarker.getOpenHours().size(); i++) {
                if (selectedMarker.getOpenHours().get(i).getDayName().equals(getCurrentDay())) {
                    OpenHour obj = selectedMarker.getOpenHours().get(i);
                    if (!obj.getOpenHour().equals("None") && !obj.getOpenMinute().equals("None") &&
                            !obj.getCloseHour().equals("None") && !obj.getCloseMinute().equals("None")){
                        openHours = "Today open:" + " " +
                                obj.getOpenHour() + ":" +
                                obj.getOpenMinute() + " - " +
                                obj.getCloseHour() + ":" +
                                obj.getCloseMinute();
                    } else if (obj.getOpenHour().equals(obj.getCloseHour()) &&
                            obj.getOpenMinute().equals(obj.getCloseMinute()) &&
                            !obj.getOpenHour().equals("None")) {
                        openHours = "All day";
                    } else {
                        openHours = "Closed";
                    }

                    break;
                }
            }
        }

        presenter.passDataToView(title, address, openHours);
    }

    private String getCurrentDay() {
        String day;

        switch (currentDay){
            case 1:
                day = "sunday";
                break;
            case 2:
                day = "monday";
                break;
            case 3:
                day = "tuesday";
                break;
            case 4:
                day = "wednesday";
                break;
            case 5:
                day = "thursday";
                break;
            case 6:
                day = "friday";
                break;
            case 7:
                day = "saturday";
                break;

            default:
                day = "";
                break;
        }

        return day;
    }
}