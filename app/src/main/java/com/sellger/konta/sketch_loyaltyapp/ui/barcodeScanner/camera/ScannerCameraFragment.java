package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.camera;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.BarcodeScanningProcessor;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.CameraSource;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.CameraSourcePreview;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.FrameMetadata;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsBarcode.common.GraphicOverlay;

import java.io.IOException;
import java.util.List;

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

        getActivity().setTitle("Skaner");

        setHasOptionsMenu(true);

        // Init views
        initViews();

        createCameraSource();
    }

    @Override
    public void initViews() {
        mCameraSourcePreview = rootView.findViewById(R.id.camera_preview);
        mGraphicOverlay = rootView.findViewById(R.id.graphic_overlay);
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
    public void createCameraSource() {
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(
                        FirebaseVisionBarcode.FORMAT_UPC_A,
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

    @Override
    public void startCameraSource() {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        Log.d(TAG, "startCameraSource + " + code);

        if (code != ConnectionResult.SUCCESS) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, MY_PERMISSIONS_REQUEST_CAMERA);
            dialog.show();
        }

        if (mCameraSource != null && mCameraSourcePreview != null && mGraphicOverlay != null) {
            try {
                Log.d(TAG, "startCameraSource: ");
                mCameraSourcePreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.d(TAG, "IOException " + e);
                mCameraSource.release();
                mCameraSource = null;
            }
        } else {
            Log.d(TAG, "startCameraSource: not started");
        }
    }

    private BarcodeScanningProcessor.BarcodeResultListener getBarcodeResultListener() {
        return new BarcodeScanningProcessor.BarcodeResultListener() {
            @Override
            public void onSuccess(@Nullable Bitmap originalCameraImage,
                                  @NonNull List<FirebaseVisionBarcode> barcodes,
                                  @NonNull FrameMetadata frameMetadata,
                                  @NonNull GraphicOverlay graphicOverlay) {
                Log.d(TAG, "onSuccess: " + barcodes.size());
                if (barcodes.size() != 0) {
                    for (FirebaseVisionBarcode barcode : barcodes) {
                        Log.d(TAG, "onSuccess: " + barcode.getRawValue());
                        navigationPresenter.getSelectedLayoutType(LAYOUT_TYPE_SCANNER, barcode.getRawValue());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure");
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
