package com.sellger.konta.sketch_loyaltyapp.service.location;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DATABASE_PATH_LAT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DATABASE_PATH_LNG;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DATABASE_PATH_LOCATION;

class LocationResultHelper {

    private static final String TAG = LocationResultHelper.class.getSimpleName();

    private List<Location> mLocations;
    private String userId;

    LocationResultHelper( List<Location> locations) {
        mLocations = locations;
        userId = FirebaseAuth.getInstance().getUid();
    }

    void sendDataToServer() {
        String userKey = FirebaseDatabase.getInstance().getReference(userId + DATABASE_PATH_LOCATION).push().getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(userId)
                .child(DATABASE_PATH_LOCATION)
                .child(userKey);

        for (int i = 0; i < mLocations.size(); i++) {
            reference
                    .child(DATABASE_PATH_LAT)
                    .setValue(mLocations.get(i).getLatitude());

            reference
                    .child(DATABASE_PATH_LNG)
                    .setValue(mLocations.get(i).getLongitude());
        }
    }
}
