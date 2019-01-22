package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.openingHours;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

public class OpeningHoursFragment extends BaseFragment implements BottomSheetContract.OpeningHoursView {

    OpeningHoursPresenter presenter;

    public OpeningHoursFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayout() { return R.layout.fragment_opening_hours; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new OpeningHoursPresenter(this);
        presenter.setUpObservable();
    }

    @Override
    public void setUpViewsWithData(SparseArray<String> singleDayOpenHours) {

        TextView mondayHours = rootView.findViewById(R.id.monday_hours_text);
        mondayHours.setText(singleDayOpenHours.get(0));

        TextView tuesdayHours = rootView.findViewById(R.id.tuesday_hours_text);
        tuesdayHours.setText(singleDayOpenHours.get(1));

        TextView wednesdayHours = rootView.findViewById(R.id.wednesday_hours_text);
        wednesdayHours.setText(singleDayOpenHours.get(2));

        TextView thursdayHours = rootView.findViewById(R.id.thursday_hours_text);
        thursdayHours.setText(singleDayOpenHours.get(3));

        TextView fridayHours = rootView.findViewById(R.id.friday_hours_text);
        fridayHours.setText(singleDayOpenHours.get(4));

        TextView saturdayHours = rootView.findViewById(R.id.saturday_hours_text);
        saturdayHours.setText(singleDayOpenHours.get(5));

        TextView sundayHours = rootView.findViewById(R.id.sunday_hours_text);
        sundayHours.setText(singleDayOpenHours.get(6));
    }
}