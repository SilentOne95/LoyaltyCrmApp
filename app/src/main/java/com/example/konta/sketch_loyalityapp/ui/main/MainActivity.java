package com.example.konta.sketch_loyalityapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.menu.HelperComponent;
import com.example.konta.sketch_loyalityapp.ui.map.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import static com.example.konta.sketch_loyalityapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_FIRST_GROUP_ID;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_ORDER;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_SECOND_GROUP_ID;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View {

    MainActivityPresenter presenter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    GoogleMapFragment mGoogleMapFragment;

    // Field that stores layout type of clicked menu item
    private String layoutType;

    public static String PACKAGE_NAME;

    @Override
    protected int getLayout() { return R.layout.activity_main; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getApplicationComponent().inject(this);
        PACKAGE_NAME = getApplication().getPackageName();

        // Init toolbar and action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Init drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(this);

        // Init navigation view
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Using Retrofit to set up NavDrawer
        presenter = new MainActivityPresenter(this, new MainActivityModel());
        presenter.requestDataFromServer();
        presenter.displayHomeScreen();
    }

    @Override
    public void setActivity(Class<? extends Activity> activity) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, activity));
    }

    @Override
    public void setFragment(BaseFragment fragment) {

        fragment.attachPresenter(presenter);

        // Replacing the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.switch_view_layout, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Entering / exiting animations for activities
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) { }

    @Override
    public void onDrawerOpened(@NonNull View view) { }

    @Override
    public void onDrawerClosed(@NonNull View view) { presenter.displaySelectedScreen(layoutType); }

    @Override
    public void onDrawerStateChanged(int i) { }

    @Override
    public void setDisplayScreenChecked(String layoutType) {
        // TODO: Fix logic
    }

    @Override
    public void setDataToNavDrawer(SparseArray<HelperComponent> menuSectionArray,
                                   SparseArray<HelperComponent> submenuSectionArray,
                                   int homeScreenId) {

        Menu menu = mNavigationView.getMenu(), submenu = mNavigationView.getMenu();

        for (int i = 0; i < menuSectionArray.size(); i++) {
                menu.add(NAV_VIEW_FIRST_GROUP_ID, i, NAV_VIEW_ORDER, menuSectionArray.get(i).getTitle());
        }

        for (int i = 0; i < submenuSectionArray.size(); i++) {
                submenu.add(NAV_VIEW_SECOND_GROUP_ID, i, NAV_VIEW_ORDER, submenuSectionArray.get(i).getTitle());
        }

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
        layoutType = presenter.getLayoutType(menuItem.getGroupId(), menuItem.getItemId());

        // Uncheck all checked menu items
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }

        // Set item as selected to persist highlight
        menuItem.setChecked(true).setCheckable(true);

        // Close drawer when item is tapped
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
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
}