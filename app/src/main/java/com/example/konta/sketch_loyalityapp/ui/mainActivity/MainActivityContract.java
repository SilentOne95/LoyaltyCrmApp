package com.example.konta.sketch_loyalityapp.ui.mainActivity;

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
        void setDisplayScreenChecked(String layoutType);
    }

    interface Presenter {

        void requestDataFromServer();

        void displayHomeScreen();
        void displaySelectedScreen(String layoutType);
    }

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<MenuComponent> noticeList);
            void onFailure(Throwable t);
        }

        void fetchDataFromServer(OnFinishedListener onFinishedListener);
    }
}
