package com.sellger.konta.sketch_loyaltyapp.service.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;
import com.sellger.konta.sketch_loyaltyapp.service.pushNotification.SendNotificationHelper;

import java.util.List;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = LocationUpdatesBroadcastReceiver.class.getSimpleName();

    public static final String ACTION_PROCESS_UPDATES =
            "com.sellger.konta.sketch_loyaltyapp.action" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    LocationResultHelper locationResultHelper = new LocationResultHelper(locations);
                    locationResultHelper.sendDataToServer();

                    SendNotificationHelper sendNotificationHelper = new SendNotificationHelper(context, "broadcast");
                    sendNotificationHelper.send();
                }
            }
        }
    }
}
