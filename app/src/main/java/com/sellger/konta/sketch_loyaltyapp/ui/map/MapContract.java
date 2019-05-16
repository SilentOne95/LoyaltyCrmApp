package com.sellger.konta.sketch_loyaltyapp.ui.map;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;

import java.util.List;

public interface MapContract {

    interface View {

        void initViews();

        void setUpGoogleApiClient();

        void setCustomMapStyle();

        void checkLocationPermission();

        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();

        void setBottomSheetState(int state);

        void setUpBottomSheetPanelWithData(String title, String address, String openHours);
    }

    interface Presenter {

        void setUpObservable();

        void requestDataFromServer();

        void passDataToCluster(List<Marker> markerList);

        void switchBottomSheetState(Object object);

        void passClickedMarkerId(int markerId);

        void getSelectedMarker(int markerId);

        void formatMarkerData(Marker marker);

        String getCurrentDay();

        void passDataToView(String title, String address, String openHours);
    }
}
