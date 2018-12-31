package com.example.konta.sketch_loyalityapp.map;

public interface MapContract {

    interface View {

        int getBottomSheetState();
        void setBottomSheetState(int state);
    }

    interface Presenter {
        void switchBottomSheetState(Object object);
    }
}
