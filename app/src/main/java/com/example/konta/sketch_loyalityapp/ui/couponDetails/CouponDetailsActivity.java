package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;

import static com.example.konta.sketch_loyalityapp.Constants.BARCODE_COUPON_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.BARCODE_WIDTH;
import static com.example.konta.sketch_loyalityapp.Constants.BASE_URL_IMAGES;
import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    private static final String TAG = CouponDetailsActivity.class.getSimpleName();

    CouponDetailsPresenter presenter;

    private ProgressBar progressBar;
    private View layoutContainer;
    private ImageView couponImage;
    private TextView couponMarker, couponDate, couponTitle, couponNewPrice, couponBasicPrice, couponDescription;
    private Button showCouponCodeButton;

    private int couponId;
    private String couponCode;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private BottomSheetBehavior bottomSheetBehavior;
    private ViewFlipper viewFlipper;
    private TextView bottomCouponCodeTextView;
    private ImageView bottomBarcodeView, switchFlipperLeftArrow, switchFlipperRightArrow;
    private Bitmap bitmap = null;

    @Override
    protected int getLayout() { return R.layout.activity_coupon_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Kupon");

        // Receiving id of the clicked coupon
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            couponId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        layoutContainer = findViewById(R.id.layout_container);
        layoutContainer.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress_bar);

        couponImage = findViewById(R.id.imageView);
        couponMarker = findViewById(R.id.discount_marker_text_view);
        couponDate = findViewById(R.id.valid_date_text_view);
        couponTitle = findViewById(R.id.product_title_text_view);
        couponNewPrice = findViewById(R.id.price_amount_text_view);
        couponBasicPrice = findViewById(R.id.old_price_amount_text_view);
        couponDescription = findViewById(R.id.coupon_description_text_view);

        showCouponCodeButton = findViewById(R.id.show_coupon_button);
        showCouponCodeButton.setOnClickListener(this);

        // Bottom Sheet
        View bottomSheet = findViewById(R.id.bottom_coupon_details_activity);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        switchFlipperLeftArrow = findViewById(R.id.bottom_coupon_arrow_left);
        switchFlipperRightArrow = findViewById(R.id.bottom_coupon_arrow_right);
        viewFlipper = findViewById(R.id.bottom_coupon_view_flipper);
        bottomCouponCodeTextView = findViewById(R.id.bottom_coupon_code_text);
        bottomBarcodeView = findViewById(R.id.bottom_coupon_barcode_bitmap);

        switchFlipperLeftArrow.setOnClickListener(this);
        switchFlipperLeftArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));
        switchFlipperRightArrow.setOnClickListener(this);
        switchFlipperRightArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));

        presenter = new CouponDetailsPresenter(this, new CouponDetailsModel());
        presenter.requestDataFromServer(couponId);
    }

    @Override
    public void hideProgressBar() {
        layoutContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUpViewWithData(Coupon coupon) {
        if (!TextUtils.isEmpty(coupon.getImage())) {
            // TODO: Upload images to server
            Picasso.get()
                    .load(BASE_URL_IMAGES + coupon.getImage())
                    .error(R.drawable.no_image_available)
                    .into(couponImage);
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .into(couponImage);
        }

        if (!TextUtils.isEmpty(coupon.getReductionAmount())) {
            if (coupon.getReductionType().equals("percent")) {
                couponMarker.setText("-".concat(coupon.getReductionAmount()).concat("%"));
            } else {
                couponMarker.setText("-".concat(coupon.getReductionAmount()).concat("zł"));
            }
        } else {
            couponMarker.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getFreshTime())) {
            couponDate.setText(coupon.getFreshTime());
        } else {
            couponDate.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getTitle())) {
            couponTitle.setText(coupon.getTitle());
        } else {
            couponTitle.setText(DEFAULT_STRING);
        }

        if (coupon.getPriceAfter() != null && !coupon.getPriceAfter().toString().trim().isEmpty()) {
            couponNewPrice.setText(String.valueOf(decimalFormat.format(coupon.getPriceAfter())).concat(" zł"));
        } else {
            couponNewPrice.setText(DEFAULT_STRING);
        }

        if (coupon.getPrice() != null && !coupon.getPrice().toString().trim().isEmpty()) {
            couponBasicPrice.setText(String.valueOf(decimalFormat.format(coupon.getPrice())).concat(" zł"));
        } else {
            couponBasicPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getDescription())) {
            couponDescription.setText(Html.fromHtml(coupon.getDescription()));
        }

        if (!TextUtils.isEmpty(coupon.getCouponCode())) {
            couponCode = coupon.getCouponCode();
            bottomCouponCodeTextView.setText(couponCode);
            try {
                bitmap = encodeAsBitmap(couponCode);
                bottomBarcodeView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        } else {
            couponCode = DEFAULT_STRING;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_coupon_button:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.bottom_coupon_arrow_right:
                viewFlipper.setInAnimation(this, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_right);
                viewFlipper.showNext();
                break;
            case R.id.bottom_coupon_arrow_left:
                viewFlipper.setInAnimation(this, R.anim.slide_in_left);
                viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                viewFlipper.showPrevious();
                break;
        }
    }

    private Bitmap encodeAsBitmap(String contents) throws WriterException {
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
}