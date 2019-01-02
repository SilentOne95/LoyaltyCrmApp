package com.example.konta.sketch_loyalityapp.ui.mapFragment;

public interface MapContract {

    interface View {

        int getBottomSheetState();
        void setBottomSheetState(int state);
    }

    interface Presenter {
        void switchBottomSheetState(Object object);
    }

    interface Model {

    }
}
