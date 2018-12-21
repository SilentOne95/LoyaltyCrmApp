package com.example.konta.sketch_loyalityapp.modelClasses;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

public class Item {

    private String mItemTitle;
    private RoundedBitmapDrawable mBitmapDrawable;

    /**
     * Create a new GridViewItem or Navigation Drawer object.
     *
     * @param itemTitle is the name of the product or category
     * @param bitmapDrawable is the bitmap with rounded corners
     */
    public Item(String itemTitle, RoundedBitmapDrawable bitmapDrawable) {
        mItemTitle = itemTitle;
        mBitmapDrawable = bitmapDrawable;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the image resource Id. */
    public RoundedBitmapDrawable getBitmapDrawable() { return mBitmapDrawable; }
}