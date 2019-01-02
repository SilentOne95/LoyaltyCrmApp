package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.ui.couponsFragment.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.mapFragment.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.homeFragment.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.productsFragment.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contactActivity.ContactActivity;
import com.example.konta.sketch_loyalityapp.ui.termsActivity.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.ui.websiteActivity.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.ui.logInActivity.LogInActivity;

public class MainActivityPresenter implements MainActivityContract.Presenter, BaseFragmentContract.Presenter {

    @Nullable
    private MainActivityContract.View view;

    MainActivityPresenter(MainActivityContract.View view) { this.view = view; }

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
