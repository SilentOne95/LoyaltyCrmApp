package com.example.konta.sketch_loyalityapp.ui.main;

import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.ui.home.HomePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityModel implements MainActivityContract.Model {

    private MainActivityPresenter presenter;
    private HomePresenter homePresenter;

    private SparseArray<HelperComponent> menuArray = new SparseArray<>();
    private SparseArray<HelperComponent> submenuArray = new SparseArray<>();
    private SparseArray<MenuComponent> navDrawerArray = new SparseArray<>();

    @Override
    public Disposable fetchDataFromServer(MainActivityPresenter presenter) {
        this.presenter = presenter;
        return getObservableTimer()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::refactorFetchedDataForNavDrawer, throwable -> {});
    }

    @Override
    public Disposable fetchDataFromServer(HomePresenter presenter) {
        homePresenter = presenter;

        return getObservableTimer()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(componentList -> {
                    refactorFetchedDataForHomeView(componentList);
                    presenter.hideProgressBar();
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<List<MenuComponent>> getObservableTimer() {
        return Single.zip(getObservable(), Single.timer(1000, TimeUnit.MILLISECONDS),
                (componentList, timer) -> componentList);
    }

    private Single<List<MenuComponent>> getObservable() {
        return RetrofitClient.getInstance().create(Api.class)
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void refactorFetchedDataForNavDrawer(List<MenuComponent> listOfItems) {
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
    public void refactorFetchedDataForHomeView(List<MenuComponent> listOfItems) {
        int i = 0;
        int numOfColumns = 1;

        if (listOfItems != null) {
            for (MenuComponent component : listOfItems) {
                if (component.getList().equals("menu") && !component.getIsHomePage().equals(1)) {
                    navDrawerArray.append(i, component);
                    i++;
                }
            }

            for (MenuComponent component : listOfItems) {
                if (component.getList().equals("submenu") && !component.getIsHomePage().equals(1)) {
                    navDrawerArray.append(i, component);
                    i++;
                }
            }
        }

        if (listOfItems != null) {
            for (MenuComponent component : listOfItems) {
                if (component.getIsHomePage().equals(1)) {
                    numOfColumns = component.getNumberOfColumns();
                    break;
                }
            }
        }

        if (numOfColumns < 1 || numOfColumns > 2) {
            numOfColumns = 2;
        }

        homePresenter.passDataToAdapter(navDrawerArray, numOfColumns);
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
