package com.example.konta.sketch_loyalityapp.ModelClasses;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ItemLocation implements ClusterItem {

    private String mItemTitle;
    private double mItemLat;
    private double mItemLng;
    private LatLng mPosition;

    public ItemLocation(double itemLat, double itemLng) {
        mPosition = new LatLng(itemLat, itemLng);
    }

    public ItemLocation(String itemTitle, double itemLat, double itemLng) {
        mItemTitle = itemTitle;
        mItemLat = itemLat;
        mItemLng = itemLng;
        mPosition = new LatLng(itemLat, itemLng);
    }

    /** Getters associated with ClusterItem */
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mItemTitle;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    /** Get the latitude the item. */
    public double getItemLat() { return mItemLat; }

    /** Get the longitude of the item. */
    public double getItemLng() { return mItemLng; }
}