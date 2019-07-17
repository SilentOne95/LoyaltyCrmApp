package com.jemsushi.loyaltyapp.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jemsushi.loyaltyapp.ui.map.bottomSheet.openingHours.OpeningHoursFragment;
import com.jemsushi.loyaltyapp.ui.map.bottomSheet.contactInfo.ContactInfoFragment;
import com.jemsushi.loyaltyapp.R;

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