package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.app.Activity;

import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMenuArray;

import java.util.ArrayList;
import java.util.List;

public interface MainActivityContract {

    interface View {

        void initViews();

        void registerNetworkReceiver();

        void displaySnackbar(boolean isNetwork);

        void unregisterNetworkReceiver();

        void setActivity(Class<? extends Activity> activity);

        void setFragment(BaseFragment fragment, String fragmentTitle, String data);

        void setLogInFragment(BaseFragment fragment);

        void hideNavDrawerScrollbar();

        void setDataToNavDrawer(ArrayList<MenuComponent> menuSectionArray,
                                ArrayList<MenuComponent> submenuSectionArray,
                                int homeScreenId, String[] iconNameArray);

        void setDisplayItemChecked(int viewPosition);

        void uncheckItemsNavDrawer();

        void setNavViewHeaderVisibility(String isAccountAnonymous);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();

        void displayHomeScreen(String layoutType);

        void refactorFetchedData(List<MenuComponent> menuComponentList);

        HelperMenuArray sortMenuDataList(List<MenuComponent> listOfItems);

        void passDataToNavDrawer(ArrayList<MenuComponent> one, ArrayList<MenuComponent> two, int id);

        String matchRelevantIconName(String layoutType);

        String getLayoutType(int groupId, int itemId);

        String getLayoutTitle(String layoutType);

        void displaySelectedScreen(String layoutType, String data);

        void setUpObservableHomeAdapter();

        void passIdOfSelectedView(int viewPosition);

        void matchRelevantLayoutType(String displayedLayoutName);
    }
}
