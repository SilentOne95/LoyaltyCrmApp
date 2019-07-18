package com.ecommercelab.loyaltyapp.ui.map;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.ecommercelab.loyaltyapp.data.entity.Marker;

import java.util.List;

public interface MapContract {

    interface View {

        void checkLocationPermission();

        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();

        void setBottomSheetState(int state);

        void setUpBottomSheetPanelWithData(String title, String address, String openHours);

        void setUpSearchViewAdapter(Cursor cursor);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void setUpObservable();

        void requestDataFromServer();

        void getCursorMarker(String providedText);

        void switchBottomSheetState(Object object);

        void passClickedMarkerId(int markerId);

        LatLng getPositionFromObject(Object selectedPlace);

        int getIdFromObject(Object selectedPlace);
    }
}
