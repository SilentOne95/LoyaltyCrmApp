package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.support.annotation.Nullable;
import android.util.Log;

import com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.camera.ScannerCameraFragment;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragmentContract;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.instruction.ScanResultFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.myAccount.MyAccountFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.coupons.CouponsFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.home.HomePresenter;
import com.sellger.konta.sketch_loyaltyapp.ui.login.LogInFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthNumber.LogInPhoneFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode.LogInVerifyFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.map.GoogleMapFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.home.HomeFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.products.ProductsFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.contact.ContactFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.settings.SettingsActivity;
import com.sellger.konta.sketch_loyaltyapp.ui.terms.TermsFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.website.WebsiteFragment;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_ACCOUNT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_CAMERA;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_CONTACT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_LOGIN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_MAP;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PHONE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_PRODUCTS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SETTINGS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_TERMS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_URL;

public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter {

    private static final String TAG = MainActivityPresenter.class.getSimpleName();

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
        displaySelectedScreen(layoutType, LAYOUT_DATA_EMPTY_STRING);
    }

    @Override
    public void passDataToNavDrawer(ArrayList<MenuComponent> menu,
                                    ArrayList<MenuComponent> submenu, int homeScreenId) {
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
            case LAYOUT_TYPE_HOME:
                iconName = "ic_menu_home";
                break;
            case LAYOUT_TYPE_PRODUCTS:
                iconName = "ic_menu_product";
                break;
            case LAYOUT_TYPE_COUPONS:
                iconName = "ic_menu_coupon";
                break;
            case LAYOUT_TYPE_MAP:
                iconName = "ic_menu_map";
                break;
            case LAYOUT_TYPE_URL:
                iconName = "ic_menu_website";
                break;
            case LAYOUT_TYPE_TERMS:
                iconName = "ic_menu_terms";
                break;
            case LAYOUT_TYPE_CONTACT:
                iconName = "ic_menu_contact";
                break;
            case LAYOUT_TYPE_SCANNER:
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
                case LAYOUT_TYPE_HOME:
                    view.setFragment(new HomeFragment(), data);
                    break;
                case LAYOUT_TYPE_PRODUCTS:
                    view.setFragment(new ProductsFragment(), data);
                    break;
                case LAYOUT_TYPE_COUPONS:
                    view.setFragment(new CouponsFragment(), data);
                    break;
                case LAYOUT_TYPE_MAP:
                    view.setFragment(new GoogleMapFragment(), data);
                    break;
                case LAYOUT_TYPE_URL:
                    view.setFragment(new WebsiteFragment(), data);
                    break;
                case LAYOUT_TYPE_TERMS:
                    view.setFragment(new TermsFragment(), data);
                    break;
                case LAYOUT_TYPE_CONTACT:
                    view.setFragment(new ContactFragment(), data);
                    break;
                case LAYOUT_TYPE_SCANNER:
                    view.setFragment(new ScanResultFragment(), data);
                    break;
                case LAYOUT_TYPE_CAMERA:
                    view.setFragment(new ScannerCameraFragment(), data);
                    break;
                case LAYOUT_TYPE_ACCOUNT:
                    view.setFragment(new MyAccountFragment(), data);
                    break;
                case LAYOUT_TYPE_SETTINGS:
                    view.setActivity(SettingsActivity.class);
                    break;

                // Registration views
                case LAYOUT_TYPE_LOGIN:
                    view.setFragment(new LogInFragment(), data);
                    break;
                case LAYOUT_TYPE_PHONE:
                    view.setFragment(new LogInPhoneFragment(), data);
                    break;
                case LAYOUT_TYPE_CODE:
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
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer viewPosition) {
                Log.d(TAG, "onNext" + String.valueOf(viewPosition));
                passIdOfSelectedView(viewPosition);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
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