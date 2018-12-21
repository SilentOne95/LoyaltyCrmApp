package com.example.konta.sketch_loyalityapp.modelClasses;

import android.support.v4.graphics.drawable.RoundedBitmapDrawable;

public class ItemCoupon {

    private String mItemTitle, mItemValidDate, mItemDescription;
    private RoundedBitmapDrawable mItemBitmapDrawable;
    private int mItemDiscount;
    private double mItemBasicPrice, mItemFinalPrice;

    /**
     * Create a new GridViewItem or Navigation Drawer object.
     *
     * @param itemTitle      is the name of the product or category
     * @param bitmapDrawable is the bitmap with rounded corners
     */
    public ItemCoupon(String itemTitle, RoundedBitmapDrawable bitmapDrawable, int itemDiscount,
                      double itemBasicPrice, double itemFinalPrice, String itemValidDate,
                      String itemDescription) {
        mItemTitle = itemTitle;
        mItemBitmapDrawable = bitmapDrawable;
        mItemDiscount = itemDiscount;
        mItemBasicPrice = itemBasicPrice;
        mItemFinalPrice = itemFinalPrice;
        mItemValidDate = itemValidDate;
        mItemDescription = itemDescription;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the image resource Id. */
    public RoundedBitmapDrawable getItemBitmapDrawable() { return mItemBitmapDrawable; }

    /** Get the discount of the item. */
    public int getItemDiscount() { return  mItemDiscount; }

    /** Get the basic price of the item. */
    public double getItemBasicPrice() { return mItemBasicPrice; }

    /** Get the final price of the item. */
    public double getItemFinalPrice() { return mItemFinalPrice; }

    /** Get the valid date of the item. */
    public String getItemValidDate() { return mItemValidDate; }

    /** Get the description of the item. */
    public String getItemDescription() { return mItemDescription; }
}