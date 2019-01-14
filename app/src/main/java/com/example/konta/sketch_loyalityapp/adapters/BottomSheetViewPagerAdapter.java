package com.example.konta.sketch_loyalityapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours.OpeningHoursFragment;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo.ContactInfoFragment;
import com.example.konta.sketch_loyalityapp.R;

public class BottomSheetViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private int[] tabTitles = {R.string.first_fragment, R.string.second_fragment};
    private int[] imageResId = {R.drawable.ic_clock, R.drawable.ic_info};

    public BottomSheetViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_view_custom, null);

        TextView textView = view.findViewById(R.id.tab_view_text);
        textView.setText(tabTitles[position]);
        textView.setCompoundDrawablesWithIntrinsicBounds(imageResId[position], 0, 0, 0);

        return view;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OpeningHoursFragment();
        } else {
            return new ContactInfoFragment();
        }
    }

    @Override
    public int getCount() { return 2; }
}