package com.example.konta.sketch_loyalityapp.ui.home;

import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;

import java.util.List;

public interface HomeContract {

    interface View {
        void setUpAdapter(List<MenuComponent> menuComponentList);
    }

    interface Presenter {
        void fetchDataFromServer();
    }

    interface Model {

    }
}
