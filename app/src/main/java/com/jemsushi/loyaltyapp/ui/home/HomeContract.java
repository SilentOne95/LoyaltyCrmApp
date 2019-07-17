package com.jemsushi.loyaltyapp.ui.home;

import com.jemsushi.loyaltyapp.data.entity.MenuComponent;

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
