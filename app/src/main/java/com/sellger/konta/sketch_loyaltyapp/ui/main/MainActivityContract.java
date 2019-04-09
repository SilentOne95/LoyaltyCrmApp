package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.app.Activity;

import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.HelperArray;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.ui.home.HomePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MainActivityContract {

    interface View {

        void initViews();

        void setFragment(BaseFragment fragment, String data);
        void setActivity(Class<? extends Activity> activity);
        void setLogInFragment(BaseFragment fragment);

        void hideNavDrawerScrollbar();

        void setDataToNavDrawer(ArrayList<MenuComponent> menuSectionArray,
                                ArrayList<MenuComponent> submenuSectionArray,
                                int homeScreenId, String[] iconNameArray);
        void setDisplayItemChecked(int viewPosition);
        void uncheckItemsNavDrawer();

        void setNavViewHeaderVisibility(String isAccountAnonymous);
    }

    interface Presenter {

        void requestDataFromServer();
        String getLayoutType(int groupId, int itemId);

        void displayHomeScreen(String layoutType);
        void displaySelectedScreen(String layoutType, String data);

        void passDataToNavDrawer(ArrayList<MenuComponent> one, ArrayList<MenuComponent> two, int id);
        String matchRelevantIconName(String layoutType);

        void setUpObservableHomeAdapter();
        void passIdOfSelectedView(int viewPosition);
    }

    interface Model {

        Disposable fetchDataFromServer(MainActivityPresenter presenter);
        Disposable fetchDataFromServer(HomePresenter presenter);

        void fetchedDataForNavDrawer(List<MenuComponent> listOfItems);
        void fetchedDataForHomeView(List<MenuComponent> listOfItems);
        HelperArray refactorFetchedData(List<MenuComponent> listOfItems);

        String getMenuLayoutType(int itemId);
        String getSubmenuLayoutType(int itemId);
    }
}
