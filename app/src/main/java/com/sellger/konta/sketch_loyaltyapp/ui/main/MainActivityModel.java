package com.sellger.konta.sketch_loyaltyapp.ui.main;

import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.HelperArray;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.ui.home.HomePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityModel implements MainActivityContract.Model {

    private MainActivityPresenter presenter;
    private HomePresenter homePresenter;

    private ArrayList<MenuComponent> menuArray = new ArrayList<>();
    private ArrayList<MenuComponent> submenuArray = new ArrayList<>();
    private ArrayList<MenuComponent> navDrawerArray = new ArrayList<>();

    @Override
    public Disposable fetchDataFromServer(MainActivityPresenter presenter) {
        this.presenter = presenter;
        return getObservableTimer()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::fetchedDataForNavDrawer, throwable -> {});
    }

    @Override
    public Disposable fetchDataFromServer(HomePresenter presenter) {
        homePresenter = presenter;

        return getObservableTimer()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(componentList -> {
                    fetchedDataForHomeView(componentList);
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
    public void fetchedDataForNavDrawer(List<MenuComponent> listOfItems) {
        int homeScreenId = 0;

        HelperArray helperArray = refactorFetchedData(listOfItems);
        menuArray = helperArray.getMenuArray();
        submenuArray = helperArray.getSubmenuArray();

        for (int i = 0; i < menuArray.size(); i++) {
            if (menuArray.get(i).getIsHomePage().equals(1)) {
                homeScreenId = menuArray.get(i).getPosition() - 1;
                break;
            }
        }

        for (int i = 0; i < submenuArray.size(); i++) {
            if (submenuArray.get(i).getIsHomePage().equals(1)) {
                homeScreenId = submenuArray.get(i).getPosition() - 1;
                break;
            }
        }

        presenter.passDataToNavDrawer(menuArray, submenuArray, homeScreenId);
    }

    @Override
    public void fetchedDataForHomeView(List<MenuComponent> listOfItems) {
        int numOfColumns = 2;
        HelperArray helperArray = refactorFetchedData(listOfItems);
        navDrawerArray = helperArray.getMenuArray();
        navDrawerArray.addAll(helperArray.getSubmenuArray());

        for (int i = 0; i < navDrawerArray.size(); i++) {
            if (navDrawerArray.get(i).getIsHomePage().equals(1)) {
                numOfColumns = navDrawerArray.get(i).getNumberOfColumns();
                navDrawerArray.remove(i);
                break;
            }
        }

        homePresenter.passDataToAdapter(navDrawerArray, numOfColumns);
    }

    @Override
    public HelperArray refactorFetchedData(List<MenuComponent> listOfItems) {
        String menuType;
        ArrayList<MenuComponent> menuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> submenuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedMenuArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedSubmenuArray = new ArrayList<>();

        for (int i = 0; i < listOfItems.size(); i++) {
            menuType = listOfItems.get(i).getList();

            switch (menuType) {
                case "menu":
                    menuLocalArray.add(listOfItems.get(i));
                    break;
                case "submenu":
                    submenuLocalArray.add(listOfItems.get(i));
                    break;
            }
        }

        int index = 0;
        int position = 1;

        do {

            if (menuLocalArray.get(index).getPosition() == position) {
                sortedMenuArray.add(menuLocalArray.get(index));

                position++;
                index = 0;
            } else {
                index++;
            }

        } while (sortedMenuArray.size() < menuLocalArray.size());

        index = 0;
        position = 1;

        do {

            if (submenuLocalArray.get(index).getPosition() == position) {
                sortedSubmenuArray.add(submenuLocalArray.get(index));

                position ++;
                index = 0;
            } else {
                index++;
            }


        } while (sortedSubmenuArray.size() < submenuLocalArray.size());

        return new HelperArray(sortedMenuArray, sortedSubmenuArray);
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
