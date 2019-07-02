package com.sellger.konta.sketch_loyaltyapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.instruction.ScanResultFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.myAccount.MyAccountFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.LogInFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthNumber.LogInPhoneFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthCode.LogInVerifyFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.map.GoogleMapFragment;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static com.sellger.konta.sketch_loyaltyapp.Constants.ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_DATA_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_DRAWER_ACTION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_LOGIN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_PHONE_CODE;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_PHONE_NUM;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_NAME_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_ACCOUNT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_HOME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_LOGIN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SETTINGS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_FIRST_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_ORDER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_SECOND_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NAV_VIEW_THIRD_GROUP_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.NOT_ANONYMOUS_REGISTRATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class MainActivity extends BaseActivity implements MainActivityContract.View,
        DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityPresenter presenter;

    private FirebaseAuth mFirebaseAuth;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    GoogleMapFragment mGoogleMapFragment;

    private View mNavViewHeaderShadeContainer;
    private Button mNavViewHeaderButton;

    // Field that stores layout type of clicked menu item
    private String mLayoutType = null;

    // Helper variable for selecting relevant item in NavDrawer after popBackStack
    private int mBackStackCounter = 0;

    public static String PACKAGE_NAME;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.useAppLanguage();

        PACKAGE_NAME = getApplication().getPackageName();

        // Init views
        initViews();

        // Set up presenter
        presenter = new MainActivityPresenter(this, Injection.provideLoyaltyRepository(getApplicationContext()));
        presenter.requestDataFromServer();
        presenter.setUpObservableHomeAdapter();

        // Display relevant view based on whether user is already logged or not
        if (mFirebaseAuth.getCurrentUser() != null) {
            presenter.displayHomeScreen(LAYOUT_TYPE_HOME);
        } else {
            presenter.displayHomeScreen(LAYOUT_TYPE_LOGIN);
        }

        // TODO: Testing solution for back fragment transaction and selecting relevant item in NavView
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() < mBackStackCounter) {
                presenter.getLayoutTypeOfSelectedScreen(getSupportFragmentManager().findFragmentById(R.id.switch_view_layout).getClass().getSimpleName());
            }
            mBackStackCounter = getSupportFragmentManager().getBackStackEntryCount();
        });
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        View navigationViewHeader = mNavigationView.getHeaderView(0);
        mNavViewHeaderShadeContainer = navigationViewHeader.findViewById(R.id.navigation_view_header_shade_container);
        mNavViewHeaderButton = navigationViewHeader.findViewById(R.id.navigation_header_button);

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
    }

    /**
     * Called from {@link #onCreate(Bundle)} to hide scrollbar in NavDrawer view.
     */
    private void hideNavDrawerScrollbar() {
        // If NavView is not null, hide scrollbar
        if (mNavigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    /**
     * Populates menu with custom layout.
     *
     * @param menu is the options menu in which you place items
     * @return true for the menu to be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Called from {@link MainActivityPresenter#displaySelectedScreen(String, String)} to open relevant
     * activity.
     *
     * @param activity which is going to be opened
     */
    @Override
    public void setActivity(Class<? extends Activity> activity) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, activity));
    }

    /**
     * Called from {@link MainActivityPresenter#displaySelectedScreen(String, String)} to open relevant
     * fragment.
     *
     * @param fragment      which is going to be opened
     * @param fragmentTitle of fragment
     * @param data          passed from previous screen. Depends on needs, it can be information if user is
     *                      logged anonymously or it's just data passed to next screen
     */
    @Override
    public void setFragment(BaseFragment fragment, String fragmentTitle, String data) {
        fragment.attachPresenter(presenter);
        Bundle bundle = new Bundle();

        // Adding layout title
        if (!fragmentTitle.equals("")) {
            bundle.putString(BUNDLE_TITLE_STRING, fragmentTitle);
            fragment.setArguments(bundle);
        }

        // Adding data
        if (!data.equals("") && !data.equals(ANONYMOUS_REGISTRATION)
                && !data.equals(NOT_ANONYMOUS_REGISTRATION)) {
            bundle.putString(BUNDLE_DATA_STRING, data);
            fragment.setArguments(bundle);
        } else if (data.equals(ANONYMOUS_REGISTRATION) || data.equals(NOT_ANONYMOUS_REGISTRATION)) {
            setNavViewHeaderVisibility(data);
        }

        // Replacing the fragment
        if (fragment instanceof LogInPhoneFragment || fragment instanceof LogInVerifyFragment) {
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
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragment.getClass().getSimpleName(), 0);
            if (!fragmentPopped) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                        .replace(R.id.switch_view_layout, fragment)
                        .addToBackStack(fragment.getClass().getSimpleName())
                        .commit();
            }
        }
    }

    /**
     * Called from {@link #setLogInFragment(BaseFragment)} to open log in screen in certain situation
     * when onBackPressed is triggered.
     *
     * @param fragment which is going to be opened
     */
    private void setLogInFragment(BaseFragment fragment) {
        fragment.attachPresenter(presenter);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.switch_view_layout, fragment)
                .commit();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        // If back button is pressed on certain view, set up desired view
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.switch_view_layout);
        switch (fragment.getClass().getSimpleName()) {
            case LAYOUT_NAME_PHONE_NUM:
                setLogInFragment(new LogInFragment());
                break;
            case LAYOUT_NAME_PHONE_CODE:
                setLogInFragment(new LogInFragment());
                break;
            case LAYOUT_NAME_LOGIN:
                if (mFirebaseAuth.getCurrentUser() != null) {
                    presenter.displaySelectedScreen(LAYOUT_TYPE_HOME, LAYOUT_DATA_EMPTY_STRING);
                    presenter.passIdOfSelectedView(0);
                }
                break;
            case LAYOUT_NAME_SCANNER:
                presenter.displaySelectedScreen(LAYOUT_TYPE_SCANNER, LAYOUT_DATA_EMPTY_STRING);
                break;
            default:
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
                break;
        }
    }

    /**
     * Called from {@link MainActivityPresenter#passDataToNavDrawer(ArrayList, ArrayList, int)} to refactor
     * and set data in NavDrawer view.
     *
     * @param menuSectionArray    of NavDrawer menu data which is going to be set in first 'section'
     * @param submenuSectionArray of NavDrawer submenu data which is going to be set just below menu data
     * @param homeScreenId        is an int ID of screen which was chosen to be a 'home screen' of the app
     * @param iconNameArray       of strings that contain icon name for every menu item in NavDrawer view
     */
    @Override
    public void setDataToNavDrawer(ArrayList<MenuComponent> menuSectionArray,
                                   ArrayList<MenuComponent> submenuSectionArray,
                                   int homeScreenId, String[] iconNameArray) {
        Resources resources = this.getResources();
        Menu menu = mNavigationView.getMenu();
        int arrayIndex = 0;

        // Get title, icon name and set up NavView menu items
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

        // Set additional menu item group, which is options screen
        menu.add(NAV_VIEW_THIRD_GROUP_ID, 0, NAV_VIEW_ORDER, R.string.menu_nav_view_settings_text).setIcon(R.drawable.ic_menu_options);

        // Set home screen as checked in NavView
        mNavigationView.getMenu().getItem(homeScreenId).setChecked(true).setCheckable(true);
    }

    /**
     * This hook is called whenever an item in options menu is selected.
     *
     * @param item is a selected item
     * @return false to allow normal menu processing to proceed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Listener for handling events on navigation items.
     *
     * @param menuItem is an item which was clicked
     * @return true to display the item as the selected one
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Assign clicked menuItem IDs and layout type to global variables to pass them as arguments
        // in another method that is responsible for displaying new screen
        if (!menuItem.isChecked()) {
            if (menuItem.getGroupId() == 2) {
                mLayoutType = LAYOUT_TYPE_SETTINGS;
            } else {
                mLayoutType = presenter.getLayoutType(menuItem.getGroupId(), menuItem.getItemId());
            }
        } else {
            mLayoutType = null;
        }

        // Uncheck all checked menu items
        uncheckItemsNavView();

        // Set item as selected to persist highlight
        menuItem.setChecked(true).setCheckable(true);

        // Close drawer after delay when item is tapped
        new Handler().postDelayed(() -> mDrawerLayout.closeDrawer(GravityCompat.START), DELAY_DRAWER_ACTION);

        return true;
    }

    /**
     * One of four listeners for monitoring events about drawers, which is called when a drawer has
     * settled in a completely closed state.
     *
     * @param view of the drawer
     */
    @Override
    public void onDrawerClosed(@NonNull View view) {
        presenter.displaySelectedScreen(mLayoutType, LAYOUT_DATA_EMPTY_STRING);
        // Set layoutType to null to avoid creating new instance of fragment, when closing nav drawer
        // by clicking next to the view
        mLayoutType = null;
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
    }

    @Override
    public void onDrawerStateChanged(int i) {
    }

    /**
     * Called from {@link MainActivityPresenter#passIdOfSelectedView(int)} to set checked menu item
     * in NavDrawer.
     *
     * @param viewPosition of the menu item which should be set as checked
     */
    @Override
    public void setDisplayItemChecked(int viewPosition) {
        // Uncheck all checked menu items
        uncheckItemsNavView();

        // Set menu item with given position as checked in NavView
        mNavigationView.getMenu().getItem(viewPosition).setChecked(true).setCheckable(true);
    }

    /**
     * Called from {@link #setDisplayItemChecked(int)} to uncheck all previous selected menu items.
     */
    private void uncheckItemsNavView() {
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    /**
     * Retrieves the results for permission requests in {@link GoogleMapFragment} and {@link ScanResultFragment}.
     *
     * @param requestCode  is an int of permission that was requested
     * @param permissions  that were requested
     * @param grantResults are results for the corresponding permissions which is either granted or denied
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            mGoogleMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Interface definition for a callback to be invoked when a menu item is clicked.
     *
     * @param menuItem which was invoked
     * @return boolean whether other callbacks should be executed or not
     */
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // Assign relevant action based on which option was clicked
        switch (menuItem.getItemId()) {
            case R.id.main_menu_my_account:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.switch_view_layout);
                if (mFirebaseAuth.getCurrentUser().isAnonymous()) {
                    presenter.displaySelectedScreen(LAYOUT_TYPE_LOGIN, LAYOUT_DATA_EMPTY_STRING);
                } else if (!(fragment instanceof MyAccountFragment)) {
                    presenter.displaySelectedScreen(LAYOUT_TYPE_ACCOUNT, LAYOUT_DATA_EMPTY_STRING);
                    uncheckItemsNavView();
                }
                break;
            case R.id.main_menu_options:
                presenter.displaySelectedScreen(LAYOUT_TYPE_SETTINGS, LAYOUT_DATA_EMPTY_STRING);
                break;
        }

        return false;
    }

    /**
     * Called when a view has been clicked.
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     *
     * @param view which was clicked
     */
    @Override
    public void onClick(View view) {
        mLayoutType = LAYOUT_TYPE_LOGIN;

        // Close drawer after delay when item is tapped
        new Handler().postDelayed(() -> mDrawerLayout.closeDrawer(GravityCompat.START), DELAY_DRAWER_ACTION);
    }

    /**
     * Called from {@link #setFragment(BaseFragment, String, String)} to set visibility of certain view
     * placed in NavViewHeader depends on whether account is anonymous or not.
     *
     * @param isAccountAnonymous string of data passed to {@link #setFragment(BaseFragment, String, String)}
     *                           with information about registration method
     */
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

    /**
     * Called from {@link MainActivityPresenter#requestDataFromServer()} whenever data is
     * unavailable to get.
     *
     * @param message is a string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}