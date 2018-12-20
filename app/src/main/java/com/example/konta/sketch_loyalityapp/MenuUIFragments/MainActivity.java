package com.example.konta.sketch_loyalityapp.MenuUIFragments;

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

import com.example.konta.sketch_loyalityapp.Adapters.BottomSheetViewPagerAdapter;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.ContactActivity;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.LoginUI.LogInActivity;
import com.example.konta.sketch_loyalityapp.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String json;
    GoogleMapFragment mGoogleMapFragment;
    private BottomSheetBehavior mBottomSheetBehavior;


    // Arrays to store key-value pairs to store specified type assigned to view
    private SparseArray<String> menuSectionOneArray = new SparseArray<>();
    private SparseArray<String> menuSectionTwoArray = new SparseArray<>();

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "menu.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        // Reading JSON file from assets
        json = ((MyApplication) getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        prepareMenuData();

        // Display chosen screen as a default one after app is launched
        displaySelectedScreen(0,1);
        mNavigationView.getMenu().getItem(1).setChecked(true).setCheckable(true);

        // Set up BottomSheet
        View mBottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheet.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(200);
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
                menuOne.add(0, i, 0, title).setIcon(resourceId);

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
                    menuTwo.add(1, i, 0, title).setIcon(resourceId);

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

    private void displaySelectedScreen(int groupId, int itemId) {
        // Initializing the fragment object which is selected
        if (groupId == 0){
            switch (itemId) {
                case 0:
                    getRelevantLayoutType(0,0);
                    break;
                case 1:
                    getRelevantLayoutType(0,1);
                    break;
                case 2:
                    getRelevantLayoutType(0,2);
                    break;
                case 3:
                    getRelevantLayoutType(0,3);
                    break;
                case 4:
                    getRelevantLayoutType(0,4);
                    break;
                case 5:
                    getRelevantLayoutType(0,5);
                    break;
            }
        } else {
            switch (itemId) {
                case 0:
                    getRelevantLayoutType(1,0);
                    break;
                case 1:
                    getRelevantLayoutType(1,1);
                    break;
            }
        }
    }

    private void getRelevantLayoutType(int sectionNumber, int position) {
        String layoutType;
        Fragment fragment = null;

        if (sectionNumber == 0) {
            layoutType = menuSectionOneArray.get(position);
        } else {
            layoutType = menuSectionTwoArray.get(position);
        }

        switch (layoutType) {
            case "LogIn":
                Intent intentLogIn = new Intent(MainActivity.this, LogInActivity.class);
                MainActivity.this.startActivity(intentLogIn);
                break;
            case "Home":
                fragment = new HomeFragment();
                break;
            case "Products":
                fragment = new ProductsFragment();
                break;
            case "Coupons":
                fragment = new CouponsFragment();
                break;
            case "Map":
                fragment = new GoogleMapFragment();
                break;
            case "Internet":
                Intent intentWebView = new Intent(MainActivity.this, WebsiteActivity.class);
                MainActivity.this.startActivity(intentWebView);
                break;
            case "Terms":
                Intent intentTermsConditionsView = new Intent(MainActivity.this, TermsConditionsActivity.class);
                MainActivity.this.startActivity(intentTermsConditionsView);
                break;
            case "Contact":
                Intent intentContactView = new Intent(MainActivity.this, ContactActivity.class);
                MainActivity.this.startActivity(intentContactView);
                break;
        }

        // Replacing the fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.switch_view_layout, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Entering / exiting animations for activities
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        // Update the UI based on the item selected
        displaySelectedScreen(menuItem.getGroupId(), menuItem.getItemId());

        // Uncheck all checked menu items
        int size = mNavigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
        // Set item as selected to persist highlight
        menuItem.setChecked(true).setCheckable(true);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GoogleMapFragment.MY_PERMISSIONS_REQUEST_LOCATION) {
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
}