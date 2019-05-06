package com.sellger.konta.sketch_loyaltyapp.ui.home;

import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.util.ArrayList;

public interface HomeContract {

    interface View {

        void initViews();

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
