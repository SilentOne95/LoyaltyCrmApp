package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.app.Activity;

import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.util.ArrayList;

public interface MainActivityContract {

    interface View {

        void setActivity(Class<? extends Activity> activity);

        void setFragment(BaseFragment fragment, String fragmentTitle, String data);

        void setDataToNavDrawer(ArrayList<MenuComponent> menuSectionArray,
                                ArrayList<MenuComponent> submenuSectionArray,
                                int homeScreenId, String[] iconNameArray);

        void setDisplayItemChecked(int viewPosition);

        void setNavViewHeaderVisibility(String isAccountAnonymous);

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();

        void displayHomeScreen(String layoutType);

        String getLayoutType(int groupId, int itemId);

        void displaySelectedScreen(String layoutType, String data);

        void setUpObservableHomeAdapter();

        void passIdOfSelectedView(int viewPosition);

        void getLayoutTypeOfSelectedScreen(String displayedLayoutName);
    }
}
