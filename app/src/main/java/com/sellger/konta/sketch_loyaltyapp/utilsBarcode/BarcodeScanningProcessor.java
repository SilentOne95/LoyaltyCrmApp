// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.sellger.konta.sketch_loyaltyapp.utilsBarcode;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.sellger.konta.sketch_loyaltyapp.base.VisionProcessorBase;
import com.sellger.konta.sketch_loyaltyapp.utilsBarcode.common.CameraImageGraphic;
import com.sellger.konta.sketch_loyaltyapp.utilsBarcode.common.FrameMetadata;
import com.sellger.konta.sketch_loyaltyapp.utilsBarcode.common.GraphicOverlay;

import java.io.IOException;
import java.util.List;

public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = BarcodeScanningProcessor.class.getSimpleName();

    private final FirebaseVisionBarcodeDetector detector;

    BarcodeResultListener barcodeResultListener;

    public BarcodeScanningProcessor(FirebaseVisionBarcodeDetector detector) {
        this.detector = detector;
    }

    public void setBarcodeResultListener(BarcodeResultListener barcodeResultListener) {
        this.barcodeResultListener = barcodeResultListener;
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
            graphicOverlay.add(barcodeGraphic);
        }
        graphicOverlay.postInvalidate();

        if(barcodeResultListener!=null)
            barcodeResultListener.onSuccess(originalCameraImage,barcodes,frameMetadata,graphicOverlay);

    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Barcode detection failed " + e);
        if(barcodeResultListener!=null)
            barcodeResultListener.onFailure(e);
    }

    public interface BarcodeResultListener
    {
        void onSuccess(
                @Nullable Bitmap originalCameraImage,
                @NonNull List<FirebaseVisionBarcode> barcodes,
                @NonNull FrameMetadata frameMetadata,
                @NonNull GraphicOverlay graphicOverlay);

        void onFailure(@NonNull Exception e);
    }
}
