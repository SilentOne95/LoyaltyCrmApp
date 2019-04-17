package com.sellger.konta.sketch_loyaltyapp.ui.barcodeScanner.camera;

public interface ScannerCameraContract {

    interface View {

        void initViews();
        void createCameraSource();
        void startCameraSource();
    }
}
