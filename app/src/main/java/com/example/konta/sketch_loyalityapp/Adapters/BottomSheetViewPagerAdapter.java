package com.example.konta.sketch_loyalityapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.konta.sketch_loyalityapp.AdditionalUI.FirstFragment;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.AdditionalUI.SecondFragment;

public class BottomSheetViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

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
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_first);
            case 1:
                return mContext.getString(R.string.category_second);
            default:
                return null;
        }
    }
}
