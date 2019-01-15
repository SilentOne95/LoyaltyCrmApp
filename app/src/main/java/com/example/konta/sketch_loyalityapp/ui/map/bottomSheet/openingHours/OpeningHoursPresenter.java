package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.data.map.Friday;
import com.example.konta.sketch_loyalityapp.data.map.Marker;
import com.example.konta.sketch_loyalityapp.data.map.Monday;
import com.example.konta.sketch_loyalityapp.data.map.OpenHours;
import com.example.konta.sketch_loyalityapp.data.map.Saturday;
import com.example.konta.sketch_loyalityapp.data.map.Sunday;
import com.example.konta.sketch_loyalityapp.data.map.Thursday;
import com.example.konta.sketch_loyalityapp.data.map.Tuesday;
import com.example.konta.sketch_loyalityapp.data.map.Wednesday;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

import java.util.List;

public class OpeningHoursPresenter implements BottomSheetContract.OpeningHoursPresenter {

    @Nullable
    private BottomSheetContract.OpeningHoursView view;

    OpeningHoursPresenter(@Nullable BottomSheetContract.OpeningHoursView view) {
        this.view = view;
    }

    @Override
    public void formatOpenHoursData(List<Marker> markerList) {
        SparseArray<String> list = new SparseArray<>();
        OpenHours open;
        String openHour, openMinute, closeHour, closeMinute, day;
        openHour = openMinute = closeHour = closeMinute = day = null;

        Marker marker = markerList.get(0);

        int i = 0;
        do {
            open = marker.getOpenHours();

            switch (i) {
                case 0:
                    Monday monday = open.getMonday();

                    openHour = monday.getOpenHour().toString();
                    openMinute = monday.getOpenMinute().toString();
                    closeHour = monday.getCloseHour().toString();
                    closeMinute = monday.getCloseMinute().toString();
                    break;
                case 1:
                    Tuesday tuesday = open.getTuesday();

                    openHour = tuesday.getOpenHour().toString();
                    openMinute = tuesday.getOpenMinute().toString();
                    closeHour = tuesday.getCloseHour().toString();
                    closeMinute = tuesday.getCloseMinute().toString();
                    break;
                case 2:
                    if (open.getWednesday() != null){
                        Wednesday wednesday = open.getWednesday();

                        openHour = wednesday.getOpenHour().toString();
                        openMinute = wednesday.getOpenMinute().toString();
                        closeHour = wednesday.getCloseHour().toString();
                        closeMinute = wednesday.getCloseMinute().toString();
                    }
                    break;
                case 3:
                    Thursday thursday = open.getThursday();

                    openHour = thursday.getOpenHour().toString();
                    openMinute = thursday.getOpenMinute().toString();
                    closeHour = thursday.getCloseHour().toString();
                    closeMinute = thursday.getCloseMinute().toString();
                    break;
                case 4:
                    Friday friday = open.getFriday();

                    openHour = friday.getOpenHour().toString();
                    openMinute = friday.getOpenMinute().toString();
                    closeHour = friday.getCloseHour().toString();
                    closeMinute = friday.getCloseMinute().toString();
                    break;
                case 5:
                    Saturday saturday = open.getSaturday();

                    openHour = saturday.getOpenHour().toString();
                    openMinute = saturday.getOpenMinute().toString();
                    closeHour = saturday.getCloseHour().toString();
                    closeMinute = saturday.getCloseMinute().toString();
                    break;
                case 6:
                    Sunday sunday = open.getSunday();

                    openHour = sunday.getOpenHour().toString();
                    openMinute = sunday.getOpenMinute().toString();
                    closeHour = sunday.getCloseHour().toString();
                    closeMinute = sunday.getCloseMinute().toString();
                    break;
                default:
                    break;
            }

            if (openHour.isEmpty() || openHour.equals(closeHour)) {
                day = "Closed";
            } else {
                day = openHour + ":" + openMinute + " - " + closeHour + ":" + closeMinute;
            }

            list.append(i, day);
            i++;
        } while (i < 7);

        if (view != null) {
            view.setUpViewsWithData(list);
        }
    }
}
