package com.example.konta.sketch_loyalityapp.ui.barcodeScanner;

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

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.ui.coupons.CouponsFragment;

public class BarcodeScanner extends BaseFragment implements View.OnClickListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private TextView scanResultText;
    private Switch autoFocusSwitch, flashSwitch;
    private Button scanButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_barcode_scanner;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Skaner");

        autoFocusSwitch = rootView.findViewById(R.id.barcode_scanner_switch_button_one);
        flashSwitch = rootView.findViewById(R.id.barcode_scanner_switch_button_two);

        scanButton = rootView.findViewById(R.id.barcode_scanner_scan_button);
        scanButton.setOnClickListener(this);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        searchItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v) {

    }
}
