package com.sellger.konta.sketch_loyaltyapp.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SearchView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.sellger.konta.sketch_loyaltyapp.adapter.BottomSheetViewPagerAdapter;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.service.geofencing.GeofenceTransitionsIntentService;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsMap.CustomClusterRenderer;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.sellger.konta.sketch_loyaltyapp.Constants.ALL_DAY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.FASTEST_UPDATE_INTERVAL;
import static com.sellger.konta.sketch_loyaltyapp.Constants.GEOFENCE_DEFAULT_RADIUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MAX_WAIT_TIME;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.REQUEST_CHECK_SETTINGS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_CONNECTION_FAILED;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_CONNECTION_SUSPENDED;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_LOCATION_PERMISSION_DENIED;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TODAY_OPEN_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.UPDATE_INTERVAL;

public class GoogleMapFragment extends BaseFragment implements OnMapReadyCallback, MapContract.View,
        GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback,
        OnCompleteListener<LocationSettingsResponse>, SearchView.OnQueryTextListener, View.OnClickListener {

    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    private MapPresenter presenter;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    private ClusterManager<Marker> mClusterManager;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int mPreviousSelectedMarkerId;

    // BottomSheet PeekHeight Panel views
    private View mPanelPeekHeight, mBottomSheet;
    private TextView mPanelPlaceTitle, mPanelAddress, mPanelTodayOpenHours;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private FloatingActionButton mFab;
    private String mLastSelectedMarkerTitle;

    // Geofences
    private GeofencingClient mGeofencingClient;
    private List<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent = null;

    @Override
    protected int getLayout() {
        return R.layout.fragment_google_map;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Extract additional data, which is fragment's title
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
        } else {
            getActivity().setTitle("Mapa");
        }

        setHasOptionsMenu(true);

        // Setting up presenter
        presenter = new MapPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.setUpObservable();

        // Setting up map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        // Init views
        initViews();

        // Setting up views
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mFab.setVisibility(View.GONE);
        mFab.setOnClickListener(this);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
                mFab.setVisibility(View.VISIBLE);
                if (slideOffset < 0.0) {
                    mFab.animate().scaleX(1 + slideOffset).scaleY(1 + slideOffset).setDuration(0).start();
                }
            }
        });

        // Custom TabLayout with ViewPager set up
        BottomSheetViewPagerAdapter pagerAdapter = new BottomSheetViewPagerAdapter(getContext(), getChildFragmentManager());
        mViewPager.setAdapter(pagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        // Setting up custom TabLayout view
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
    }

    private void startGeofence(List<Marker> markerList) {
        mGeofencingClient = LocationServices.getGeofencingClient(getContext());

        for (Marker marker : markerList) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(marker.getTitle())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setCircularRegion(marker.getLat(), marker.getLng(), GEOFENCE_DEFAULT_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }

        Intent intent = new Intent(getContext(), GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGeofencePendingIntent != null) {
            mGeofencingClient.removeGeofences(getGeofencePendingIntent());
        }

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void initViews() {
        mPanelPeekHeight = rootView.findViewById(R.id.bottom_sheet_peek);
        mBottomSheet = rootView.findViewById(R.id.map_bottom_sheet);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mTabLayout = rootView.findViewById(R.id.tabs);

        mFab = rootView.findViewById(R.id.fab);

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
        if (mGoogleApiClient != null) {
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Setting map's custom style depending on day time
        setCustomMapStyle();

        // Setting up BottomSheet height as layout height. It has to be implemented here to measure
        // view after it's created to get different value than 0
        mBottomSheetBehavior.setPeekHeight(mPanelPeekHeight.getHeight());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        setUpGoogleApiClient();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                // Request Location Permission
                checkLocationPermission();
            }
        } else {
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
    public void setCustomMapStyle() {
        int mapStyleResId;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Warsaw"));
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 7 && hourOfDay <= 22) {
            mapStyleResId = R.raw.map_style_day;
        } else {
            mapStyleResId = R.raw.map_style_night;
        }

        // TODO:
        try {
            boolean isSuccessful = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), mapStyleResId));
        } catch (Resources.NotFoundException e) {
            // Catch exception
        }
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
        displayToastMessage(TOAST_CONNECTION_SUSPENDED);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        displayToastMessage(TOAST_CONNECTION_FAILED);
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
        if (requestCode == 101) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    Toast.makeText(getActivity(), states.isLocationPresent() + "", Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void checkLocationPermission() {
        if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted. Do the needed location-related task
                if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mGoogleMap.setMyLocationEnabled(true);

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
            } else {
                // Permission denied, display Toast message
                displayToastMessage(TOAST_LOCATION_PERMISSION_DENIED);
            }
        }
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                // The last location in the list is the newest
                Location mLastLocation = locationList.get(locationList.size() - 1);
            }
            super.onLocationResult(locationResult);
        }
    };

    @Override
    public void setUpCluster(final List<Marker> markerList) {
        startGeofence(markerList);

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
            if (mPreviousSelectedMarkerId == marker.getId() && getBottomSheetState() == BottomSheetBehavior.STATE_HIDDEN) {
                presenter.switchBottomSheetState(marker);
            } else if (mPreviousSelectedMarkerId != marker.getId()) {
                presenter.passClickedMarkerId(marker.getId());
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
    public int getBottomSheetState() {
        return mBottomSheetBehavior.getState();
    }

    @Override
    public void setBottomSheetState(int state) {
        mBottomSheetBehavior.setState(state);
    }

    @Override
    public void setUpBottomSheetPanelWithData(String title, String address, String openHours) {
        if (openHours.contains(TODAY_OPEN_STRING)) {
            openHours = openHours.replace(TODAY_OPEN_STRING, getResources().getString(R.string.bottom_sheet_today_open_text));
        } else if (openHours.contains(ALL_DAY_STRING)) {
            openHours = getResources().getString(R.string.bottom_sheet_today_open_all_day_text);
        } else {
            openHours = getResources().getString(R.string.bottom_sheet_today_closed_text);
        }

        mLastSelectedMarkerTitle = title;
        mPanelPlaceTitle.setText(title);
        mPanelAddress.setText(address);
        mPanelTodayOpenHours.setText(openHours);
    }

    @Override
    public void displayToastMessage(String message) {
        switch (message) {
            case TOAST_ERROR:
                message = getString(R.string.default_toast_error_message);
                break;
            case TOAST_LOCATION_PERMISSION_DENIED:
                message = getString(R.string.localization_permission_denied);
                break;
            case TOAST_CONNECTION_SUSPENDED:
                message = getString(R.string.map_connection_suspended);
                break;
            case TOAST_CONNECTION_FAILED:
                message = getString(R.string.map_connection_failed);
                break;
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (mLastSelectedMarkerTitle != null) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=" +
                            mLastSelectedMarkerTitle.replaceAll(" ", "+")));
            startActivity(intent);
        }
    }
}