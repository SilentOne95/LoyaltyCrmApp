package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.ui.homeFragment.HomeFragment;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    @Nullable
    private MainActivityContract.View view;

    MainActivityPresenter(@Nullable MainActivityContract.View view) { this.view = view; }

    @Override
    public void displayHomeScreen() {
        if (view != null) {
            view.setFragment(new HomeFragment());
        }
    }
}
