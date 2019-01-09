package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.adapterModel.ItemHome;
import com.example.konta.sketch_loyalityapp.data.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.couponsFragment.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.mapFragment.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.homeFragment.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.productsFragment.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contactActivity.ContactActivity;
import com.example.konta.sketch_loyalityapp.ui.termsActivity.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.ui.websiteActivity.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.ui.logInActivity.LogInActivity;

import java.util.List;


public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter, MainActivityContract.Model.OnFinishedListener {

    @Nullable
    private MainActivityContract.View view;
    private MainActivityContract.Model model;

    MainActivityPresenter(@Nullable MainActivityContract.View view,
                          MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void displayHomeScreen() {
        if (view != null) {
            view.setFragment(new HomeFragment());
        }
    }

    @Override
    public void onFinished(List<MenuComponent> listMenuComponent) {

        int menuId = 0, submenuId = 0, homeScreenId = 0;
        String menuType, type, title;
        SparseArray<HelperComponent> menuArray = new SparseArray<>();
        SparseArray<HelperComponent> submenuArray = new SparseArray<>();

        for (int i = 0; i < listMenuComponent.size(); i++) {
            menuType = listMenuComponent.get(i).getList();

            if (menuType.equals("menu")) {
                type = listMenuComponent.get(i).getType();
                title = listMenuComponent.get(i).getComponentTitle();

                menuArray.append(menuId, new HelperComponent(type, title));
                menuId++;

                if (listMenuComponent.get(i).getIsHomePage().equals(1)) {
                    homeScreenId = menuId - 1;
                }

            } else if (menuType.equals("submenu")) {
                type = listMenuComponent.get(i).getType();
                title = listMenuComponent.get(i).getComponentTitle();

                submenuArray.append(submenuId, new HelperComponent(type, title));
                submenuId++;

                if (listMenuComponent.get(i).getIsHomePage().equals(1)) {
                    homeScreenId = menuId - 1;
                }
            }
        }

        if (view != null) {
            view.setDataToNavDrawer(menuArray, submenuArray, homeScreenId);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void displaySelectedScreen(String layoutType) {

        if (layoutType != null) {
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

    }

    @Override
    public void getSelectedLayoutType(ItemHome item) {
        displaySelectedScreen(item.getLayoutType());
    }
}