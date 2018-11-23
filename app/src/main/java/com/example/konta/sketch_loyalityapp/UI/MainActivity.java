package com.example.konta.sketch_loyalityapp.UI;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.Adapters.ExpandableListAdapter;
import com.example.konta.sketch_loyalityapp.ModelClasses.MenuModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GoogleMapFragment mapFragment;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> menuList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> submenuList = new HashMap<>();
    private String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandable_list_view);

        // Reading JSON file from assets
        readFromAssets();

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        prepareMenuData();
        populateExpandableList();

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Display chosen screen as a default one after app is launched
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.switch_view_layout, new HomeFragment());
        ft.commit();
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
            JSONArray menuArray = object.getJSONArray("dropdownMenu");

            boolean hasChild;
            MenuModel menuModel;
            MenuModel childModel;

            // Iterate through every menu items and its children and add them to the separate arrays
            for (int i = 0; i < menuArray.length(); i++) {
                JSONObject insideMenuObj = menuArray.getJSONObject(i);
                String menuTitle = insideMenuObj.getString("menuTitle");
                String menuIcon = insideMenuObj.getString("menuIcon");

                JSONArray submenuArray = insideMenuObj.getJSONArray("submenuInfo");

                // Check if menu item has submenu items
                hasChild = submenuArray != null;

                // Get icon of menu item
                Resources resources = this.getResources();
                final int menuResourceId = resources.getIdentifier(menuIcon, "drawable", this.getPackageName());

                // Add new menu item to an array
                menuModel = new MenuModel(menuTitle, true, hasChild, menuResourceId);
                menuList.add(menuModel);
                submenuList.put(menuModel, null);

                // Extract all submenu items from JSON and add them to an array
                if (submenuArray != null) {
                    List<MenuModel> childModelsList = new ArrayList<>();

                    for (int j = 0; j < submenuArray.length(); j++) {
                        JSONObject insideSubmenuObj = submenuArray.getJSONObject(j);
                        String submenuTitle = insideSubmenuObj.getString("submenuTitle");

                        childModel = new MenuModel(submenuTitle, false, false);
                        childModelsList.add(childModel);

                        submenuList.put(menuModel, childModelsList);
                    }
                } else {
                    submenuList.put(menuModel, null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, menuList, submenuList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (menuList.get(groupPosition).isGroup) {
                    if (!menuList.get(groupPosition).hasChildren) {
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (submenuList.get(menuList.get(groupPosition)) != null) {
                    onBackPressed();
                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == GoogleMapFragment.MY_PERMISSIONS_REQUEST_LOCATION) {
            mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}