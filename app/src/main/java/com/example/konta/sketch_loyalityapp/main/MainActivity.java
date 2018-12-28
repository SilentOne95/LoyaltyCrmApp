package com.example.konta.sketch_loyalityapp.main;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.konta.sketch_loyalityapp.adapters.BottomSheetViewPagerAdapter;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.CouponsFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.HomeFragment;
import com.example.konta.sketch_loyalityapp.drawerDependentViews.ProductsFragment;
import com.example.konta.sketch_loyalityapp.independentViews.ContactActivity;
import com.example.konta.sketch_loyalityapp.independentViews.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.independentViews.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.loginViews.LogInActivity;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import static com.example.konta.sketch_loyalityapp.Constants.BOTTOM_SHEET_PEEK_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.DISPLAY_STARTING_VIEW_GROUP_ID;
import static com.example.konta.sketch_loyalityapp.Constants.DISPLAY_STARTING_VIEW_ITEM_ID;
import static com.example.konta.sketch_loyalityapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_FIRST_GROUP_ID;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_ORDER;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_SECOND_GROUP_ID;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        MainActivityContract.View {

    @Inject MainActivityContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private String json;
    GoogleMapFragment mGoogleMapFragment;

    // Fields which stores clicked menuItem IDs
    private int groupId;
    private int itemId;

    // Global field responsible for storing info which flragment should be opened
    private Fragment mFragment = null;

    // Arrays to store key-value pairs to store specified type assigned to view
    private SparseArray<String> menuSectionOneArray = new SparseArray<>();
    private SparseArray<String> menuSectionTwoArray = new SparseArray<>();

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "menu.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyApplication) getApplication()).getApplicationComponent().inject(this);

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


        // Reading JSON file from assets
        json = ((MyApplication) getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        prepareMenuData();

        // Display chosen screen as a default one after app is launched
        displaySelectedScreen(DISPLAY_STARTING_VIEW_GROUP_ID,DISPLAY_STARTING_VIEW_ITEM_ID);
        mNavigationView.getMenu().getItem(DISPLAY_STARTING_VIEW_ITEM_ID).setChecked(true).setCheckable(true);

        // Set up BottomSheet
        View mBottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheet.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(BOTTOM_SHEET_PEEK_HEIGHT);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Custom TabLayout with ViewPager set up
        ViewPager viewPager = findViewById(R.id.view_pager);
        BottomSheetViewPagerAdapter pagerAdapter = new BottomSheetViewPagerAdapter(MainActivity.this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Setting up custom TabLayout view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setView(this);
    }

    private void prepareMenuData() {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrayOne = object.getJSONArray("sectionOne");

            for (int i = 0; i < arrayOne.length(); i++) {
                JSONObject insideObj = arrayOne.getJSONObject(i);
                String title = insideObj.getString("componentTitle");
                String icon = insideObj.getString("menuIcon");

                Resources resources = this.getResources();
                final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                Menu menuOne = mNavigationView.getMenu();
                menuOne.add(NAV_VIEW_FIRST_GROUP_ID, i, NAV_VIEW_ORDER, title).setIcon(resourceId);

                // Put type of views into an array
                String type = insideObj.getString("componentType");
                menuSectionOneArray.append(i, type);
            }

            if (object.getJSONArray("sectionTwo") != null){
                JSONArray arrayTwo = object.getJSONArray("sectionTwo");
                for (int i = 0; i < arrayTwo.length(); i++) {
                    JSONObject insideObj = arrayTwo.getJSONObject(i);
                    String title = insideObj.getString("componentTitle");
                    String icon = insideObj.getString("menuIcon");

                    Resources resources = this.getResources();
                    final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                    Menu menuTwo = mNavigationView.getMenu();
                    menuTwo.add(NAV_VIEW_SECOND_GROUP_ID, i, NAV_VIEW_ORDER, title).setIcon(resourceId);

                    // Put type of views into an array
                    String type = insideObj.getString("componentType");
                    menuSectionTwoArray.append(i, type);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    protected void onStart() {
        super.onStart();
        // Entering / exiting animations for activities
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Assign menu IDs to global variables to display attached view after drawer is closed
        groupId = menuItem.getGroupId();
        itemId = menuItem.getItemId();

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

    @Override
    public void onClick(View view) {
        switch (mBottomSheetBehavior.getState()) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case BottomSheetBehavior.STATE_EXPANDED:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            default:
                break;
        }
    }

    public void changeBottomSheetState(int state) {
        if (state == 1 && mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (state == 0 && mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void displaySelectedScreen(int groupId, int itemId) {
        String layoutType;

        if (groupId == 0) {
            layoutType = menuSectionOneArray.get(itemId);
        } else {
            layoutType = menuSectionTwoArray.get(itemId);
        }

        switch (layoutType) {
            case "LogIn":
                MainActivity.this.startActivity(new Intent(MainActivity.this, LogInActivity.class));
                break;
            case "Home":
                mFragment = new HomeFragment();
                break;
            case "Products":
                mFragment = new ProductsFragment();
                break;
            case "Coupons":
                mFragment = new CouponsFragment();
                break;
            case "Map":
                mFragment = new GoogleMapFragment();
                break;
            case "Internet":
                MainActivity.this.startActivity(new Intent(MainActivity.this, WebsiteActivity.class));
                break;
            case "Terms":
                MainActivity.this.startActivity(new Intent(MainActivity.this, TermsConditionsActivity.class));
                break;
            case "Contact":
                MainActivity.this.startActivity(new Intent(MainActivity.this, ContactActivity.class));
                break;
        }

        // Replacing the fragment
        if (mFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.switch_view_layout, mFragment)
                    .commit();
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) { }

    @Override
    public void onDrawerOpened(@NonNull View view) { }

    @Override
    public void onDrawerClosed(@NonNull View view) { displaySelectedScreen(groupId, itemId); }

    @Override
    public void onDrawerStateChanged(int i) { }
}