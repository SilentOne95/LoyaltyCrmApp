package com.sellger.konta.sketch_loyaltyapp.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackerService extends Service {

    public TrackerService() { }

    private static final String TAG = TrackerService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildNotification();
        } else {
            startForeground(1, new Notification());
        }

        buildNotification();
        loginToFirebase();
    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));

        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createChannel();
        } else {
            channelId = "";
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentText("GPS localization is enabled")
                        .setSmallIcon(R.drawable.logo_icon)
                        .setOngoing(true);

        startForeground(1, notificationBuilder.build());
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel channel = new NotificationChannel("snap map channel", name, importance);
        channel.enableLights(true);
        channel.setLightColor(Color.BLUE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        } else {
            stopSelf();
        }

        return "snap map channel";
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "receiver stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            requestLocationUpdates();
            Log.d(TAG, "logged to Firebase");
        }
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10 * 60 * 1000);
        request.setMaxWaitTime(60 * 60 * 1000);
        request.setFastestInterval(2 * 60 * 1000);
        request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            String userId = FirebaseAuth.getInstance().getUid();

            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    String userKey = FirebaseDatabase.getInstance().getReference(userId + "location").push().getKey();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                            .child(userId)
                            .child("location");

                    GeoFire geoFire = new GeoFire(reference);
                    Location location = locationResult.getLastLocation();
                    geoFire.setLocation(userKey, new GeoLocation(location.getLatitude(), location.getLongitude()),
                            (key, error) -> {
                        if (error != null) {
                            Log.d(TAG, "error occured while saving location data");
                        } else {
                            Log.d(TAG, "location saved successfully ");
                        }
                    });
                }
            }, null);
        }
    }
}
