package com.sellger.konta.sketch_loyaltyapp.ui.home;

import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.util.ArrayList;

public interface HomeContract {

    interface View {

        void setUpAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns);

        void hideProgressBar();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();

        void passIdOfSelectedView(int viewId);
    }
}
