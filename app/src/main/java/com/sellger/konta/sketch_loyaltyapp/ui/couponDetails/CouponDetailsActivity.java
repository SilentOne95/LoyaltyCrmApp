package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_COUPON_HEIGHT;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BARCODE_WIDTH;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BASE_URL_IMAGES;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    private static final String TAG = CouponDetailsActivity.class.getSimpleName();

    private CouponDetailsPresenter presenter;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private ImageView mCouponImage;
    private TextView mCouponMarker, mCouponDate, mCouponTitle, mCouponNewPrice, mCouponBasicPrice, mCouponDescription;
    private Button showCouponCodeButton;

    private int couponId;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private View mBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ViewFlipper mViewFlipper;
    private TextView mBottomCouponCodeTextView;
    private ImageView mBottomBarcodeView, mSwitchFlipperLeftArrow, mSwitchFlipperRightArrow;

    @Override
    protected int getLayout() { return R.layout.activity_coupon_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Kupon");

        // TODO:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            couponId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        // Init views
        initViews();

        // Setting up views
        mLayoutContainer.setVisibility(View.GONE);
        showCouponCodeButton.setOnClickListener(this);

        // Setting up BottomSheet views
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mSwitchFlipperLeftArrow.setOnClickListener(this);
        mSwitchFlipperLeftArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));
        mSwitchFlipperRightArrow.setOnClickListener(this);
        mSwitchFlipperRightArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));

        presenter = new CouponDetailsPresenter(this, Injection.provideLoyaltyRepository(getApplicationContext()));
        presenter.requestDataFromServer(couponId);
    }

    @Override
    public void initViews() {
        mLayoutContainer = findViewById(R.id.layout_container);

        mProgressBar = findViewById(R.id.progress_bar);

        mCouponImage = findViewById(R.id.imageView);
        mCouponMarker = findViewById(R.id.discount_marker_text_view);
        mCouponDate = findViewById(R.id.valid_date_text_view);
        mCouponTitle = findViewById(R.id.product_title_text_view);
        mCouponNewPrice = findViewById(R.id.price_amount_text_view);
        mCouponBasicPrice = findViewById(R.id.old_price_amount_text_view);
        mCouponDescription = findViewById(R.id.coupon_description_text_view);

        showCouponCodeButton = findViewById(R.id.show_coupon_button);

        // Bottom Sheet
        mBottomSheet = findViewById(R.id.bottom_coupon_details_activity);
        mSwitchFlipperLeftArrow = findViewById(R.id.bottom_coupon_arrow_left);
        mSwitchFlipperRightArrow = findViewById(R.id.bottom_coupon_arrow_right);
        mViewFlipper = findViewById(R.id.bottom_coupon_view_flipper);
        mBottomCouponCodeTextView = findViewById(R.id.bottom_coupon_code_text);
        mBottomBarcodeView = findViewById(R.id.bottom_coupon_barcode_bitmap);
    }

    @Override
    public void hideProgressBar() {
        mLayoutContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUpViewWithData(Coupon coupon) {
        if (!TextUtils.isEmpty(coupon.getImage())) {
            // TODO: Upload images to server
            Picasso.get()
                    .load(BASE_URL_IMAGES + coupon.getImage())
                    .error(R.drawable.no_image_available)
                    .into(mCouponImage);
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .into(mCouponImage);
        }

        if (!TextUtils.isEmpty(coupon.getReductionAmount())) {
            if (coupon.getReductionType().equals("percent")) {
                mCouponMarker.setText("-".concat(coupon.getReductionAmount()).concat("%"));
            } else {
                mCouponMarker.setText("-".concat(coupon.getReductionAmount()).concat("zł"));
            }
        } else {
            mCouponMarker.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getFreshTime())) {
            mCouponDate.setText(coupon.getFreshTime());
        } else {
            mCouponDate.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getTitle())) {
            mCouponTitle.setText(coupon.getTitle());
        } else {
            mCouponTitle.setText(DEFAULT_STRING);
        }

        if (coupon.getPriceAfter() != null && !coupon.getPriceAfter().toString().trim().isEmpty()) {
            mCouponNewPrice.setText(String.valueOf(decimalFormat.format(coupon.getPriceAfter())).concat(" zł"));
        } else {
            mCouponNewPrice.setText(DEFAULT_STRING);
        }

        if (coupon.getPrice() != null && !coupon.getPrice().toString().trim().isEmpty()) {
            mCouponBasicPrice.setText(String.valueOf(decimalFormat.format(coupon.getPrice())).concat(" zł"));
        } else {
            mCouponBasicPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getDescription())) {
            mCouponDescription.setText(Html.fromHtml(coupon.getDescription()));
        }

        String couponCode;
        if (!TextUtils.isEmpty(coupon.getCouponCode())) {
            couponCode = coupon.getCouponCode();
            mBottomCouponCodeTextView.setText(couponCode);
            try {
                Bitmap bitmap = encodeAsBitmap(couponCode);
                mBottomBarcodeView.setImageBitmap(bitmap);
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
                switchBottomSheetState();
                break;
            case R.id.bottom_coupon_arrow_right:
                mViewFlipper.setInAnimation(this, R.anim.slide_in_right);
                mViewFlipper.setOutAnimation(this, R.anim.slide_out_right);
                mViewFlipper.showNext();
                break;
            case R.id.bottom_coupon_arrow_left:
                mViewFlipper.setInAnimation(this, R.anim.slide_in_left);
                mViewFlipper.setOutAnimation(this, R.anim.slide_out_left);
                mViewFlipper.showPrevious();
                break;
        }
    }

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

    @Override
    public void switchBottomSheetState() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            showCouponCodeButton.setText(R.string.hide_my_coupon_text);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            showCouponCodeButton.setText(R.string.show_my_coupon_text);
        }
    }

    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(this, message , Toast.LENGTH_LONG).show();
    }
}