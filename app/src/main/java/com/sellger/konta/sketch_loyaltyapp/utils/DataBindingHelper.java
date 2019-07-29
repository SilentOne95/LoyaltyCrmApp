package com.sellger.konta.sketch_loyaltyapp.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.BindingAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.EnumMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_COUPON_HEIGHT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_WIDTH;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;
import static com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivity.PACKAGE_NAME;

public class DataBindingHelper {

    // HomeAdapter
    @BindingAdapter({"itemHomeAdapter"})
    public static void setIconHomeAdapter(ImageView imageView, MenuComponent item) {
        int imageId = getApplicationContext()
                .getResources()
                .getIdentifier(item.getImage(), "drawable", PACKAGE_NAME);

            Picasso.get()
                    .load(imageId)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (shouldViewBeDisabled(item.getType())) {
                                DrawableCompat.setTint(imageView.getDrawable(),
                                        getApplicationContext().getResources().getColor(R.color.colorNavViewStateEnableFalse));
                            } else {
                                DrawableCompat.setTint(imageView.getDrawable(),
                                        getApplicationContext().getResources().getColor(R.color.colorAccent));
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
    }

    /**
     * Check if user is logged with anonymous account and paint title text if so.
     */
    @BindingAdapter({"setHomeTitleTextColor"})
    public static void setItemHomeAdapterTextColor(TextView textView, MenuComponent item) {
        if (shouldViewBeDisabled(item.getType())) {
            textView.setTextColor(getApplicationContext().getResources().getColor(R.color.colorNavViewStateEnableFalse));
        }
    }

    /**
     * Called from {@link #setIconHomeAdapter(ImageView, MenuComponent)} and
     * {@link #setItemHomeAdapterTextColor(TextView, MenuComponent)} to check if user is logged
     * anonymously.
     *
     * @param currentItemLayoutType string param
     * @return true if layout type matches criteria, otherwise false
     */
    private static boolean shouldViewBeDisabled(String currentItemLayoutType) {
        return FirebaseAuth.getInstance().getCurrentUser().isAnonymous()
                && (currentItemLayoutType.equals(LAYOUT_TYPE_COUPONS)
                || currentItemLayoutType.equals(LAYOUT_TYPE_SCANNER));
    }

    // ProductAdapter
    @BindingAdapter({"itemProductAdapter", "itemProgressBar", "itemNumOfColumns"})
    public static void setIconProductAdapter(ImageView imageView, String imageUrl,
                                             ProgressBar progressBar, Object numOfColumns) {
        int imageWidth, imageHeight;

        switch ((Integer) numOfColumns) {
            case 1:
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
            case 2:
                imageWidth = BITMAP_WIDTH_TWO_COLUMNS;
                imageHeight = BITMAP_HEIGHT_TWO_COLUMNS;
                break;
            default:
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
        }

        Picasso.get()
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS, 0))
                .error(R.drawable.no_image_available)
                .resize(imageWidth, imageHeight)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

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
    @BindingAdapter({"imageBitmap"})
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
    @BindingAdapter({"detailsImage"})
    public static void setDetailsImage(ImageView imageView, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .error(R.drawable.no_image_available)
                .into(imageView);
    }
}
