package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet;

import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMarker;

import java.util.List;

public interface BottomSheetContract {

    interface ContactInfoView {

        void initViews();

        void setUpViewsWithData(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface OpeningHoursView {

        void initViews();

        void setUpViewsWithData(String[] singleDayOpenHours);
    }

    interface ContactInfoPresenter {

        void setUpObservable();

        void getMarkerList();

        void formatContactInfoData(List<HelperMarker> markerList);
        String formatPhoneNumber(HelperMarker marker);
        String formatEmailAddress(HelperMarker marker);
        String formatWebsiteAddress(HelperMarker marker);
        void passDataToView(String phoneNumber, String emailAddress, String websiteAddress);
    }

    interface OpeningHoursPresenter {

        void setUpObservable();

        void getMarkerList();
        void formatOpenHoursData(List<HelperMarker> markerList);
        String checkIfOpenHoursAreValid(OpenHour time);
    }
}
