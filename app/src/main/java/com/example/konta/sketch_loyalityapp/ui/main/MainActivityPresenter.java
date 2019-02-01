package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomePresenter;
import com.example.konta.sketch_loyalityapp.ui.login.LogInActivity;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.products.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contact.ContactActivity;
import com.example.konta.sketch_loyalityapp.ui.terms.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.ui.website.WebsiteActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter {

    @Nullable
    private MainActivityContract.View view;
    private MainActivityContract.Model model;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    MainActivityPresenter(@Nullable MainActivityContract.View view,
                          MainActivityContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        Disposable disposable = model.fetchDataFromServer(this);
        compositeDisposable.add(disposable);
    }

    @Override
    public void displayHomeScreen() {
        if (view != null) {
            view.setFragment(new HomeFragment());
        }
    }

    @Override
    public void passDataToNavDrawer(SparseArray<HelperComponent> menu,
                               SparseArray<HelperComponent> submenu, int homeScreenId) {
        if (view != null) {
            view.setDataToNavDrawer(menu, submenu, homeScreenId);
        }
    }

    @Override
    public String getLayoutType(int groupId, int itemId) {
        String layoutType;

        if (groupId == 0) {
            layoutType = model.getMenuLayoutType(itemId);
        } else {
            layoutType = model.getSubmenuLayoutType(itemId);
        }

        return layoutType;
    }

    @Override
    public void displaySelectedScreen(String layoutType) {

        if (layoutType != null && view != null) {
            switch (layoutType) {
                case "home":
                    view.setFragment(new HomeFragment());
                    break;
                case "products":
                    view.setFragment(new ProductsFragment());
                    break;
                case "coupons":
                    view.setFragment(new CouponsFragment());
                    break;
                case "map":
                    view.setFragment(new GoogleMapFragment());
                    break;
                case "login":
                    view.setActivity(LogInActivity.class);
                    break;
                case "url":
                    view.setActivity(WebsiteActivity.class);
                    break;
                case "terms":
                    view.setActivity(TermsConditionsActivity.class);
                    break;
                case "contact":
                    view.setActivity(ContactActivity.class);
                    break;
            }
        }
    }

    @Override
    public void getSelectedLayoutType(MenuComponent item) {
        displaySelectedScreen(item.getType());
    }

    @Override
    public void setUpObservableHomeAdapter() {

        Observable<Integer> observable = HomePresenter.getObservableSelectedView();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("home", "onSubscribe");
            }

            @Override
            public void onNext(Integer viewPosition) {
                Log.d("home", "onNext" + String.valueOf(viewPosition));
                passIdOfSelectedView(viewPosition);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("home", "onError");
            }

            @Override
            public void onComplete() {
                Log.d("home", "onComplete");
            }
        };

        observable.subscribe(observer);
    }

    @Override
    public void passIdOfSelectedView(int viewPosition) {
        if (view != null) {
            view.setDisplayItemChecked(viewPosition);
        }
    }
}