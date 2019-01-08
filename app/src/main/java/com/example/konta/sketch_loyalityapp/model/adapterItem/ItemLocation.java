package com.example.konta.sketch_loyalityapp.model.adapterItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemLocation implements ClusterItem {

    private LatLng mPosition;
    private String mId;

    public ItemLocation(double itemLat, double itemLng, String id) {
        mPosition = new LatLng(itemLat, itemLng);
        mId = id;
    }

    public ItemLocation(LatLng position, String id) {
        mPosition = position;
        mId = id;
    }

    /** Getters associated with ClusterItem */
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mId;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}