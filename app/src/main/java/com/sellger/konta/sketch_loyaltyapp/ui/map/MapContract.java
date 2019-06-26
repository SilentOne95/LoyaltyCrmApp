package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;

import java.util.List;

public interface MapContract {

    interface View {

        void setUpGoogleApiClient();

        void setCustomMapStyle();

        void checkLocationPermission();

        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();

        void setBottomSheetState(int state);

        void setUpBottomSheetPanelWithData(String title, String address, String openHours);

        void setUpSearchViewAdapter(Cursor cursor);

        void animateCameraAndShowBottomSheet(Object selectedPlace);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void setUpObservable();

        void requestDataFromServer();

        void passDataToCluster(List<Marker> markerList);

        void getCursorMarker(String providedText);

        void switchBottomSheetState(Object object);

        void passClickedMarkerId(int markerId);

        void getSelectedMarker(int markerId);

        void formatMarkerData(Marker marker);

        String getCurrentDay();

        void passDataToView(String title, String address, String openHours);

        LatLng getPositionFromObject(Object selectedPlace);

        int getIdFromObject(Object selectedPlace);
    }
}
