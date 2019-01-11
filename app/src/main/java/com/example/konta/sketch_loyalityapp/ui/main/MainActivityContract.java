package com.example.konta.sketch_loyalityapp.ui.main;

import android.app.Activity;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.data.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;

import java.util.List;

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
        String getLayoutType(int groupId, int itemId);

        void displayHomeScreen();
        void displaySelectedScreen(String layoutType);
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<MenuComponent> menuComponentList);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener);
    }
}
