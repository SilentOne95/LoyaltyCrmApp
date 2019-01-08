package com.example.konta.sketch_loyalityapp.ui.mapFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.adapters.BottomSheetViewPagerAdapter;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.utils.CustomClusterRenderer;
import com.example.konta.sketch_loyalityapp.model.adapterItem.ItemLocation;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.konta.sketch_loyalityapp.Constants.BOTTOM_SHEET_PEEK_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback,
        View.OnClickListener, MapContract.View {

    MapContract.Presenter mPresenter;

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    private String json = null;
    private String layoutTitle;
    private ClusterManager<ItemLocation> mClusterManager;
    private SparseArray<ItemLocation> mListOfMarkers = new SparseArray<>();
    private BottomSheetBehavior mBottomSheetBehavior;

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "locations.json";

    @Override
    protected int getLayout() { return R.layout.fragment_google_map; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new MapPresenter(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        // Reading JSON file from assets
        json = ((MyApplication) getActivity().getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file
        // and adding markers (items) to cluster
        extractDataFromJson();

        getActivity().setTitle(layoutTitle);

        // Set up BottomSheet
        View mBottomSheet = rootView.findViewById(R.id.bottom_sheet);
        mBottomSheet.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setPeekHeight(BOTTOM_SHEET_PEEK_HEIGHT);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // Custom TabLayout with ViewPager set up
        ViewPager viewPager = rootView.findViewById(R.id.view_pager);
        BottomSheetViewPagerAdapter pagerAdapter = new BottomSheetViewPagerAdapter(getContext(), getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Setting up custom TabLayout view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20 * 1000);
        mLocationRequest.setFastestInterval(10 * 1000);

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                // Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }

        // Add default geo location to set camera on certain country
        LatLng poland = new LatLng(51.253679, 19.069815);

        // Move and zoom camera to certain position
        CameraPosition cameraPosition = new CameraPosition.Builder().target(poland).zoom(5.8f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mGoogleMap.animateCamera(cameraUpdate);

        // Add markers to map and set up ClusterManager
        setUpCluster();

        // Set custom cluster style
        final CustomClusterRenderer renderer = new CustomClusterRenderer(getContext(), mGoogleMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        // Handle events related to BottomSheet
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ItemLocation>() {
            @Override
            public boolean onClusterItemClick(ItemLocation itemLocation) {
                mPresenter.switchBottomSheetState(itemLocation);
                return true;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPresenter.switchBottomSheetState(latLng);
            }
        });
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                // The last location in the list is the newest
                mLastLocation =  locationList.get(locationList.size() - 1);
            }
            super.onLocationResult(locationResult);
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        // Stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission granted. Do the needed location-related task
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, display Toast message
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void extractDataFromJson() {
        try {
            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            JSONArray array = object.getJSONArray("shops");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);
                int shopId = insideObj.getInt("shopId");
                String shopTitle = insideObj.getString("shopTitle");

                JSONArray shopCoordinates = insideObj.getJSONArray("shopCoordinates");
                double shopLatitude = shopCoordinates.getDouble(0);
                double shopLongitude = shopCoordinates.getDouble(1);

                mListOfMarkers.append(i,
                        new ItemLocation(shopLatitude, shopLongitude, Integer.toString(shopId)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpCluster() {
        // Initialize the manager with the context and the map
        mClusterManager = new ClusterManager<>(getContext(), mGoogleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

        // Setting up markers using json data
        addMarkersToCluster();
    }

    private void addMarkersToCluster() {
        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < mListOfMarkers.size(); i++) {
            ItemLocation marker = new ItemLocation(mListOfMarkers.get(i).getPosition(), mListOfMarkers.get(i).getTitle());
            mClusterManager.addItem(marker);
        }
    }

    @Override
    public void onClick(View view) { mPresenter.switchBottomSheetState(view); }

    @Override
    public int getBottomSheetState() {
        return mBottomSheetBehavior.getState();
    }

    @Override
    public void setBottomSheetState(int state) {
        mBottomSheetBehavior.setState(state);
    }
}