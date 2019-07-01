package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.instruction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.ui.coupons.CouponsFragment;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_DATA_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_DATA_EMPTY_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_CAMERA;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_CAMERA;

public class ScanResultFragment extends BaseFragment implements ScanResultContract.View, View.OnClickListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private TextView mScanResultTextView;
    private Button mScanButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_scan_result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // Init views
        initViews();
        mScanButton.setOnClickListener(this);

        // Extract additional data, which is fragment's title and scan result
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
            if (!TextUtils.isEmpty(bundle.getString(BUNDLE_DATA_STRING))) {
                mScanResultTextView.setText(bundle.getString(BUNDLE_DATA_STRING));
            }
        } else {
            getActivity().setTitle("Skaner");
        }
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mScanResultTextView = rootView.findViewById(R.id.barcode_scanner_scan_result_text_view);
        mScanButton = rootView.findViewById(R.id.barcode_scanner_scan_button);
    }

    /**
     * Initialize the contents of the Activity's standard options menu and sets up items visibility.
     *
     * @param menu in which you place items
     * @param inflater menu inflater
     */
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

    /**
     * Called from {@link #onClick(View)} when view to open camera is clicked to check permission.
     */
    private void checkCameraPermission() {
        if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_CAMERA, LAYOUT_DATA_EMPTY_STRING);
        }
    }

    /**
     * Retrieves the results for camera permission.
     *
     * @param requestCode  is an int of permission that was requested
     * @param permissions  that were requested
     * @param grantResults are results for the corresponding permissions which is either granted or denied
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted. Do the needed location-related task
                if (getActivity() != null && ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {

                    navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_CAMERA, LAYOUT_DATA_EMPTY_STRING);
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
