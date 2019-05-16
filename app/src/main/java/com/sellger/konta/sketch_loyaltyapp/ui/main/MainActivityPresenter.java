package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMenuArray;
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
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
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
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_DRAWER_TYPE_MENU;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_DRAWER_TYPE_SUBMENU;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;

public class MainActivityPresenter implements MainActivityContract.Presenter,
        BaseFragmentContract.Presenter {

    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    @NonNull
    private MainActivityContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private ArrayList<MenuComponent> mMenuArray = new ArrayList<>();
    private ArrayList<MenuComponent> mSubmenuArray = new ArrayList<>();

    MainActivityPresenter(@NonNull MainActivityContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getMenu(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                Log.d(TAG, "onDataLoaded: " + data.size());
                refactorFetchedData((List<MenuComponent>) data);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG, "onDataNotAvailable");
            }
        });
    }

    @Override
    public void displayHomeScreen(String layoutType) {
        displaySelectedScreen(layoutType, LAYOUT_DATA_EMPTY_STRING);
    }

    @Override
    public void refactorFetchedData(List<MenuComponent> menuComponentList) {
        int homeScreenId = 0;

        HelperMenuArray helperMenuArray = sortMenuDataList(menuComponentList);
        mMenuArray = helperMenuArray.getMenuArray();
        mSubmenuArray = helperMenuArray.getSubmenuArray();

        for (int i = 0; i < mMenuArray.size(); i++) {
            if (mMenuArray.get(i).getIsHomePage().equals(1)) {
                homeScreenId = mMenuArray.get(i).getPosition() - 1;
                break;
            }
        }

        for (int i = 0; i < mSubmenuArray.size(); i++) {
            if (mSubmenuArray.get(i).getIsHomePage().equals(1)) {
                homeScreenId = mSubmenuArray.get(i).getPosition() - 1;
                break;
            }
        }

        // Remove nav view header shade if an account is not anonymous
        if (mFirebaseAuth.getCurrentUser() != null && !mFirebaseAuth.getCurrentUser().isAnonymous()){
            view.setNavViewHeaderVisibility(NOT_ANONYMOUS_REGISTRATION);
        }

        passDataToNavDrawer(mMenuArray, mSubmenuArray, homeScreenId);
    }

    @Override
    public HelperMenuArray sortMenuDataList(List<MenuComponent> listOfItems) {
        String menuType;
        ArrayList<MenuComponent> menuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> submenuLocalArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedMenuArray = new ArrayList<>();
        ArrayList<MenuComponent> sortedSubmenuArray = new ArrayList<>();

        for (int i = 0; i < listOfItems.size(); i++) {
            menuType = listOfItems.get(i).getList();

            switch (menuType) {
                case NAV_DRAWER_TYPE_MENU:
                    menuLocalArray.add(listOfItems.get(i));
                    break;
                case NAV_DRAWER_TYPE_SUBMENU:
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

        return new HelperMenuArray(sortedMenuArray, sortedSubmenuArray);
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

        view.setDataToNavDrawer(menu, submenu, homeScreenId, iconNameArray);
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
            layoutType = mMenuArray.get(itemId).getType();
        } else {
            layoutType = mSubmenuArray.get(itemId).getType();
        }

        return layoutType;
    }

    @Override
    public void displaySelectedScreen(String layoutType, String data) {

        if (layoutType != null) {
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
                Log.d(TAG, "onNext" + viewPosition);
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
        view.setDisplayItemChecked(viewPosition);
    }
}