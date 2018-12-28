package com.example.konta.sketch_loyalityapp.main;

import android.support.annotation.Nullable;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    @Nullable
    private MainActivityContract.View mView;

    @Override
    public void setView(MainActivityContract.View view) { mView = view; }

    @Override
    public void displaySelectedScreen() {

    }

    @Override
    public void bottomSheetClicked() {

    }
}
