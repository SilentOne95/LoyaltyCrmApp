package com.example.konta.sketch_loyalityapp.ui.main;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.network.Api;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivityModel implements MainActivityContract.Model {

    private MainActivityPresenter presenter;
    private SparseArray<HelperComponent> menuArray = new SparseArray<>();
    private SparseArray<HelperComponent> submenuArray = new SparseArray<>();

    @Override
    public Disposable fetchDataFromServer(MainActivityPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserver());
    }

    private Single<List<MenuComponent>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DisposableSingleObserver<List<MenuComponent>> getObserver() {
        return new DisposableSingleObserver<List<MenuComponent>>() {
            @Override
            public void onSuccess(List<MenuComponent> menuComponents) {
                refactorFetchedData(menuComponents);
            }

            @Override
            public void onError(Throwable e) { }
        };
    }

    @Override
    public void refactorFetchedData(List<MenuComponent> listOfItems) {
        int homeScreenId = 0;
        String menuType, type, title;
        SparseArray<MenuComponent> menuTemporary = new SparseArray<>();
        SparseArray<MenuComponent> submenuTemporary = new SparseArray<>();

        for (int i = 0; i < listOfItems.size(); i++) {
            menuType = listOfItems.get(i).getList();

            switch (menuType) {
                case "menu":
                    menuTemporary.append(i, listOfItems.get(i));

                    if (listOfItems.get(i).getIsHomePage().equals(1)) {
                        homeScreenId = listOfItems.get(i).getId();
                    }
                    break;
                case "submenu":
                    submenuTemporary.append(i, listOfItems.get(i));

                    if (listOfItems.get(i).getIsHomePage().equals(1)) {
                        homeScreenId = listOfItems.get(i).getId();
                    }
                    break;
            }
        }

        int key;
        int index = 0;
        int position = 1;
        do {
            key = menuTemporary.keyAt(index);

            if (menuTemporary.get(key).getPosition() == position) {
                type = menuTemporary.get(key).getType();
                title = menuTemporary.get(key).getComponentTitle();

                menuArray.append(position - 1, new HelperComponent(type, title));

                if (menuTemporary.get(key).getIsHomePage().equals(1)) {
                    homeScreenId = menuTemporary.get(key).getPosition() - 1;
                }

                position++;
                index = 0;
            } else {
                index++;
            }
        } while (menuArray.size() < menuTemporary.size());

        index = 0;
        position = 1;
        do {
            key = submenuTemporary.keyAt(index);

            if (submenuTemporary.get(key).getPosition() == position) {
                type = submenuTemporary.get(key).getType();
                title = submenuTemporary.get(key).getComponentTitle();

                submenuArray.append(position - 1, new HelperComponent(type, title));

                if (submenuTemporary.get(key).getIsHomePage().equals(1)) {
                    homeScreenId = submenuTemporary.get(key).getPosition() + menuArray.size() - 1;
                }

                position++;
                index = 0;
            } else {
                index++;
            }
        } while (submenuArray.size() < submenuTemporary.size());

        presenter.passDataToNavDrawer(menuArray, submenuArray, homeScreenId);
    }

    @Override
    public String getMenuLayoutType(int itemId) {
        return menuArray.get(itemId).getType();
    }

    @Override
    public String getSubmenuLayoutType(int itemId) {
        return submenuArray.get(itemId).getType();
    }
}
