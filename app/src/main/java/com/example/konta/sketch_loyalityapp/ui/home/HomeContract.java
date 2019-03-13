package com.example.konta.sketch_loyalityapp.ui.home;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

import java.util.ArrayList;

public interface HomeContract {

    interface View {

        void setUpAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void hideProgressBar();
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns);
        void hideProgressBar();

        void passIdOfSelectedView(int viewId);
    }
}
