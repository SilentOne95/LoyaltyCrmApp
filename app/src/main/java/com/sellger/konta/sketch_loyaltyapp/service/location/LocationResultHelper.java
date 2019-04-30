package com.sellger.konta.sketch_loyaltyapp.service.location;

import android.location.Location;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class LocationResultHelper {

    private static final String TAG = LocationResultHelper.class.getSimpleName();

    private List<Location> mLocations;
    private String userId;

    LocationResultHelper( List<Location> locations) {
        mLocations = locations;
        userId = FirebaseAuth.getInstance().getUid();
    }

    void sendDataToServer() {
        String userKey = FirebaseDatabase.getInstance().getReference(userId + "location").push().getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(userId)
                .child("location")
                .child(userKey);

        for (int i = 0; i < mLocations.size(); i++) {
            reference
                    .child("lat")
                    .setValue(mLocations.get(i).getLatitude());

            reference
                    .child("lng")
                    .setValue(mLocations.get(i).getLongitude());
        }
    }
}
