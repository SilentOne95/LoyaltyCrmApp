package com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.openingHours;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.databinding.FragmentOpeningHoursBinding;
import com.sellger.konta.sketch_loyaltyapp.ui.map.bottomSheet.BottomSheetContract;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class OpeningHoursFragment extends BaseFragment implements BottomSheetContract.OpeningHoursView {

    private static final String TAG = OpeningHoursFragment.class.getSimpleName();

    private OpeningHoursPresenter presenter;
    private FragmentOpeningHoursBinding mBinding;

    private TextView mMondayHours, mTuesdayHours, mWednesdayHours, mThursdayHours, mFridayHours,
            mSaturdayHours, mSundayHours;

    public OpeningHoursFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_opening_hours;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_opening_hours, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init views
        initViews();

        // Setting up presenter
        presenter = new OpeningHoursPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.setUpObservable();
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mMondayHours = mBinding.getRoot().findViewById(R.id.monday_hours_text);
        mTuesdayHours = mBinding.getRoot().findViewById(R.id.tuesday_hours_text);
        mWednesdayHours = mBinding.getRoot().findViewById(R.id.wednesday_hours_text);
        mThursdayHours = mBinding.getRoot().findViewById(R.id.thursday_hours_text);
        mFridayHours = mBinding.getRoot().findViewById(R.id.friday_hours_text);
        mSaturdayHours = mBinding.getRoot().findViewById(R.id.saturday_hours_text);
        mSundayHours = mBinding.getRoot().findViewById(R.id.sunday_hours_text);
    }

    /**
     * Called from {@link OpeningHoursPresenter#getSelectedMarker(int)} to populate view.
     *
     * @param singleDayOpenHours list of strings retrieved from {@link Marker} object
     */
    @Override
    public void setUpViewsWithData(String[] singleDayOpenHours) {
        mMondayHours.setText(singleDayOpenHours[0]);
        mTuesdayHours.setText(singleDayOpenHours[1]);
        mWednesdayHours.setText(singleDayOpenHours[2]);
        mThursdayHours.setText(singleDayOpenHours[3]);
        mFridayHours.setText(singleDayOpenHours[4]);
        mSaturdayHours.setText(singleDayOpenHours[5]);
        mSundayHours.setText(singleDayOpenHours[6]);
    }

    /**
     * Called from {@link OpeningHoursPresenter#getSelectedMarker(int)} whenever data is
     * unavailable to get.
     *
     * @param message string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}