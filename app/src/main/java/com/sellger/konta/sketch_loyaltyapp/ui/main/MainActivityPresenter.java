package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMenuArray;
import com.sellger.konta.sketch_loyaltyapp.service.network.NetworkSchedulerService;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void scheduleJob(Context context) {
        JobInfo jobInfo = new JobInfo.Builder(0, new ComponentName(context, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    // TODO: Temporary workaround
    @Override
    public void startNetworkIntentService(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(context, NetworkSchedulerService.class);
            context.startService(startServiceIntent);
            new Handler().postDelayed(this::setUpNetworkObservable, 3000);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setUpNetworkObservable() {
        Observable<Boolean> observable = NetworkSchedulerService.getObservable();
        Observer<Boolean> onMarkerClickObserver = new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(Boolean aBoolean) {
                view.displaySnackbar(aBoolean);
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        };

        observable.subscribe(onMarkerClickObserver);
    }

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

        mAllMenuItemsArray.addAll(mMenuArray);
        mAllMenuItemsArray.addAll(mSubmenuArray);

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

    @Override
    public String getLayoutTitle(String layoutType) {
        String layoutName = "";
        for(MenuComponent component : mAllMenuItemsArray) {
            if (component.getType().equals(layoutType)) {
                layoutName = component.getComponentTitle();
                break;
            }
        }

        return layoutName;
    }

    @Override
    public void getSelectedLayoutType(String item, String data) { displaySelectedScreen(item, data); }

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

    @Override
    public void passIdOfSelectedView(int viewPosition) {
        view.setDisplayItemChecked(viewPosition);
    }

    // TODO:
    @Override
    public void matchRelevantLayoutType(String displayedLayoutName) {
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