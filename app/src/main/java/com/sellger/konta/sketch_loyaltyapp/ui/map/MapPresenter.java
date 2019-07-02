package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import android.text.TextUtils;
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

import static com.sellger.konta.sketch_loyaltyapp.Constants.ALL_DAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.CLOSED_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_UNABLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_BOTTOMSHEET_ACTION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.ERROR_NONE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FRIDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MONDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SATURDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.SUNDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.THURSDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TODAY_OPEN_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TUESDAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.WEDNESDAY_STRING;

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

    /**
     * Called from {@link GoogleMapFragment#onViewCreated(View, Bundle)} to set up Observable which
     * listen which item was clicked in {@link GoogleMapFragment} and then pass selected {@link Marker} object.
     */
    @Override
    public void setUpObservable() {
        mMarkerIdSubject = PublishSubject.create();

        Observable<Integer> observable = getObservable();
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

    public static Observable<Integer> getObservable() {
        return mMarkerIdSubject;
    }

    /**
     * Called from {@link GoogleMapFragment#onViewCreated(View, Bundle)} to fetch required data from {@link LoyaltyRepository}.
     */
    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getAllMarkers(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                passDataToCluster((List<Marker>) data);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link #requestDataFromServer()} to pass list of markers to cluster in {@link GoogleMapFragment}.
     *
     * @param markerList of {@link Marker} objects
     */
    private void passDataToCluster(List<Marker> markerList) {
        view.setUpCluster(markerList);
    }

    /**
     * Called from {@link GoogleMapFragment#onQueryTextChange(String)} to get {@link Cursor} object
     * with {@link Marker} data that contains matching provided text by user.
     *
     * @param providedText by the user in a SearchView
     */
    @Override
    public void getCursorMarker(String providedText) {
        loyaltyRepository.getCursorMarker(providedText, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                view.setUpSearchViewAdapter((Cursor) object);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link GoogleMapFragment#onMapReady(GoogleMap)} and
     * {@link GoogleMapFragment#animateCameraAndShowBottomSheet(Object)} to change BottomSheet state.
     *
     * @param object that was clicked on map on which depends which action should be triggered
     */
    @Override
    public void switchBottomSheetState(Object object) {
        if (object instanceof Marker || object instanceof Cursor) {
            if (view.getBottomSheetState() == BottomSheetBehavior.STATE_HIDDEN) {
                view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
                new Handler().postDelayed(() -> view.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED), DELAY_BOTTOMSHEET_ACTION);
            }
        } else if (object instanceof LatLng) {
            if (view.getBottomSheetState() != BottomSheetBehavior.STATE_HIDDEN) {
                view.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }

    /**
     * Called from {@link GoogleMapFragment#animateCameraAndShowBottomSheet(Object)} to pass ID of
     * clicked marker on map.
     *
     * @param markerId of clicked {@link Marker}
     */
    @Override
    public void passClickedMarkerId(int markerId) {
        mMarkerIdSubject.onNext(markerId);
    }

    /**
     * Called from {@link #setUpObservable()} to get {@link Marker} object of selected marker.
     *
     * @param markerId of clicked {@link Marker}
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
        List<OpenHour> openHourList = marker.getOpenHourList();

        String title, address, openHours;
        title = address = openHours = DEFAULT_UNABLE_STRING;

        if (!TextUtils.isEmpty(marker.getTitle())) {
            title = marker.getTitle();
        }

        if (!TextUtils.isEmpty(marker.getAddress()) && !TextUtils.isEmpty(marker.getPostalCode())
                && !TextUtils.isEmpty(marker.getCity())) {
            address = marker.getAddress() + ", " +
                    marker.getPostalCode().substring(0, 2) + "-" + marker.getPostalCode().substring(2) + " " +
                    marker.getCity();
        }

        if (openHourList != null) {
            for (OpenHour openHour : openHourList) {
                if (openHour.getDayName().equals(getCurrentDay())) {
                    if (!openHour.getOpenHour().equals(ERROR_NONE_STRING) && !openHour.getOpenMinute().equals(ERROR_NONE_STRING) &&
                            !openHour.getCloseHour().equals(ERROR_NONE_STRING) && !openHour.getCloseMinute().equals(ERROR_NONE_STRING)) {
                        openHours = TODAY_OPEN_STRING + " " +
                                openHour.getOpenHour() + ":" +
                                openHour.getOpenMinute() + " - " +
                                openHour.getCloseHour() + ":" +
                                openHour.getCloseMinute();
                    } else if (openHour.getOpenHour().equals(openHour.getCloseHour()) &&
                            openHour.getOpenMinute().equals(openHour.getCloseMinute()) &&
                            !openHour.getOpenHour().equals(ERROR_NONE_STRING)) {
                        openHours = ALL_DAY_STRING;
                    } else {
                        openHours = CLOSED_STRING;
                    }

                    break;
                }
            }
        }

        passDataToView(title, address, openHours);
    }

    /**
     * Called from {@link #formatMarkerData(Marker)} to determine what day is while user is running the app.
     *
     * @return string with current day
     */
    private String getCurrentDay() {
        String day = "";

        switch (mCurrentDay) {
            case 1:
                day = SUNDAY_STRING;
                break;
            case 2:
                day = MONDAY_STRING;
                break;
            case 3:
                day = TUESDAY_STRING;
                break;
            case 4:
                day = WEDNESDAY_STRING;
                break;
            case 5:
                day = THURSDAY_STRING;
                break;
            case 6:
                day = FRIDAY_STRING;
                break;
            case 7:
                day = SATURDAY_STRING;
                break;
        }

        return day;
    }

    /**
     * Called from {@link #formatMarkerData(Marker)} to pass prepared data to view.
     *
     * @param title string of selected marker on map
     * @param address string of selected marker on map
     * @param openHours string of selected marker on map
     */
    private void passDataToView(String title, String address, String openHours) {
        view.setUpBottomSheetPanelWithData(title, address, openHours);
    }

    /**
     * Called from {@link GoogleMapFragment#animateCameraAndShowBottomSheet(Object)} to get position
     * of marker either from {@link Marker} object or {@link Cursor}.
     *
     * @param selectedPlace is object type of selected marker. It can be either {@link Marker} or {@link Cursor}
     * @return LatLng object with retrieved position
     */
    @Override
    public LatLng getPositionFromObject(Object selectedPlace) {
        LatLng position;

        if (selectedPlace instanceof Marker) {
            Marker marker = (Marker) selectedPlace;
            position = new LatLng(marker.getLat(), marker.getLng());
        } else {
            Cursor cursor = (Cursor) selectedPlace;
            double lat = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("lat")));
            double lng = Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow("lng")));
            position = new LatLng(lat, lng);
        }

        return position;
    }

    /**
     * Called from {@link GoogleMapFragment#animateCameraAndShowBottomSheet(Object)} to get ID
     * of marker either from {@link Marker} object or {@link Cursor}.
     *
     * @param selectedPlace is object type of selected marker. It can be either {@link Marker} or {@link Cursor}
     * @return int object with retrieved ID
     */
    @Override
    public int getIdFromObject(Object selectedPlace) {
        int markerId;

        if (selectedPlace instanceof Marker) {
            Marker marker = (Marker) selectedPlace;
            markerId = marker.getId();
        } else {
            Cursor cursor = (Cursor) selectedPlace;
            markerId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("id")));
        }

        return markerId;
    }
}
