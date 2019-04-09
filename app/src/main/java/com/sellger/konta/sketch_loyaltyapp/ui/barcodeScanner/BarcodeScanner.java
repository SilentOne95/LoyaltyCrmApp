package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.coupons.CouponsFragment;

public class BarcodeScanner extends BaseFragment implements View.OnClickListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private TextView mScanResultText;
    private Switch mAutoFocusSwitch, mFlashSwitch;
    private Button mScanButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_barcode_scanner;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Skaner");

        setHasOptionsMenu(true);

        // Init views
        initViews();
        mScanButton.setOnClickListener(this);
    }

    public void initViews() {
        mAutoFocusSwitch = rootView.findViewById(R.id.barcode_scanner_switch_button_one);
        mFlashSwitch = rootView.findViewById(R.id.barcode_scanner_switch_button_two);

        mScanButton = rootView.findViewById(R.id.barcode_scanner_scan_button);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        searchItem.setVisible(false);
        optionsItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {

    }
}
