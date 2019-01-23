package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.login.LogInActivity;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.products.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contact.ContactActivity;
import com.example.konta.sketch_loyalityapp.ui.terms.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.ui.website.WebsiteActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter {

    @Nullable
    private MainActivityContract.View view;
    private MainActivityContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    MainActivityPresenter(@Nullable MainActivityContract.View view,
                          MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void displayHomeScreen() {
        if (view != null) {
            view.setFragment(new HomeFragment());
        }
    }

    @Override
    public void setUpNavDrawer(SparseArray<HelperComponent> menu,
                               SparseArray<HelperComponent> submenu, int homeScreenId) {
        if (view != null) {
            view.setDataToNavDrawer(menu, submenu, homeScreenId);
        }
    }

    @Override
    public String getLayoutType(int groupId, int itemId) {
        String layoutType;

        if (groupId == 0) {
            layoutType = model.getMenuLayoutType(itemId);
        } else {
            layoutType = model.getSubmenuLayoutType(itemId);
        }

        return layoutType;
    }

    @Override
    public void displaySelectedScreen(String layoutType) {

        if (layoutType != null && view != null) {
            switch (layoutType) {
                case "home":
                    view.setFragment(new HomeFragment());
                    break;
                case "products":
                    view.setFragment(new ProductsFragment());
                    break;
                case "coupons":
                    view.setFragment(new CouponsFragment());
                    break;
                case "map":
                    view.setFragment(new GoogleMapFragment());
                    break;
                case "login":
                    view.setActivity(LogInActivity.class);
                    break;
                case "internet":
                    view.setActivity(WebsiteActivity.class);
                    break;
                case "terms":
                    view.setActivity(TermsConditionsActivity.class);
                    break;
                case "contact":
                    view.setActivity(ContactActivity.class);
                    break;
            }

            view.setDisplayScreenChecked(layoutType);
        }
    }

    @Override
    public void getSelectedLayoutType(MenuComponent item) {
        displaySelectedScreen(item.getType());
    }
}