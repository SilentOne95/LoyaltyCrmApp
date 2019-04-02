package com.sellger.konta.sketch_loyaltyapp.base;

public interface BaseFragmentContract {

    interface View {

        void attachPresenter(Presenter presenter);
    }

    interface Presenter {

        void getSelectedLayoutType(String itemType, String data);
    }
}
