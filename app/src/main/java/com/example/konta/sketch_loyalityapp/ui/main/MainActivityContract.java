package com.example.konta.sketch_loyalityapp.ui.main;

import android.app.Activity;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MainActivityContract {

    interface View {

        void setFragment(BaseFragment fragment);
        void setActivity(Class<? extends Activity> activity);

        void setDataToNavDrawer(SparseArray<HelperComponent> menuSectionArray,
                                SparseArray<HelperComponent> submenuSectionArray,
                                int homeScreenId);
        void onResponseFailure(Throwable throwable);
        void setDisplayScreenChecked(String layoutType);
    }

    interface Presenter {

        void requestDataFromServer();
        void refactorFetchedData(List<MenuComponent> listOfItems);
        String getLayoutType(int groupId, int itemId);

        void displayHomeScreen();
        void displaySelectedScreen(String layoutType);
    }

    interface Model {

        Disposable fetchDataFromServer();
    }
}
