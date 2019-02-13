package com.example.konta.sketch_loyalityapp.ui.main;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.example.konta.sketch_loyalityapp.base.BaseFragmentContract;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.barcodeGenerator.BarcodeGenerator;
import com.example.konta.sketch_loyalityapp.ui.barcodeScanner.BarcodeScanner;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomePresenter;
import com.example.konta.sketch_loyalityapp.ui.login.LogInActivity;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.ui.home.HomeFragment;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo.ContactInfoFragment;
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
                iconName = "ic_menu_cube";
                break;
            case "coupons":
                iconName = "ic_menu_coupon";
                break;
            case "map":
                iconName = "ic_menu_marker";
                break;
            case "login":
                iconName = "ic_menu_account";
                break;
            case "url":
                iconName = "ic_menu_search";
                break;
            case "terms":
                iconName = "ic_menu_terms";
                break;
            case "contact":
                iconName = "ic_menu_phone";
                break;
            case "scanner":
                iconName = "ic_menu_barcode_scan";
                break;
            case "barcode":
                iconName = "ic_menu_account";
                break;
            default:
                iconName = "ic_menu_script";
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
                case "scanner":
                    view.setFragment(new BarcodeScanner());
                    break;
                case "barcode":
                    view.setFragment(new BarcodeGenerator());
                    break;
                default:
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