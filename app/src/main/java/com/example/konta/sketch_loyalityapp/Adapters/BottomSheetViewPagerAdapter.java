package com.example.konta.sketch_loyalityapp.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.konta.sketch_loyalityapp.AdditionalUI.FirstFragment;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.AdditionalUI.SecondFragment;

public class BottomSheetViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private final int PAGE_COUNT = 2;
    private int[] tabTitles = {R.string.first_fragment, R.string.second_fragment};
    private int[] imageResId = {R.drawable.ic_clock, R.drawable.ic_info};

    public BottomSheetViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FirstFragment();
        } else {
            return new SecondFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = mContext.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   "
                + mContext.getResources().getString(tabTitles[position]));
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
