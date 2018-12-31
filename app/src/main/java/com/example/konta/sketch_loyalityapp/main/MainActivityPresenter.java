package com.example.konta.sketch_loyalityapp.main;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.CouponsFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.HomeFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.ProductsFragment;
import com.example.konta.sketch_loyalityapp.independentViews.ContactActivity;
import com.example.konta.sketch_loyalityapp.independentViews.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.independentViews.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.loginViews.LogInActivity;

public class MainActivityPresenter implements MainActivityContract.Presenter, BaseFragmentContract.Presenter {

    @Nullable
    private MainActivityContract.View view;

    MainActivityPresenter(MainActivityContract.View view) { this.view = view; }

    @Override
    public void setView(MainActivityContract.View view) { this.view = view; }

    @Override
    public void displaySelectedScreen(int groupId, int itemId, String layoutType) {

        switch (layoutType) {
            case "Home":
                view.setFragment(new HomeFragment());
                break;
            case "Products":
                view.setFragment(new ProductsFragment());
                break;
            case "Coupons":
                view.setFragment(new CouponsFragment());
                break;
            case "Map":
                view.setFragment(new GoogleMapFragment());
                break;
            case "LogIn":
                view.setActivity(LogInActivity.class);
                break;
            case "Internet":
                view.setActivity(WebsiteActivity.class);
                break;
            case "Terms":
                view.setActivity(TermsConditionsActivity.class);
                break;
            case "Contact":
                view.setActivity(ContactActivity.class);
                break;
        }
    }

    @Override
    public void displayHomeScreen() {
        view.setFragment(new HomeFragment());
    }

}
