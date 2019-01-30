package com.example.konta.sketch_loyalityapp.ui.home;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

public interface HomeContract {

    interface View {

        void setUpAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns);

        void passIdOfSelectedView(int viewId);
    }
}
