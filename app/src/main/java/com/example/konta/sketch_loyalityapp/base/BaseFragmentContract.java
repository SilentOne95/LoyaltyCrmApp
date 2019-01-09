package com.example.konta.sketch_loyalityapp.base;

import com.example.konta.sketch_loyalityapp.adapterModel.ItemHome;

public interface BaseFragmentContract {

    interface View {

        void attachPresenter(Presenter presenter);
    }

    interface Presenter {
        void getSelectedLayoutType(ItemHome item);
    }
}
