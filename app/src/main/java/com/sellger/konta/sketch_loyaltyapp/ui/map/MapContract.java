package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.app.PendingIntent;

import com.sellger.konta.sketch_loyaltyapp.pojo.map.Marker;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.contactInfo.ContactInfoPresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours.OpeningHoursPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MapContract {

    interface View {

        void initViews();

        void setUpGoogleApiClient();
        void checkLocationPermission();
        void startTrackService();
        void startLocationService();
        void requestLocationUpdates();
        PendingIntent getPendingIntent();

        void setCustomMapStyle();
        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();
        void setBottomSheetState(int state);

        void setUpBottomSheetPanelWithData(String title, String address, String openHours);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToCluster(List<Marker> markerList);

        void switchBottomSheetState(Object object);

        void setUpObservable();
        void passDataToView(String title, String address, String openHours);

        void findSelectedMarkerData(int markerId);
        void passDataToBottomSheet(int markerId);
    }

    interface Model {

        Disposable fetchDataFromServer(MapPresenter presenter);

        Disposable fetchDataFromServer(OpeningHoursPresenter openingHoursPresenter);
        Disposable fetchDataFromServer(ContactInfoPresenter contactInfoPresenter);

        void getMarkerData(int markerId);
    }
}
