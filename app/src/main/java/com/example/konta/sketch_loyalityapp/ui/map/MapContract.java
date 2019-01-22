package com.example.konta.sketch_loyalityapp.ui.map;

import com.example.konta.sketch_loyalityapp.pojo.map.Marker;

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

        void switchBottomSheetState(Object object);

        void passDataToBottomSheet(int markerId);
    }

    interface Model {

        Disposable fetchDataFromServer();
    }
}
