package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMenuArray;
import com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.camera.ScannerCameraFragment;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragmentContract;
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
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_CONTACT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_COUPONS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_MAP;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_PRODUCTS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_TERMS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_WEBSITE;
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
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

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
    private ArrayList<MenuComponent> mAllMenuItemsArray = new ArrayList<>();

    MainActivityPresenter(@NonNull MainActivityContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link MainActivity#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     */
    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getMenu(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                refactorFetchedData((List<MenuComponent>) data);
            }

            @Override
            public void onDataNotAvailable() {
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link MainActivity#onCreate(Bundle)} if user has already signed in.
     *
     * @param layoutType string with data which screen should be opened
     */
    @Override
    public void displayHomeScreen(String layoutType) {
        displaySelectedScreen(layoutType, LAYOUT_DATA_EMPTY_STRING);
    }

    /**
     * Called from {@link MainActivity#setFragment(BaseFragment, String, String)} and
     * {@link MainActivity#setUpNavigationView(ArrayList, ArrayList, int, String[])} to pass arrays
     * with NavView items.
     */
    @Override
    public void getMenuListToLimitAccess() {
        view.limitAccessForAnonymousUser(mMenuArray, mSubmenuArray);
    }

    /**
     * Called from {@link #requestDataFromServer()} to refactor fetched data.
     *
     * @param menuComponentList of fetched items of {@link MenuComponent}
     */
    private void refactorFetchedData(List<MenuComponent> menuComponentList) {
        int homeScreenId = 0;

        // Sort fetched data and pass to separate arrays
        HelperMenuArray helperMenuArray = sortMenuDataList(menuComponentList);
        mMenuArray = helperMenuArray.getMenuArray();
        mSubmenuArray = helperMenuArray.getSubmenuArray();

        // Get id of screen which was set as "home" screen
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

        mAllMenuItemsArray.addAll(mMenuArray);
        mAllMenuItemsArray.addAll(mSubmenuArray);

        passDataToNavDrawer(mMenuArray, mSubmenuArray, homeScreenId);
    }

    /**
     * Called from {@link #refactorFetchedData(List)} to sort all items based on menu section
     * (first or second) and target position in related menu section.
     *
     * @param listOfItems of {@link MenuComponent}
     * @return {@link HelperMenuArray} with two sorted menu arrays
     */
    private HelperMenuArray sortMenuDataList(List<MenuComponent> listOfItems) {
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

    /**
     * Called from {@link #refactorFetchedData(List)} to prepare data to set in NavView.
     * This method assigns relevant icon to every menu item and pass all data to view.
     *
     * @param menu is array contains menu items of 'first section'
     * @param submenu is array contains menu items of 'second section'
     * @param homeScreenId contains id of screen chosen as 'home' screen
     */
    private void passDataToNavDrawer(ArrayList<MenuComponent> menu,
                                    ArrayList<MenuComponent> submenu, int homeScreenId) {
        int arraySize = menu.size() + submenu.size();
        int arrayIndex = 0;
        String[] iconNameArray = new String[arraySize];

        // Get layout type and match with icons which should be assign to menu item
        for (int i = 0; i < menu.size(); i++) {
            iconNameArray[arrayIndex] = matchRelevantIconName(menu.get(i).getType());
            arrayIndex++;
        }

        for (int i = 0; i < submenu.size(); i++) {
            iconNameArray[arrayIndex] = matchRelevantIconName(submenu.get(i).getType());
            arrayIndex++;
        }

        view.setUpNavigationView(menu, submenu, homeScreenId, iconNameArray);
    }

    /**
     * Called from {@link #passDataToNavDrawer(ArrayList, ArrayList, int)} to match fetched menu
     * layout types with icon names available in the app.
     *
     * @param layoutType of item we are going to assign icon
     * @return icon name
     */
    private String matchRelevantIconName(String layoutType) {
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

    /**
     * Called from {@link MainActivity#onNavigationItemSelected(MenuItem)} to get type of layout
     * with should be opened next, based on ID's assigned to selected menu item in NavView.
     *
     * @param groupId of selected menu item
     * @param itemId of selected menu item
     * @return layout type of selected menu item
     */
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

    /**
     * Called from {@link #getSelectedLayoutType(String, String)}, {@link #displayHomeScreen(String)},
     * {@link MainActivity#onBackPressed()}, {@link MainActivity#onDrawerClosed(View)} and
     * {@link MainActivity#onMenuItemClick(MenuItem)} to set new fragment based on passed parameter.
     *
     * @param layoutType of screen that should be opened
     * @param data which is passed to {@link MainActivity#setFragment(BaseFragment, String, String)}
     */
    @Override
    public void displaySelectedScreen(String layoutType, String data) {
        if (layoutType != null) {
            switch (layoutType) {
                case LAYOUT_TYPE_HOME:
                    view.setFragment(new HomeFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_PRODUCTS:
                    view.setFragment(new ProductsFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_COUPONS:
                    view.setFragment(new CouponsFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_MAP:
                    view.setFragment(new GoogleMapFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_URL:
                    view.setFragment(new WebsiteFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_TERMS:
                    view.setFragment(new TermsFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_CONTACT:
                    view.setFragment(new ContactFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_SCANNER:
                    view.setFragment(new ScanResultFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_CAMERA:
                    view.setFragment(new ScannerCameraFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_ACCOUNT:
                    view.setFragment(new MyAccountFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_SETTINGS:
                    view.setActivity(SettingsActivity.class);
                    break;

                // Registration views
                case LAYOUT_TYPE_LOGIN:
                    view.setFragment(new LogInFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_PHONE:
                    view.setFragment(new LogInPhoneFragment(), getLayoutTitle(layoutType), data);
                    break;
                case LAYOUT_TYPE_CODE:
                    view.setFragment(new LogInVerifyFragment(), getLayoutTitle(layoutType), data);
                    break;
            }
        }
    }

    /**
     * Called from {@link #displaySelectedScreen(String, String)} to get layout title of fragment
     * which is going to be opened.
     *
     * @param layoutType of fragment is going to be opened
     * @return layout title of fragment
     */
    private String getLayoutTitle(String layoutType) {
        String layoutName = "";
        for(MenuComponent component : mAllMenuItemsArray) {
            if (component.getType().equals(layoutType)) {
                layoutName = component.getComponentTitle();
                break;
            }
        }

        return layoutName;
    }

    /**
     * Called from callback listener implemented in {@link HomeFragment} which enable switching fragments
     * from inside fragment.
     *
     * @param item is a layout type
     * @param data is additional data which can be passed
     */
    @Override
    public void getSelectedLayoutType(String item, String data) { displaySelectedScreen(item, data); }

    /**
     * Called from {@link MainActivity#onCreate(Bundle)} to set up Observer which listen which item
     * was clicked in {@link HomeFragment} and is going to be opened with
     * method {@link #getSelectedLayoutType(String, String)}.
     */
    @Override
    public void setUpObservableHomeAdapter() {
        Observable<Integer> observable = HomePresenter.getObservableSelectedView();
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Integer viewPosition) {
                passIdOfSelectedView(viewPosition);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        };

        observable.subscribe(observer);
    }

    /**
     * Called from {@link #getLayoutTypeOfSelectedScreen(String)}, {@link #setUpObservableHomeAdapter()}
     * and {@link MainActivity#onBackPressed()} to set relevant item in NavView as checked.
     *
     * @param viewPosition represents position of item that is going to be set as checked
     */
    @Override
    public void passIdOfSelectedView(int viewPosition) {
        view.setDisplayItemChecked(viewPosition);
    }

    /**
     * Called from callback listener implemented in {@link MainActivity#onCreate(Bundle)} whenever back
     * button is pressed, so fragment could switch and it's necessary to set relevant menu item as checked.
     *
     * @param displayedLayoutName of displayed fragment
     */
    @Override
    public void getLayoutTypeOfSelectedScreen(String displayedLayoutName) {
        String layoutType = "";
        int menuIdToSelect = 0;
        switch (displayedLayoutName) {
            case LAYOUT_NAME_HOME:
                layoutType = LAYOUT_TYPE_HOME;
                break;
            case LAYOUT_NAME_PRODUCTS:
                layoutType = LAYOUT_TYPE_PRODUCTS;
                break;
            case LAYOUT_NAME_COUPONS:
                layoutType = LAYOUT_TYPE_COUPONS;
                break;
            case LAYOUT_NAME_MAP:
                layoutType = LAYOUT_TYPE_MAP;
                break;
            case LAYOUT_NAME_SCANNER:
                layoutType = LAYOUT_TYPE_SCANNER;
                break;
            case LAYOUT_NAME_WEBSITE:
                layoutType = LAYOUT_TYPE_URL;
                break;
            case LAYOUT_NAME_TERMS:
                layoutType = LAYOUT_TYPE_TERMS;
                break;
            case LAYOUT_NAME_CONTACT:
                layoutType = LAYOUT_TYPE_CONTACT;
                break;
        }

        for (int i = 0; mAllMenuItemsArray.size() > i; i++) {
            if (mAllMenuItemsArray.get(i).getType().equals(layoutType)) {
                menuIdToSelect = i;
                break;
            }
        }

        passIdOfSelectedView(menuIdToSelect);
    }
}