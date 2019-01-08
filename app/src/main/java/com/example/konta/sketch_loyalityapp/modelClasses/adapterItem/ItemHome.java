package com.example.konta.sketch_loyalityapp.modelClasses.adapterItem;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

public class ItemHome {

    private String mItemTitle, mLayoutType;
    private RoundedBitmapDrawable mBitmapDrawable;

    /**
     * Create a new GridViewItem or Navigation Drawer object.
     *
     * @param itemTitle is the name of the product or category
     * @param bitmapDrawable is the bitmap with rounded corners
     * @param layoutType decides which view should be displayed
     */
    public ItemHome(String itemTitle, RoundedBitmapDrawable bitmapDrawable, String layoutType) {
        mItemTitle = itemTitle;
        mBitmapDrawable = bitmapDrawable;
        mLayoutType = layoutType;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the image resource Id. */
    public RoundedBitmapDrawable getBitmapDrawable() { return mBitmapDrawable; }

    /** Get the layout type of the item. */
    public String getLayoutType() { return mLayoutType; }
}