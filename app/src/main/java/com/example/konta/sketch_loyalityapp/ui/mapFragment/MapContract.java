package com.example.konta.sketch_loyalityapp.ui.mapFragment;

import com.example.konta.sketch_loyalityapp.data.map.Marker;

import java.util.List;

public interface MapContract {

    interface View {

        void setUpCluster(List<Marker> markerList);

        int getBottomSheetState();
        void setBottomSheetState(int state);
    }

    interface Presenter {

        void requestDataFromServer();

        void switchBottomSheetState(Object object);
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<Marker> markerList);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener);
    }
}
