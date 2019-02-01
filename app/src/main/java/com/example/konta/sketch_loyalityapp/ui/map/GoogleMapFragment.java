package com.example.konta.sketch_loyalityapp.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.adapter.BottomSheetViewPagerAdapter;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.service.TrackerService;
import com.example.konta.sketch_loyalityapp.utils.CustomClusterRenderer;
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

import java.util.List;

import static com.example.konta.sketch_loyalityapp.Constants.BOTTOM_SHEET_PEEK_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.example.konta.sketch_loyalityapp.ui.main.MainActivity.PACKAGE_NAME;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback,
        View.OnClickListener, MapContract.View, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    MapPresenter presenter;

    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;

    private ClusterManager<Marker> mClusterManager;
    private BottomSheetBehavior mBottomSheetBehavior;

    // BottomSheet PeekHeight Panel views
    private TextView mPanelPlaceTitle, mPanelAddress, mPanelTodayOpenHours;

    @Override
    protected int getLayout() { return R.layout.fragment_google_map; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Map");

        presenter = new MapPresenter(this, new MapModel());
        presenter.setUpObservable();

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
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        // BottomSheet PeekHeight Panel
        mPanelPlaceTitle = rootView.findViewById(R.id.bottom_sheet_icon_title);
        mPanelAddress = rootView.findViewById(R.id.bottom_sheet_place_address);
        mPanelTodayOpenHours = rootView.findViewById(R.id.bottom_sheet_place_hours);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20 * 1000);
        mLocationRequest.setFastestInterval(10 * 1000);

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
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
        presenter.requestDataFromServer();

        // Point the map's listeners at the listeners implemented by the cluster manager
        mGoogleMap.setOnCameraIdleListener(mClusterManager);

        // Set BottomSheet state when map is clicked
        mGoogleMap.setOnMapClickListener(latLng -> presenter.switchBottomSheetState(latLng));

        mGoogleMap.setOnMyLocationButtonClickListener(this);
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
        if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            startTrackService();
        }

    }

    private void startTrackService() {
        if (getActivity() != null) {
            getActivity().startService(new Intent(getContext(), TrackerService.class));
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
                    if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);

                        startTrackService();
                    }
                } else {
                    // Permission denied, display Toast message
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void setUpCluster(final List<Marker> markerList) {
        // Initialize the manager with the context and the map
        mClusterManager = new ClusterManager<>(getContext(), mGoogleMap);

        // Add markers to cluster
        for (Marker marker : markerList) {
            mClusterManager.addItem(marker);
        }

        // Set custom cluster style
        final CustomClusterRenderer renderer = new CustomClusterRenderer(getContext(), mGoogleMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        // Set BottomSheet state when marker is clicked
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(marker -> {
            presenter.passDataToBottomSheet(marker.getId());
            presenter.switchBottomSheetState(marker);
            return true;
        });
    }

    @Override
    public void onClick(View view) { presenter.switchBottomSheetState(view); }

    @Override
    public int getBottomSheetState() {
        return mBottomSheetBehavior.getState();
    }

    @Override
    public void setBottomSheetState(int state) {
        mBottomSheetBehavior.setState(state);
    }

    @Override
    public String getDefaultPlaceTitle() {
        return getString(getResources()
                .getIdentifier("bottom_sheet_default_place_title", "string", PACKAGE_NAME));
    }

    @Override
    public String getDefaultPlaceData() {
        return getString(getResources()
                .getIdentifier("bottom_sheet_default_no_info_text", "string", PACKAGE_NAME));
    }

    @Override
    public void setUpBottomSheetPanelWithData(String title, String address, String openHours) {
        mPanelPlaceTitle.setText(title);
        mPanelAddress.setText(address);
        mPanelTodayOpenHours.setText(openHours);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    this.getActivity());

            builder.setMessage("GPS is disabled")
                    .setCancelable(false)
                    .setTitle("Please enable GPS localization, to show your position on map.")
                    .setPositiveButton("Enable",
                            (dialog, id) -> startActivity(new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            final AlertDialog alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            alert.show();
        }
        return false;
    }
}