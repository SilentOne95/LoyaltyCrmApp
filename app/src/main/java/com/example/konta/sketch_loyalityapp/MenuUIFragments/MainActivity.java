package com.example.konta.sketch_loyalityapp.MenuUIFragments;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.konta.sketch_loyalityapp.Adapters.BottomSheetViewPagerAdapter;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.ContactActivity;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.TermsConditionsActivity;
import com.example.konta.sketch_loyalityapp.MenuUIActivities.WebsiteActivity;
import com.example.konta.sketch_loyalityapp.LoginUI.LogInActivity;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String json;
    GoogleMapFragment googleMapFragment;
    View mBottomSheet;
    BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Reading JSON file from assets
        readFromAssets();

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        prepareMenuData();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Display chosen screen as a default one after app is launched
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.switch_view_layout, new HomeFragment());
        ft.commit();
        navigationView.getMenu().getItem(1).setChecked(true);

        // Bottom Sheet set up
        mBottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(100);
        mBottomSheetBehavior.setHideable(true);

        ViewPager viewPager = findViewById(R.id.view_pager);
        BottomSheetViewPagerAdapter customAdapter = new BottomSheetViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(customAdapter);
    }

    private void readFromAssets() {
        try {
            InputStream inputStream = this.getAssets().open("menu.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareMenuData() {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrayOne = object.getJSONArray("sectionOne");

            for (int i = 0; i < arrayOne.length(); i++) {
                JSONObject insideObj = arrayOne.getJSONObject(i);
                String title = insideObj.getString("categoryTitle");
                String icon = insideObj.getString("categoryIcon");

                Resources resources = this.getResources();
                final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                Menu menuOne = navigationView.getMenu();
                menuOne.add(0, i, 0, title).setIcon(resourceId);
            }

            JSONArray arrayTwo = object.getJSONArray("sectionTwo");
            for (int i = 0; i < arrayTwo.length(); i++) {
                JSONObject insideObj = arrayTwo.getJSONObject(i);
                String title = insideObj.getString("categoryTitle");
                String icon = insideObj.getString("categoryIcon");

                Resources resources = this.getResources();
                final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                Menu menuTwo = navigationView.getMenu();
                menuTwo.add(1, i, 0, title).setIcon(resourceId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedScreen(int groupId, int itemId) {
        // Creating fragment object
        Fragment fragment = null;

        // Initializing the fragment object which is selected
        if (groupId == 0){
            switch (itemId) {
                case 0:
                    Intent intentLogIn = new Intent(MainActivity.this, LogInActivity.class);
                    MainActivity.this.startActivity(intentLogIn);
                    break;
                case 1:
                    fragment = new HomeFragment();
                    break;
                case 2:
                    fragment = new ProductsFragment();
                    break;
                case 3:
                    fragment = new CouponsFragment();
                    break;
                case 4:
                    fragment = new GoogleMapFragment();
                    break;
                case 5:
                    Intent intentWebView = new Intent(MainActivity.this, WebsiteActivity.class);
                    MainActivity.this.startActivity(intentWebView);
                    break;
            }
        } else {
            switch (itemId) {
                case 0:
                    Intent intentTermsConditionsView = new Intent(MainActivity.this, TermsConditionsActivity.class);
                    MainActivity.this.startActivity(intentTermsConditionsView);
                    break;
                case 1:
                    Intent intentContactView = new Intent(MainActivity.this, ContactActivity.class);
                    MainActivity.this.startActivity(intentContactView);
                    break;
            }
        }

        // Replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.switch_view_layout, fragment);
            ft.commit();
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
        drawerLayout.closeDrawers();

        // Update the UI based on the item selected
        displaySelectedScreen(menuItem.getGroupId(), menuItem.getItemId());

        // Uncheck all checked menu items
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        // Set item as selected to persist highlight
        menuItem.setChecked(true);

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GoogleMapFragment.MY_PERMISSIONS_REQUEST_LOCATION) {
            googleMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}