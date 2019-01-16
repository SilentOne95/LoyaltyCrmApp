package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
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
        BaseFragmentContract.Presenter, BaseCallbackListener.ListItemsOnFinishListener<MenuComponent> {

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
    public void onFinished(List<MenuComponent> listOfItems) {
        int homeScreenId = 0;
        String menuType, type, title;
        SparseArray<MenuComponent> menuTemporary = new SparseArray<>();
        SparseArray<MenuComponent> submenuTemporary = new SparseArray<>();

        for (int i = 0; i < listOfItems.size(); i++) {
            menuType = listOfItems.get(i).getList();

            switch (menuType) {
                case "menu":
                    menuTemporary.append(i, listOfItems.get(i));

                    if (listOfItems.get(i).getIsHomePage().equals(1)) {
                        homeScreenId = listOfItems.get(i).getId();
                    }
                    break;
                case "submenu":
                    submenuTemporary.append(i, listOfItems.get(i));

                    if (listOfItems.get(i).getIsHomePage().equals(1)) {
                        homeScreenId = listOfItems.get(i).getId();
                    }
                    break;
            }
        }

        int key;
        int index = 0;
        int position = 1;
        do {
            key = menuTemporary.keyAt(index);

            if (menuTemporary.get(key).getPosition() == position) {
                type = menuTemporary.get(key).getType();
                title = menuTemporary.get(key).getComponentTitle();

                menuArray.append(position - 1, new HelperComponent(type, title));

                if (menuTemporary.get(key).getIsHomePage().equals(1)) {
                    homeScreenId = menuTemporary.get(key).getPosition() - 1;
                }

                position++;
                index = 0;
            } else {
                index++;
            }
        } while (menuArray.size() < menuTemporary.size());

        index = 0;
        position = 1;
        do {
            key = submenuTemporary.keyAt(index);

            if (submenuTemporary.get(key).getPosition() == position) {
                type = submenuTemporary.get(key).getType();
                title = submenuTemporary.get(key).getComponentTitle();

                submenuArray.append(position - 1, new HelperComponent(type, title));

                if (submenuTemporary.get(key).getIsHomePage().equals(1)) {
                    homeScreenId = submenuTemporary.get(key).getPosition() + menuArray.size() - 1;
                }

                position++;
                index = 0;
            } else {
                index++;
            }
        } while (submenuArray.size() < submenuTemporary.size());

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