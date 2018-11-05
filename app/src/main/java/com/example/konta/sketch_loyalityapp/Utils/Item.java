package com.example.konta.sketch_loyalityapp.Utils;

public class Item {

    private String mItemTitle;
    private int mImageResourceId;

    /**
     * Create a new GridViewItem or Navigation Drawer object.
     *
     * @param itemTitle is the name of the product or category
     * @param imageResourceId is drawable reference ID that corresponds to the Android version
     */
    public Item(String itemTitle, int imageResourceId) {
        mItemTitle = itemTitle;
        mImageResourceId = imageResourceId;
    }

    /** Get the title of the item. */
    public String getItemTitle() { return mItemTitle; }

    /** Get the image resource Id. */
    public int getResourceId() { return mImageResourceId; }
}
