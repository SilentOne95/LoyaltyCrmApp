package com.example.konta.sketch_loyalityapp.barcodeScanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
        return R.layout.activity_barcode_scanner;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoFocusSwitch = rootView.findViewById(R.id.switch_one);
        flashSwitch = rootView.findViewById(R.id.switch_two);

        scanButton = rootView.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
