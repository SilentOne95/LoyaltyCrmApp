package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo.ContactInfoPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours.OpeningHoursPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MapContract {

    interface View {

        void setUpCluster(List<Marker> markerList);
        void setProgressBarVisibility(boolean isNeeded);

        int getBottomSheetState();
        void setBottomSheetState(int state);

        void setUpBottomSheetPanelWithData(String title, String address, String openHours);

        String getDefaultPlaceTitle();
        String getDefaultPlaceData();
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToCluster(List<Marker> markerList);
        void isProgressBarNeeded(boolean isNeeded);

        void switchBottomSheetState(Object object);

        void setUpObservable();
        void passDataToView(Marker marker);

        void findSelectedMarkerId(int markerId);
        String getCurrentDay();
        void passDataToBottomSheet(int markerId);
    }

    interface Model {

        Disposable fetchDataFromServer(MapPresenter presenter);

        Disposable fetchDataFromServer(OpeningHoursPresenter openingHoursPresenter);
        Disposable fetchDataFromServer(ContactInfoPresenter contactInfoPresenter);
    }
}
