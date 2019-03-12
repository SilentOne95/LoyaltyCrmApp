package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.ui.barcodeGenerator.BarcodeGenerator;
import com.example.konta.sketch_loyalityapp.ui.barcodeScanner.BarcodeScanner;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomePresenter;
import com.example.konta.sketch_loyalityapp.ui.login.LogInFragment;
import com.example.konta.sketch_loyalityapp.ui.login.phoneAuthNumber.LogInPhoneFragment;
import com.example.konta.sketch_loyalityapp.ui.login.phoneAuthCode.LogInVerifyFragment;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.products.ProductsFragment;
import com.example.konta.sketch_loyalityapp.ui.contact.ContactFragment;
import com.example.konta.sketch_loyalityapp.ui.terms.TermsFragment;
import com.example.konta.sketch_loyalityapp.ui.website.WebsiteFragment;

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
    public void displayHomeScreen(String layoutType) {
        displaySelectedScreen(layoutType, "");
    }

    @Override
    public void passDataToNavDrawer(SparseArray<HelperComponent> menu,
                                    SparseArray<HelperComponent> submenu, int homeScreenId) {
        int arraySize = menu.size() + submenu.size();
        int arrayIndex = 0;
        String[] iconNameArray = new String[arraySize];

        for (int i = 0; i < menu.size(); i++) {
            iconNameArray[arrayIndex] = matchRelevantIconName(menu.get(i).getType());
            arrayIndex++;
        }

        for (int i = 0; i < submenu.size(); i++) {
            iconNameArray[arrayIndex] = matchRelevantIconName(submenu.get(i).getType());
            arrayIndex++;
        }

        if (view != null) {
            view.setDataToNavDrawer(menu, submenu, homeScreenId, iconNameArray);
        }
    }

    @Override
    public String matchRelevantIconName(String layoutType) {
        String iconName;

        switch (layoutType) {
            case "home":
                iconName = "ic_menu_home";
                break;
            case "products":
                iconName = "ic_menu_product";
                break;
            case "coupons":
                iconName = "ic_menu_coupon";
                break;
            case "map":
                iconName = "ic_menu_map";
                break;
            case "url":
                iconName = "ic_menu_website";
                break;
            case "terms":
                iconName = "ic_menu_terms";
                break;
            case "contact":
                iconName = "ic_menu_contact";
                break;
            case "scanner":
                iconName = "ic_menu_scanner";
                break;
            default:
                iconName = "ic_menu_page";
                break;
        }

        return iconName;
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
    public void displaySelectedScreen(String layoutType, String data) {

        if (layoutType != null && view != null) {
            switch (layoutType) {
                case "home":
                    view.setFragment(new HomeFragment(), data);
                    break;
                case "products":
                    view.setFragment(new ProductsFragment(), data);
                    break;
                case "coupons":
                    view.setFragment(new CouponsFragment(), data);
                    break;
                case "map":
                    view.setFragment(new GoogleMapFragment(), data);
                    break;
                case "url":
                    view.setFragment(new WebsiteFragment(), data);
                    break;
                case "terms":
                    view.setFragment(new TermsFragment(), data);
                    break;
                case "contact":
                    view.setFragment(new ContactFragment(), data);
                    break;
                case "scanner":
                    view.setFragment(new BarcodeScanner(), data);
                    break;
                case "barcode":
                    view.setFragment(new BarcodeGenerator(), data);
                    break;

                // Registration views
                case "login":
                    view.setFragment(new LogInFragment(), data);
                    break;
                case "phone":
                    view.setFragment(new LogInPhoneFragment(), data);
                    break;
                case "code":
                    view.setFragment(new LogInVerifyFragment(), data);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void getSelectedLayoutType(String item, String data) { displaySelectedScreen(item, data); }

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