package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
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

import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    private static final String TAG = CouponDetailsActivity.class.getSimpleName();

    private CouponDetailsPresenter presenter;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private ImageView mCouponImage;
    private TextView mCouponMarker, mCouponDate, mCouponTitle, mCouponNewPrice, mCouponBasicPrice, mCouponDescription;
    private Button mShowCouponCodeButton;

    private int couponId;

    private View mBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ViewFlipper mViewFlipper;
    private TextView mBottomCouponCodeTextView;
    private ImageView mBottomBarcodeView, mSwitchFlipperLeftArrow, mSwitchFlipperRightArrow;

    @Override
    protected int getLayout() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Kupon");

        // TODO:
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            couponId = extras.getInt(EXTRAS_ELEMENT_ID);
        }

        // Init views
        initViews();

        presenter = new CouponDetailsPresenter(this, Injection.provideLoyaltyRepository(getApplicationContext()));
        presenter.requestDataFromServer(couponId);
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
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

        mShowCouponCodeButton = findViewById(R.id.show_coupon_button);

        // Bottom Sheet
        mBottomSheet = findViewById(R.id.bottom_coupon_details_activity);
        mSwitchFlipperLeftArrow = findViewById(R.id.bottom_coupon_arrow_left);
        mSwitchFlipperRightArrow = findViewById(R.id.bottom_coupon_arrow_right);
        mViewFlipper = findViewById(R.id.bottom_coupon_view_flipper);
        mBottomCouponCodeTextView = findViewById(R.id.bottom_coupon_code_text);
        mBottomBarcodeView = findViewById(R.id.bottom_coupon_barcode_bitmap);

        // Setting up views
        mLayoutContainer.setVisibility(View.GONE);
        mShowCouponCodeButton.setOnClickListener(this);

        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);

        mSwitchFlipperLeftArrow.setOnClickListener(this);
        mSwitchFlipperLeftArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));
        mSwitchFlipperRightArrow.setOnClickListener(this);
        mSwitchFlipperRightArrow.setColorFilter(new PorterDuffColorFilter(
                ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));
    }

    /**
     * Called from {@link CouponDetailsPresenter#passDataToView(Coupon)} to populate view with {@link Coupon} details.
     *
     * @param coupon item containing all details, refer {@link Coupon}
     */
    @Override
    public void setUpViewWithData(Coupon coupon) {
        if (!TextUtils.isEmpty(coupon.getImage())) {
            Picasso.get()
                    .load(coupon.getImage())
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
            mCouponNewPrice.setText(String.valueOf(formatPrice(coupon.getPriceAfter())).concat(" zł"));
        } else {
            mCouponNewPrice.setText(DEFAULT_STRING);
        }

        if (coupon.getPrice() != null && !coupon.getPrice().toString().trim().isEmpty()) {
            mCouponBasicPrice.setText(String.valueOf(formatPrice(coupon.getPrice())).concat(" zł"));
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
                Bitmap bitmap = presenter.encodeAsBitmap(couponCode);
                mBottomBarcodeView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called from {@link #setUpViewWithData(Coupon)} to edit price format.
     *
     * @param price get from {@link Coupon} item
     * @return formatted price form
     */
    private String formatPrice(float price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMaximumFractionDigits(2);

        return decimalFormat.format(price);
    }

    /**
     * This hook is called whenever an item in options menu is selected.
     *
     * @param item is a selected item
     * @return false to allow normal menu processing to proceed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called from {@link #onClick(View)} to change BottomSheetState.
     */
    private void switchBottomSheetState() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    /**
     * Implementation of callback listener that handle BottomSheet state changes.
     */
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED
                    && !mShowCouponCodeButton.getText().equals(getText(R.string.show_my_coupon_text))) {
                mShowCouponCodeButton.setText(R.string.show_my_coupon_text);
            } else if (newState == BottomSheetBehavior.STATE_EXPANDED
                    && !mShowCouponCodeButton.getText().equals(getText(R.string.hide_my_coupon_text))){
                mShowCouponCodeButton.setText(R.string.hide_my_coupon_text);
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param view which was clicked
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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

    /**
     * Called from {@link CouponDetailsPresenter#hideProgressBar()} to hide progress bar when data is fetched or not.
     */
    @Override
    public void hideProgressBar() {
        mLayoutContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Called from {@link CouponDetailsPresenter#requestDataFromServer(int)} whenever data is
     * unavailable to get.
     *
     * @param message is a string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}