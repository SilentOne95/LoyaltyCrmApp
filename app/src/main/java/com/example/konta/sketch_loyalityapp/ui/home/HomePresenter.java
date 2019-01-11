package com.example.konta.sketch_loyalityapp.ui.home;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityContract;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter, MainActivityContract.Model.OnFinishedListener {

    @Nullable
    private HomeContract.View view;
    private MainActivityContract.Model model;

    HomePresenter(@Nullable HomeContract.View view, MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onFinished(List<MenuComponent> listMenuComponent) {

        // TODO:
        SparseArray<MenuComponent> list = new SparseArray<>();
        int i = 0;

        if (listMenuComponent != null) {
            for (MenuComponent component : listMenuComponent) {
                if (component.getList().equals("menu")) {
                    list.append(i, component);
                    i++;
                    Log.d("test 1 ", component.getComponentTitle());
                }
            }

            for (MenuComponent component : listMenuComponent) {
                if (component.getList().equals("submenu")) {
                    list.append(i, component);
                    i++;
                    Log.d("test 2 ", component.getComponentTitle());
                }
            }
        }

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
