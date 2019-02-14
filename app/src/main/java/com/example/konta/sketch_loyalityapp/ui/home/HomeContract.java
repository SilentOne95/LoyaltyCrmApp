package com.example.konta.sketch_loyalityapp.ui.home;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

public interface HomeContract {

    interface View {

        void setUpAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void hideProgressBar();
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns);
        void hideProgressBar();

        void passIdOfSelectedView(int viewId);
    }
}
