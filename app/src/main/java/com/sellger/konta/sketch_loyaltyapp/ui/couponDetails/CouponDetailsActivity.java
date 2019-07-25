package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.databinding.ActivityCouponDetailsBinding;

import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    private static final String TAG = CouponDetailsActivity.class.getSimpleName();

    private CouponDetailsPresenter presenter;
    private ActivityCouponDetailsBinding mBinding;

    private ProgressBar mProgressBar;
    private View mLayoutContainer;
    private Button mShowCouponCodeButton;

    private int couponId;

    private View mBottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ViewFlipper mViewFlipper;
    private ImageView mSwitchFlipperLeftArrow, mSwitchFlipperRightArrow;

    @Override
    protected int getLayout() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_coupon_details);

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
        mShowCouponCodeButton = findViewById(R.id.show_coupon_button);

        // Bottom Sheet
        mBottomSheet = findViewById(R.id.bottom_coupon_details_activity);
        mSwitchFlipperLeftArrow = findViewById(R.id.bottom_coupon_arrow_left);
        mSwitchFlipperRightArrow = findViewById(R.id.bottom_coupon_arrow_right);
        mViewFlipper = findViewById(R.id.bottom_coupon_view_flipper);

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
        mBinding.setItem(coupon);
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