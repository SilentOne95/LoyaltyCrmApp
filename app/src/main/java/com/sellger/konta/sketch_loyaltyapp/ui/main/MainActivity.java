package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.service.TrackerService;
import com.sellger.konta.sketch_loyaltyapp.ui.myAccount.MyAccount;
import com.sellger.konta.sketch_loyaltyapp.ui.login.LogInFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthNumber.LogInPhoneFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode.LogInVerifyFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.map.GoogleMapFragment;
import com.sellger.konta.sketch_loyaltyapp.root.MyApplication;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_FIRST_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_ORDER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_SECOND_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_THIRD_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    MainActivityPresenter presenter;

    private FirebaseAuth mFirebaseAuth;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    GoogleMapFragment mGoogleMapFragment;

    private View mNavViewHeaderShadeContainer;
    private Button mNavViewHeaderButton;

    // Field that stores layout type of clicked menu item
    private String layoutType = null;

    public static String PACKAGE_NAME;

    @Override
    protected int getLayout() { return R.layout.activity_main; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        // Check if GPS permission is granted - if so, start TrackingService
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startService(new Intent(this, TrackerService.class));
        }

        ((MyApplication) getApplication()).getApplicationComponent().inject(this);
        PACKAGE_NAME = getApplication().getPackageName();

        // Init views
        initViews();

        // Set up Toolbar, ActionBar, DrawerLayout, NavigationView
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mDrawerLayout.addDrawerListener(this);

        mNavigationView.setNavigationItemSelectedListener(this);
        hideNavDrawerScrollbar();

        mNavViewHeaderButton.setOnClickListener(this);

        // Set up presenter
        presenter = new MainActivityPresenter(this, new MainActivityModel());
        presenter.requestDataFromServer();
        presenter.setUpObservableHomeAdapter();

        // Remove nav view header shade if an account is not anonymous
        if (mFirebaseAuth.getCurrentUser() != null && !mFirebaseAuth.getCurrentUser().isAnonymous()){
            setNavViewHeaderVisibility(NOT_ANONYMOUS_REGISTRATION);
        }

        // Display relevant view based on whether user is already logged or not
        // TODO:
        // Replace hardcoded variables
        if (mFirebaseAuth.getCurrentUser() != null) {
            presenter.displayHomeScreen("home");
        } else {
            presenter.displayHomeScreen("login");
        }

        // Using Retrofit to set up NavDrawer
