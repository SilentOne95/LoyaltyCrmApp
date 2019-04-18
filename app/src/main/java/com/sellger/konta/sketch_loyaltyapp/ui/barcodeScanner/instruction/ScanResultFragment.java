package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.instruction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.coupons.CouponsFragment;

import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_CAMERA;

public class ScanResultFragment extends BaseFragment implements ScanResultContract.View, View.OnClickListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private TextView mScanResultTextView;
    private Switch mAutoFocusSwitch, mFlashSwitch;
    private Button mScanButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_scan_result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Skaner");

        setHasOptionsMenu(true);

        // Init views
        initViews();
        mScanButton.setOnClickListener(this);

        // Extract additional data
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mScanResultTextView.setText(bundle.getString("DATA_STRING"));
        }
    }

    @Override
    public void initViews() {
        mScanResultTextView = rootView.findViewById(R.id.barcode_scanner_scan_result_text_view);

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
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            navigationPresenter.getSelectedLayoutType("camera", "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted. Do the needed location-related task
                if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    navigationPresenter.getSelectedLayoutType("camera", "");
                }
            } else {
                // Permission denied, display Toast message
                Toast.makeText(getActivity(),
                        getResources().getText(R.string.camera_permission_denied), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
