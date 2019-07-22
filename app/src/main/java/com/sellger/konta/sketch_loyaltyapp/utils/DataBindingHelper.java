package com.sellger.konta.sketch_loyaltyapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.EnumMap;
import java.util.Map;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_COUPON_HEIGHT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_WIDTH;

public class DataBindingHelper {

    // ProductDetailsActivity & CouponDetailsActivity
    public static String getFormattedPrice(Float price) {
        return String.valueOf(formatPrice(price)).concat(" zł");
    }

    /**
     * Called from {@link #getFormattedPrice(Float)} to edit price format.
     *
     * @param price get from {@link Product} item
     * @return formatted price form
     */
    private static String formatPrice(float price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(2);

        return decimalFormat.format(price);
    }

    public static String getFormattedReductionAmount(String reductionType, String reductionAmount) {
        if (reductionType.equals("amount")) {
            return "-".concat(reductionAmount).concat("zł");
        } else {
            return "-".concat(reductionAmount).concat("%");
        }
    }

    /**
     * This method generate bitmap (barcode) from string and sets to imageView.
     *
     * @param couponCode is string of barcode number
     */
    @BindingAdapter("bind:imageBitmap")
    public static void setBitmapImage(ImageView imageView, String couponCode) {
        if (couponCode == null) {
            return;
        }

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;

        try {
            result = writer.encode(couponCode, BarcodeFormat.CODE_128, BARCODE_WIDTH, BARCODE_COUPON_HEIGHT, hints);
        } catch (IllegalArgumentException | WriterException iae) {
            // Unsupported format
            return;
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

        imageView.setImageBitmap(bitmap);
    }

    // Images
    @BindingAdapter("bind:detailsImage")
    public static void setDetailsImage(ImageView imageView, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .error(R.drawable.no_image_available)
                .into(imageView);
    }
}
