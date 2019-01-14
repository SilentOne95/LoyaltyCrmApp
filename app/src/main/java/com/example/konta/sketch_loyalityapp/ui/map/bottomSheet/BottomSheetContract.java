package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import com.example.konta.sketch_loyalityapp.data.map.Marker;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

    }

    interface OpeningHoursView {

    }

    interface ContactInfoPresenter {
        void requestMarkersList();
    }

    interface OpeningHoursPresenter {
        void requestMarkersList();
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<Marker> markerList);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener);

        void fetchMarkerList(OnFinishedListener onFinishedListener);
    }
}
