package com.example.konta.sketch_loyalityapp.main;

public interface MainActivityContract {

    interface View {

    }

    interface Presenter {

        void setView(MainActivityContract.View view);

        void displaySelectedScreen();
        void bottomSheetClicked();
    }

    interface Model {

    }
}
