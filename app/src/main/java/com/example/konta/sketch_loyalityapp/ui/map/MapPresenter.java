package com.example.konta.sketch_loyalityapp.ui.map;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.pojo.map.OpenHour;
import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class MapPresenter implements MapContract.Presenter {

    @Nullable
    private MapContract.View view;
    private MapContract.Model model;

    private static final String TAG = "MapFragment";
    private List<Marker> markerList = null;
    private int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

    private static PublishSubject<Integer> markerIdSubject;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    MapPresenter(@Nullable MapContract.View view, MapContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void passDataToCluster(List<Marker> markerList) {
        this.markerList = markerList;
        if (view != null) {
            view.setUpCluster(markerList);
        }
    }

    @Override
    public void switchBottomSheetState(Object object) {

        if (view != null) {
            if (object instanceof Marker) {
                if (view.getBottomSheetState() == BottomSheetBehavior.STATE_HIDDEN) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                    new Handler().postDelayed(() -> view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED), 300);
                }
            } else if (object instanceof LatLng) {
                if (view.getBottomSheetState() != BottomSheetBehavior.STATE_HIDDEN) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                }
            } else if (object instanceof View) {
                if (view.getBottomSheetState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        }
    }

    @Override
    public void passDataToBottomSheet(int markerId) {
        markerIdSubject.onNext(markerId);
    }

    public static Observable<Integer> getObservable() {
        return markerIdSubject;
    }

    @Override
    public void setUpObservable() {
         markerIdSubject = PublishSubject.create();

        Observable<Integer> observable = getObservable();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer markerId) {
                Log.d(TAG, "onNext" + String.valueOf(markerId));
                findSelectedMarkerId(markerId);
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
    public void findSelectedMarkerId(int markerId) {
        int markerPosition = 0;

        for (int i = 0; i < markerList.size(); i++) {
            if (markerList.get(i).getId().equals(markerId)) {
                markerPosition = i;
                break;
            }
        }

        passDataToView(markerList.get(markerPosition));
    }

    @Override
    public void passDataToView(Marker marker) {
        String title, address, openHours;

        // TODO: Move this logic to model class
        String defaultString;
        String defaultTitle;
        if (view != null) {
            defaultTitle = view.getDefaultPlaceTitle();
            defaultString = view.getDefaultPlaceData();
        } else {
            defaultTitle = defaultString = "Unable to display";
        }
        title = defaultTitle;
        address = openHours = defaultString;

        if (!TextUtils.isEmpty(marker.getTitle())) {
            title = marker.getTitle();
        }

        if (!TextUtils.isEmpty(marker.getAddress()) && !TextUtils.isEmpty(marker.getPostCode())
                && !TextUtils.isEmpty(marker.getCity())) {
            address = marker.getAddress() + ", " +
                    marker.getPostCode() + " " +
                    marker.getCity();
        }

        if (marker.getOpenHours() != null && marker.getOpenHours().get(0).getDayName() != null) {
            for (int i = 0; i < marker.getOpenHours().size(); i++) {
                if (marker.getOpenHours().get(i).getDayName().equals(getCurrentDay())) {
                    OpenHour obj = marker.getOpenHours().get(i);
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

        if (view != null) {
            view.setUpBottomSheetPanelWithData(title, address, openHours);
        }
    }

    @Override
    public String getCurrentDay() {
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
