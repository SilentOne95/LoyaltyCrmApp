package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.adapterModel.ItemHome;
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

    MainActivityPresenter(@Nullable MainActivityContract.View view) { this.view = view; }

    @Override
    public void displayHomeScreen() {
        if (view != null) {
            view.setFragment(new HomeFragment());
        }
    }

    @Override
    public void displaySelectedScreen(String layoutType) {

        switch (layoutType) {
            case "Home":
                if (view != null)
                    view.setFragment(new HomeFragment());
                break;
            case "Products":
                if (view != null)
                    view.setFragment(new ProductsFragment());
                break;
            case "Coupons":
                if (view != null)
                    view.setFragment(new CouponsFragment());
                break;
            case "Map":
                if (view != null)
                    view.setFragment(new GoogleMapFragment());
                break;
            case "LogIn":
                if (view != null)
                    view.setActivity(LogInActivity.class);
                break;
            case "Internet":
                if (view != null)
                    view.setActivity(WebsiteActivity.class);
                break;
            case "Terms":
                if (view != null)
                    view.setActivity(TermsConditionsActivity.class);
                break;
            case "Contact":
                if (view != null)
                    view.setActivity(ContactActivity.class);
                break;
        }

        if (view != null)
            view.setDisplayScreenChecked(layoutType);
    }

    @Override
    public void getSelectedLayoutType(ItemHome item) {
        displaySelectedScreen(item.getLayoutType());
    }
}