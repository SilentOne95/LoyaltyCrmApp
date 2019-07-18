package com.ecommercelab.loyaltyapp.ui.couponDetails;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ecommercelab.loyaltyapp.data.LoyaltyDataSource;
import com.ecommercelab.loyaltyapp.data.LoyaltyRepository;
import com.ecommercelab.loyaltyapp.data.entity.Coupon;

import java.util.EnumMap;
import java.util.Map;

import static com.ecommercelab.loyaltyapp.Constants.BARCODE_COUPON_HEIGHT;
import static com.ecommercelab.loyaltyapp.Constants.BARCODE_WIDTH;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter {

    private static final String TAG = CouponDetailsPresenter.class.getSimpleName();

    @NonNull
    private CouponDetailsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    CouponDetailsPresenter(@NonNull CouponDetailsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    /**
     * Called from {@link CouponDetailsActivity#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     *
     * @param couponId of the item which info is going to be fetched
     */
    @Override
    public void requestDataFromServer(int couponId) {
        loyaltyRepository.getSingleCoupon(couponId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                hideProgressBar();
                passDataToView((Coupon) object);
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    /**
     * Called from {@link CouponDetailsActivity#setUpViewWithData(Coupon)} to generate barcode bitmap from string.
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
            result = writer.encode(contents, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_COUPON_HEIGHT, hints);
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

    /**
     * Called from {@link #requestDataFromServer(int)} to hide progress bar when data is fetched or not.
     */
    private void hideProgressBar() {
        view.hideProgressBar();
    }

    /**
     * Called from {@link #requestDataFromServer(int)} to pass refactored data to view.
     *
     * @param coupon item containing all details, refer {@link Coupon}
     */
    private void passDataToView(Coupon coupon) {
        view.setUpViewWithData(coupon);
    }
}
