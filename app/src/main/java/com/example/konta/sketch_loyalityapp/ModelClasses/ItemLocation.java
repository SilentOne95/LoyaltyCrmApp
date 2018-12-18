package com.example.konta.sketch_loyalityapp.ModelClasses;

public class ItemLocation {

    private String mItemTitle;
    private double mItemLat;
    private double mItemLng;

    public ItemLocation(String itemTitle, double itemLat, double itemLng) {
        mItemTitle = itemTitle;
        mItemLat = itemLat;
        mItemLng = itemLng;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the latitude the item. */
    public double getItemLat() { return mItemLat; }

    /** Get the longitude of the item. */
    public double getItemLng() { return mItemLng; }
}