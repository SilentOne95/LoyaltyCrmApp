package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.camera;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.BarcodeScanningProcessor;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.CameraSource;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.CameraSourcePreview;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.FrameMetadata;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.GraphicOverlay;

import java.io.IOException;
import java.util.List;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.Constants.MY_PERMISSIONS_REQUEST_CAMERA;

public class ScannerCameraFragment extends BaseFragment implements ScannerCameraContract.View {

    private static final String TAG = ScannerCameraFragment.class.getSimpleName();

    private CameraSource mCameraSource = null;
    private CameraSourcePreview mCameraSourcePreview;
    private GraphicOverlay mGraphicOverlay;
    private BarcodeScanningProcessor mBarcodeScanningProcessor;

    @Override
    protected int getLayout() {
        return R.layout.fragment_scanner_camera;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Extract additional data, which is fragment's title
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
        } else {
            getActivity().setTitle("Skaner");
        }

        setHasOptionsMenu(true);

        // Init views
        initViews();

        createCameraSource();
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mCameraSourcePreview = rootView.findViewById(R.id.camera_preview);
        mGraphicOverlay = rootView.findViewById(R.id.graphic_overlay);
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

    /**
     * Called from {@link #onCreate(Bundle)} to prepare camera to kick off.
     */
    private void createCameraSource() {
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_UPC_A,
                        FirebaseVisionBarcode.FORMAT_EAN_13,
                        FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);

        mCameraSource = new CameraSource(getActivity(), mGraphicOverlay);
        mCameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);

        mBarcodeScanningProcessor = new BarcodeScanningProcessor(detector);
        mBarcodeScanningProcessor.setBarcodeResultListener(getBarcodeResultListener());

        mCameraSource.setMachineLearningFrameProcessor(mBarcodeScanningProcessor);

        startCameraSource();
    }

    /**
     * Called from {@link #createCameraSource()} to kick off camera.
     */
    private void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());

        if (code != ConnectionResult.SUCCESS) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, MY_PERMISSIONS_REQUEST_CAMERA);
            dialog.show();
        }

        if (mCameraSource != null && mCameraSourcePreview != null && mGraphicOverlay != null) {
            try {
                mCameraSourcePreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    /**
     * Implementation of listener that handle camera barcode scan results.
     */
    private BarcodeScanningProcessor.BarcodeResultListener getBarcodeResultListener() {
        return new BarcodeScanningProcessor.BarcodeResultListener() {
            @Override
            public void onSuccess(@Nullable Bitmap originalCameraImage,
                                  @NonNull List<FirebaseVisionBarcode> barcodes,
                                  @NonNull FrameMetadata frameMetadata,
                                  @NonNull GraphicOverlay graphicOverlay) {
                if (barcodes.size() != 0) {
                    for (FirebaseVisionBarcode barcode : barcodes) {
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_SCANNER, barcode.getRawValue());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }
}
