package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.adapter.BottomSheetViewPagerAdapter;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.pojo.map.Marker;
import com.sellger.konta.sketch_loyaltyapp.service.TrackerService;
import com.sellger.konta.sketch_loyaltyapp.utils.CustomClusterRenderer;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REQUEST_CHECK_SETTINGS;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback,
        View.OnClickListener, MapContract.View, GoogleMap.OnMyLocationButtonClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback, OnCompleteListener<LocationSettingsResponse>, SearchView.OnQueryTextListener {

    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    MapPresenter presenter;

    GoogleMap mGoogleMap;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    protected LocationRequest mLocationRequest;
    protected GoogleApiClient mGoogleApiClient;

    private ClusterManager<Marker> mClusterManager;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mPreviousSelectedMarkerId;

    // BottomSheet PeekHeight Panel views
    private View mPanelPeekHeight;
    private TextView mPanelPlaceTitle, mPanelAddress, mPanelTodayOpenHours;

    @Override
    protected int getLayout() { return R.layout.fragment_google_map; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Mapa");

        setHasOptionsMenu(true);

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
        mPanelPeekHeight = rootView.findViewById(R.id.bottom_sheet_peek);
        View mBottomSheet = rootView.findViewById(R.id.map_bottom_sheet);
        mBottomSheet.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        optionsItem.setVisible(false);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Setting up BottomSheet height as layout height. It has to be implemented here to measure
        // view after it's created to get different value than 0
        mBottomSheetBehavior.setPeekHeight(mPanelPeekHeight.getHeight());

        setUpGoogleApiClient();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(10 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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

        // Initialize the manager with the context and the map
        mClusterManager = new ClusterManager<>(getContext(), mGoogleMap);

        // Add markers to map and set up ClusterManager
        presenter.requestDataFromServer();

        // Point the map's listeners at the listeners implemented by the cluster manager
        mGoogleMap.setOnCameraIdleListener(mClusterManager);

        // Set BottomSheet state when map is clicked
        mGoogleMap.setOnMapClickListener(latLng -> {
                    presenter.switchBottomSheetState(latLng);
                    // Set markerId to 0 to enable BottomSheet appear
                    mPreviousSelectedMarkerId = 0;
                }
        );

        mGoogleMap.setOnMyLocationButtonClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(getContext()).checkLocationSettings(builder.build());
        task.addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
            LocationSettingsResponse response = task.getResult(ApiException.class);
        } catch (ApiException exception) {
            switch (exception.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        // Cast to a resolvable exception.
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        resolvable.startResolutionForResult(getActivity(), 101);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    } catch (ClassCastException e) {
                        // Ignore, should be an impossible error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    break;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getContext(), "onConnectionSuspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), "onConnectionFailed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // No need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, show the user a dialog
                try {
                    // Show the dialog and check the result
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Failed to show dialog
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable, so not possible to show any dialog now
                break;
        }
    }

    // TODO: Check toasts
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 101:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Toast.makeText(getActivity(),states.isLocationPresent() + "", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getActivity(),"Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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
                    Toast.makeText(getActivity(),
                            getResources().getText(R.string.localization_permission_denied), Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    @Override
    public void setUpCluster(final List<Marker> markerList) {
        // Add markers to cluster
        for (Marker marker : markerList) {
            mClusterManager.addItem(marker);
        }

        // Set custom cluster style
        final CustomClusterRenderer renderer = new CustomClusterRenderer(getContext(), mGoogleMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        // Set up cluster an marker listeners
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(cluster -> {

            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (ClusterItem item : cluster.getItems()) {
                builder.include(item.getPosition());
            }
            final LatLngBounds bounds = builder.build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

            return true;
        });
        mClusterManager.setOnClusterItemClickListener(marker -> {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));

            // Preventing from selecting the same marker
            if (mPreviousSelectedMarkerId != marker.getId()) {
                presenter.passDataToBottomSheet(marker.getId());
                presenter.switchBottomSheetState(marker);
                mPreviousSelectedMarkerId = marker.getId();
            }

            return true;
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // If GPS is off and user click on button to localize position on map,
        // dialog window will pop up to turn on GPS
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setUpGoogleApiClient();
        }

        return false;
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
        return " ";
//        return getString(getResources()
//                .getIdentifier("bottom_sheet_default_place_title", "string", PACKAGE_NAME));
    }

    @Override
    public String getDefaultPlaceData() {
        return " ";
//        return getString(getResources()
//                .getIdentifier("bottom_sheet_default_no_info_text", "string", PACKAGE_NAME));
    }

    @Override
    public void setUpBottomSheetPanelWithData(String title, String address, String openHours) {
        if (openHours.contains("Today open:")) {
            openHours = openHours.replace("Today open:", getResources().getString(R.string.bottom_sheet_today_open_text));
        } else if (openHours.contains("All day")) {
            openHours = getResources().getString(R.string.bottom_sheet_today_open_all_day_text);
        } else {
            openHours = getResources().getString(R.string.bottom_sheet_today_closed_text);
        }

        mPanelPlaceTitle.setText(title);
        mPanelAddress.setText(address);
        mPanelTodayOpenHours.setText(openHours);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}