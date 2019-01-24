package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo.ContactInfoPresenter;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours.OpeningHoursPresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MapContract {

    interface View {

        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();
        void setBottomSheetState(int state);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToCluster(List<Marker> markerList);

        void switchBottomSheetState(Object object);

        void passDataToBottomSheet(int markerId);
    }

    interface Model {

        Disposable fetchDataFromServer(MapPresenter presenter);

        Disposable fetchDataFromServer(OpeningHoursPresenter openingHoursPresenter);
        Disposable fetchDataFromServer(ContactInfoPresenter contactInfoPresenter);
    }
}
