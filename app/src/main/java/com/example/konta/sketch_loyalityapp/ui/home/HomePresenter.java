package com.example.konta.sketch_loyalityapp.ui.home;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityContract;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {

    @Nullable
    private HomeContract.View view;
    private MainActivityContract.Model model;

    HomePresenter(@Nullable HomeContract.View view, MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void refactorFetchedData(List<MenuComponent> listMenuComponent) {

        // TODO:
        SparseArray<MenuComponent> list = new SparseArray<>();
        int i = 0;

        if (listMenuComponent != null) {
            for (MenuComponent component : listMenuComponent) {
                if (component.getList().equals("menu")) {
                    list.append(i, component);
                    i++;
                }
            }

            for (MenuComponent component : listMenuComponent) {
                if (component.getList().equals("submenu")) {
                    list.append(i, component);
                    i++;
                }
            }
        }

        if (view != null) {
            view.setUpAdapter(listMenuComponent);
        }
    }
}
