package com.ecommercelab.loyaltyapp.ui.myAccount;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import static com.ecommercelab.loyaltyapp.Constants.BARCODE_HEIGHT;
import static com.ecommercelab.loyaltyapp.Constants.BARCODE_WIDTH;

public class MyAccountPresenter implements MyAccountContract.Presenter {

    private static final String TAG = MyAccountPresenter.class.getSimpleName();

    @NonNull
    private MyAccountContract.View view;

    MyAccountPresenter(@NonNull MyAccountContract.View view) {
        this.view = view;
    }

    /**
     * Called from {@link MyAccountFragment#onViewCreated(View, Bundle)} to generate barcode bitmap from string.
     *
     * @param contents is string of barcode number
     * @return generated barcode bitmap from given string
     * @throws WriterException covers the range of exceptions which may occur when encoding a barcode
     */
    @Override
    public Bitmap encodeAsBitmap(String contents) throws WriterException {
        if (contents == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;

        try {
            result = writer.encode(contents, BarcodeFormat.UPC_A, BARCODE_WIDTH, BARCODE_HEIGHT, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
