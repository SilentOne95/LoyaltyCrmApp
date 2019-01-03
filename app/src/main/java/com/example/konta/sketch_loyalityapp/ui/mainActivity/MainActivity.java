package com.example.konta.sketch_loyalityapp.ui.mainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import com.example.konta.sketch_loyalityapp.ui.mapFragment.GoogleMapFragment;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.konta.sketch_loyalityapp.Constants.DISPLAY_STARTING_VIEW_GROUP_ID;
import static com.example.konta.sketch_loyalityapp.Constants.DISPLAY_STARTING_VIEW_ITEM_ID;
import static com.example.konta.sketch_loyalityapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_FIRST_GROUP_ID;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_ORDER;
import static com.example.konta.sketch_loyalityapp.Constants.NAV_VIEW_SECOND_GROUP_ID;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener, MainActivityContract.View {

    MainActivityPresenter mPresenter;
    SwitchLayoutPresenter mSwitchLayoutPresenter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String json;
    GoogleMapFragment mGoogleMapFragment;

    // Fields which stores clicked menuItem IDs
    private int groupId = DISPLAY_STARTING_VIEW_GROUP_ID;
    private int itemId = DISPLAY_STARTING_VIEW_ITEM_ID;
    private String layoutType;

    // Arrays to store key-value pairs to store specified type assigned to view
    public SparseArray<String> menuSectionOneArray = new SparseArray<>();
    public SparseArray<String> menuSectionTwoArray = new SparseArray<>();

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "menu.json";
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


        // Reading JSON file from assets
        json = ((MyApplication) getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        prepareMenuData();

        // Set home screen selected in navigation drawer
        mNavigationView.getMenu().getItem(itemId).setChecked(true).setCheckable(true);

        mPresenter = new MainActivityPresenter(this);
        mPresenter.displayHomeScreen();
        mSwitchLayoutPresenter = new SwitchLayoutPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNavigationView.getMenu().getItem(DISPLAY_STARTING_VIEW_ITEM_ID).setChecked(true).setCheckable(true);
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
    protected void onStart() {
        super.onStart();
        // Entering / exiting animations for activities
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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
        groupId = menuItem.getGroupId();
        itemId = menuItem.getItemId();

        if (groupId == 0) {
            layoutType = menuSectionOneArray.get(itemId);
        } else {
            layoutType = menuSectionTwoArray.get(itemId);
        }

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
    public void onDrawerSlide(@NonNull View view, float v) { }

    @Override
    public void onDrawerOpened(@NonNull View view) { }

    @Override
    public void onDrawerClosed(@NonNull View view) { mSwitchLayoutPresenter.displaySelectedScreen(groupId, itemId, layoutType); }

    @Override
    public void onDrawerStateChanged(int i) { }

    @Override
    public void setFragment(BaseFragment fragment) {

        fragment.attachPresenter(mSwitchLayoutPresenter);

        // Replacing the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.switch_view_layout, fragment)
                .commit();

    }

    @Override
    public void setActivity(Class<? extends Activity> activity) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, activity));
    }
}