package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_DATA_ERROR_MESSAGE;

public class MapPresenter implements MapContract.Presenter {

    private static final String TAG = MapPresenter.class.getSimpleName();

    @NonNull
    private MapContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    private static PublishSubject<Integer> mMarkerIdSubject;
    private int mCurrentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


    MapPresenter(@NonNull MapContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    public static Observable<Integer> getObservable() {
        return mMarkerIdSubject;
    }

    @Override
    public void setUpObservable() {
        mMarkerIdSubject = PublishSubject.create();

        Observable<Integer> observable = getObservable();
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
    public void requestDataFromServer() {
        loyaltyRepository.getAllMarkers(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                passDataToCluster((List<Marker>) data);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_DATA_ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void passDataToCluster(List<Marker> markerList) {
        view.setUpCluster(markerList);
    }

    @Override
    public void switchBottomSheetState(Object object) {
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

    @Override
    public void passClickedMarkerId(int markerId) {
        mMarkerIdSubject.onNext(markerId);
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
        List<OpenHour> openHourList = marker.getOpenHourList();

        String title, address, openHours;
        title = address = openHours = "Unable to display";

        if (!TextUtils.isEmpty(marker.getTitle())) {
            title = marker.getTitle();
        }

        if (!TextUtils.isEmpty(marker.getAddress()) && !TextUtils.isEmpty(marker.getPostalCode())
                && !TextUtils.isEmpty(marker.getCity())) {
            address = marker.getAddress() + ", " +
                    marker.getPostalCode() + " " +
                    marker.getCity();
        }

        if (openHourList != null) {
            for (OpenHour openHour : openHourList) {
                if (openHour.getDayName().equals(getCurrentDay())) {
                    if (!openHour.getOpenHour().equals("None") && !openHour.getOpenMinute().equals("None") &&
                            !openHour.getCloseHour().equals("None") && !openHour.getCloseMinute().equals("None")) {
                        openHours = "Today open:" + " " +
                                openHour.getOpenHour() + ":" +
                                openHour.getOpenMinute() + " - " +
                                openHour.getCloseHour() + ":" +
                                openHour.getCloseMinute();
                    } else if (openHour.getOpenHour().equals(openHour.getCloseHour()) &&
                            openHour.getOpenMinute().equals(openHour.getCloseMinute()) &&
                            !openHour.getOpenHour().equals("None")) {
                        openHours = "All day";
                    } else {
                        openHours = "Closed";
                    }

                    break;
                }
            }
        } else {
            // TODO: Handle null array
        }

        passDataToView(title, address, openHours);
    }

    @Override
    public String getCurrentDay() {
        String day = "";

        switch (mCurrentDay) {
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
        }

        return day;
    }

    @Override
    public void passDataToView(String title, String address, String openHours) {
        view.setUpBottomSheetPanelWithData(title, address, openHours);
    }
}