//        showInternetConnectionResult();
    }

    @Override
    public void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        View navigationViewHeader = mNavigationView.getHeaderView(0);
        mNavViewHeaderShadeContainer = navigationViewHeader.findViewById(R.id.navigation_view_header_shade_container);
        mNavViewHeaderButton = navigationViewHeader.findViewById(R.id.navigation_header_button);
    }

    protected boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void showInternetConnectionResult() {
        if (checkInternetConnection()) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setActivity(Class<? extends Activity> activity) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, activity));
    }

    @Override
    public void setFragment(BaseFragment fragment, String data) {
        fragment.attachPresenter(presenter);

        // Adding data
        if (!data.equals("") && !data.equals(ANONYMOUS_REGISTRATION)
                && !data.equals(NOT_ANONYMOUS_REGISTRATION) ) {
            Bundle bundle = new Bundle();
            bundle.putString("DATA_STRING", data);
            fragment.setArguments(bundle);
        } else if (data.equals(ANONYMOUS_REGISTRATION) || data.equals(NOT_ANONYMOUS_REGISTRATION)) {
            setNavViewHeaderVisibility(data);
        }

        // Replacing the fragment
        if (fragment instanceof LogInPhoneFragment || fragment instanceof LogInVerifyFragment
                || fragment instanceof MyAccount) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                    .replace(R.id.switch_view_layout, fragment)
                    .commit();
        } else if (fragment instanceof LogInFragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.switch_view_layout, fragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                    .replace(R.id.switch_view_layout, fragment)
                    .commit();
        }
    }

    @Override
    public void setLogInFragment(BaseFragment fragment) {
        fragment.attachPresenter(presenter);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.switch_view_layout, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // If back button is pressed on certain view, set up desired view
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.switch_view_layout);
        if (fragment instanceof LogInPhoneFragment || fragment instanceof  LogInVerifyFragment) {
            setLogInFragment(new LogInFragment());
        } else if (fragment instanceof LogInFragment && mFirebaseAuth.getCurrentUser() != null) {
            // TODO:
            // Replace hardcoded variables
            presenter.displaySelectedScreen("home", "");
            presenter.passIdOfSelectedView(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void hideNavDrawerScrollbar() {
        if (mNavigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @Override
    public void setDataToNavDrawer(ArrayList<MenuComponent> menuSectionArray,
                                   ArrayList<MenuComponent> submenuSectionArray,
                                   int homeScreenId, String[] iconNameArray) {
        Resources resources = this.getResources();
        Menu menu = mNavigationView.getMenu();
        int arrayIndex = 0;

        for (int i = 0; i < menuSectionArray.size(); i++) {
            menu.add(NAV_VIEW_FIRST_GROUP_ID, i, NAV_VIEW_ORDER,
                    menuSectionArray.get(i).getComponentTitle())
                    .setIcon(resources.getIdentifier(iconNameArray[arrayIndex], "drawable", PACKAGE_NAME));
            arrayIndex++;
        }

        for (int i = 0; i < submenuSectionArray.size(); i++) {
            menu.add(NAV_VIEW_SECOND_GROUP_ID, i, NAV_VIEW_ORDER,
                    submenuSectionArray.get(i).getComponentTitle())
                    .setIcon(resources.getIdentifier(iconNameArray[arrayIndex], "drawable", PACKAGE_NAME));
            arrayIndex++;
        }

        menu.add(NAV_VIEW_THIRD_GROUP_ID, 0, NAV_VIEW_ORDER, R.string.menu_nav_view_settings_text).setIcon(R.drawable.ic_menu_options);

        // Set checked home screen in Navigation Drawer
        mNavigationView.getMenu().getItem(homeScreenId).setChecked(true).setCheckable(true);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Assign clicked menuItem IDs and layout type to global variables
        if (!menuItem.isChecked()) {
            if (menuItem.getGroupId() == 2) {
                layoutType = "settings";
            } else {
                layoutType = presenter.getLayoutType(menuItem.getGroupId(), menuItem.getItemId());
            }
        } else {
            layoutType = null;
        }

        // Uncheck all checked menu items
        uncheckItemsNavDrawer();

        // Set item as selected to persist highlight
        menuItem.setChecked(true).setCheckable(true);

        // Close drawer after delay when item is tapped
        new Handler().postDelayed(() -> mDrawerLayout.closeDrawer(GravityCompat.START), 200);

        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) { }

    @Override
    public void onDrawerOpened(@NonNull View view) { }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        presenter.displaySelectedScreen(layoutType, "");
        // Set layoutType to null to avoid creating new instance of fragment, when closing nav drawer
        // by clicking next to the view
        layoutType = null;
//        showInternetConnectionResult();
    }

    @Override
    public void onDrawerStateChanged(int i) { }

    @Override
    public void setDisplayItemChecked(int viewPosition) {
        // Uncheck all checked menu items
        uncheckItemsNavDrawer();

        // Set checked item related to selected screen in Navigation Drawer
        mNavigationView.getMenu().getItem(viewPosition).setChecked(true).setCheckable(true);
    }

    @Override
    public void uncheckItemsNavDrawer() {
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            mGoogleMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_menu_my_account:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.switch_view_layout);
                if (mFirebaseAuth.getCurrentUser().isAnonymous()) {
                    presenter.displaySelectedScreen("login", "");
                } else if (!(fragment instanceof MyAccount)) {
                    presenter.displaySelectedScreen("barcode", "");
                    uncheckItemsNavDrawer();
                }
                break;
            case R.id.main_menu_options:
                presenter.displaySelectedScreen("settings", "");
                break;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        layoutType = "login";

        // Close drawer after delay when item is tapped
        new Handler().postDelayed(() -> mDrawerLayout.closeDrawer(GravityCompat.START), 200);
    }

    @Override
    public void setNavViewHeaderVisibility(String isAccountAnonymous) {
        switch (isAccountAnonymous) {
            case NOT_ANONYMOUS_REGISTRATION:
                mNavViewHeaderShadeContainer.setVisibility(View.GONE);
                break;
            case ANONYMOUS_REGISTRATION:
                mNavViewHeaderShadeContainer.setVisibility(View.VISIBLE);
                break;
        }
    }
}