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
    public String getLayoutType(int groupId, int itemId) {
        String layoutType;

        if (groupId == 0) {
            layoutType = menuArray.get(itemId).getValOne();
        } else {
            layoutType = submenuArray.get(itemId).getValOne();
        }

        return layoutType;
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void displaySelectedScreen(String layoutType) {

        if (layoutType != null) {
            switch (layoutType) {
                case "home":
                    if (view != null)
                        view.setFragment(new HomeFragment());
                    break;
                case "products":
                    if (view != null)
                        view.setFragment(new ProductsFragment());
                    break;
                case "coupons":
                    if (view != null)
                        view.setFragment(new CouponsFragment());
                    break;
                case "map":
                    if (view != null)
                        view.setFragment(new GoogleMapFragment());
                    break;
                case "login":
                    if (view != null)
                        view.setActivity(LogInActivity.class);
                    break;
                case "internet":
                    if (view != null)
                        view.setActivity(WebsiteActivity.class);
                    break;
                case "terms":
                    if (view != null)
                        view.setActivity(TermsConditionsActivity.class);
                    break;
                case "contact":
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