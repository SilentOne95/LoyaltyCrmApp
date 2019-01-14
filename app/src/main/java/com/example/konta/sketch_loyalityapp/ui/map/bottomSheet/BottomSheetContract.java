package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.data.map.Marker;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void setUpViewsWithData(List<Marker> markerList);
    }

    interface OpeningHoursView {

        void setUpViewsWithData(SparseArray<String> singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void requestMarkersList();
    }

    interface OpeningHoursPresenter {

        void requestMarkersList();

        void formatOpenHoursData(List<Marker> markerList);
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
