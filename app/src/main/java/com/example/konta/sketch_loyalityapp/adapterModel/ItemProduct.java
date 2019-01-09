package com.example.konta.sketch_loyalityapp.adapterModel;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

public class ItemProduct {

    private String mItemTitle, mItemDescription;
    private RoundedBitmapDrawable mItemBitmapDrawable;
    private double mItemPrice;

    /**
     * Create a new GridViewItem or Navigation Drawer object.
     *
     * @param itemTitle is the name of the product or category
     * @param itemBitmapDrawable is the bitmap with rounded corners
     */
    public ItemProduct(String itemTitle, RoundedBitmapDrawable itemBitmapDrawable,
                       double itemPrice, String itemDescription) {
        mItemTitle = itemTitle;
        mItemBitmapDrawable = itemBitmapDrawable;
        mItemPrice = itemPrice;
        mItemDescription = itemDescription;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the image resource Id. */
    public RoundedBitmapDrawable getItemBitmapDrawable() { return mItemBitmapDrawable; }

    /** Get the price of the item. */
    public double getItemPrice() { return mItemPrice; }

    /** Get the price of the item. */
    public String getItemDescription() { return mItemDescription; }
}