package com.example.konta.sketch_loyalityapp.UI;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View view, float v) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View view) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View view) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int i) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        final NavigationView mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Update the UI based on the item selected
                        displaySelectedScreen(menuItem.getItemId());

                        // Uncheck all checked menu items
                        int size = mNavigationView.getMenu().size();
                        for (int i = 0; i < size; i++) {
                            mNavigationView.getMenu().getItem(i).setChecked(false);
                        }
                        // Set item as selected to persist highlight
                        menuItem.setChecked(true);

                        return true;
                    }
                }
        );

        // Reading JSON file from assets
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

        // Extracting objects that has been built up from parsing the given JSON file
        try {
            JSONObject object = new JSONObject(json);
            JSONArray arrayOne = object.getJSONArray("sectionOne");

            for (int i = 0; i < arrayOne.length(); i++) {
                JSONObject insideObj = arrayOne.getJSONObject(i);
                String title = insideObj.getString("categoryTitle");
                String icon = insideObj.getString("categoryIcon");

                Resources resources = this.getResources();
                final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                Menu menuOne = mNavigationView.getMenu();
                menuOne.add(0, i, 0, title).setIcon(resourceId);
            }

            JSONArray arrayTwo = object.getJSONArray("sectionTwo");
            for (int i = 0; i < arrayTwo.length(); i++) {
                JSONObject insideObj = arrayTwo.getJSONObject(i);
                String title = insideObj.getString("categoryTitle");
                String icon = insideObj.getString("categoryIcon");

                Resources resources = this.getResources();
                final int resourceId = resources.getIdentifier(icon, "drawable", this.getPackageName());
                Menu menuTwo = mNavigationView.getMenu();
                menuTwo.add(1, i, 0, title).setIcon(resourceId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Display chosen screen as a default one after app is launched
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.switch_view_layout, new HomeFragment());
        ft.commit();
        mNavigationView.getMenu().getItem(0).setChecked(true);
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

    private void displaySelectedScreen(int itemId) {
        // Creating fragment object
        Fragment fragment = null;

        // Initializing the fragment object which is selected
        switch (itemId) {
            case 0:
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
        }

        // Replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.switch_view_layout, fragment);
            ft.commit();
        }
    }
}
