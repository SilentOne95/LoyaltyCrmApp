package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.data.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.login.LogInActivity;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.products.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contact.ContactActivity;
import com.example.konta.sketch_loyalityapp.ui.terms.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.ui.website.WebsiteActivity;

import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter, MainActivityContract.Model.OnFinishedListener {

    @Nullable
    private MainActivityContract.View view;
    private MainActivityContract.Model model;

    private SparseArray<HelperComponent> menuArray = new SparseArray<>();
    private SparseArray<HelperComponent> submenuArray = new SparseArray<>();

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
        if (view != null) {
            view.onResponseFailure(t);
        }
    }

    @Override
    public String getLayoutType(int groupId, int itemId) {
        String layoutType;

        if (groupId == 0) {
            layoutType = menuArray.get(itemId).getType();
        } else {
            layoutType = submenuArray.get(itemId).getType();
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