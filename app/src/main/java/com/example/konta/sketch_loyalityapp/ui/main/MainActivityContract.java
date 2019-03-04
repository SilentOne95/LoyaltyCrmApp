package com.example.konta.sketch_loyalityapp.ui.main;

import android.app.Activity;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.home.HomePresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MainActivityContract {

    interface View {

        void setFragment(BaseFragment fragment, String data);
        void setActivity(Class<? extends Activity> activity);
        void setLogInFragment(BaseFragment fragment);

        void hideNavDrawerScrollbar();

        void setDataToNavDrawer(SparseArray<HelperComponent> menuSectionArray,
                                SparseArray<HelperComponent> submenuSectionArray,
                                int homeScreenId, String[] iconNameArray);
        void setDisplayItemChecked(int viewPosition);
        void uncheckItemsNavDrawer();
    }

    interface Presenter {

        void requestDataFromServer();
        String getLayoutType(int groupId, int itemId);

        void displayHomeScreen(String layoutType);
        void displaySelectedScreen(String layoutType, String data);

        void passDataToNavDrawer(SparseArray<HelperComponent> one, SparseArray<HelperComponent> two, int id);
        String matchRelevantIconName(String layoutType);

        void setUpObservableHomeAdapter();
        void passIdOfSelectedView(int viewPosition);
    }

    interface Model {

        Disposable fetchDataFromServer(MainActivityPresenter presenter);
        Disposable fetchDataFromServer(HomePresenter presenter);

        void refactorFetchedDataForNavDrawer(List<MenuComponent> listOfItems);
        void refactorFetchedDataForHomeView(List<MenuComponent> listOfItems);

        String getMenuLayoutType(int itemId);
        String getSubmenuLayoutType(int itemId);
    }
}
