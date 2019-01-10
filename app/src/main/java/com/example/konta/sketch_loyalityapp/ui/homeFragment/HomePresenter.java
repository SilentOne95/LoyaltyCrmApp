package com.example.konta.sketch_loyalityapp.ui.homeFragment;

import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.mainActivity.MainActivityContract;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter, MainActivityContract.Model.OnFinishedListener {

    private HomeContract.View view;
    private MainActivityContract.Model model;

    HomePresenter(HomeContract.View view, MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onFinished(List<MenuComponent> listMenuComponent) {
        if (view != null) {
            view.setUpAdapter(listMenuComponent);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void fetchDataFromServer() {
        model.fetchDataFromServer(this);
    }
}
