package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    CouponDetailsPresenter presenter;

    Button showCouponCodeButton;
    GradientDrawable backgroundButton;
    Spannable staticCodeText, promoCodeText;

    // Temporary variables using to get json data from assets
    private int couponId;

    @Override
    protected int getLayout() { return R.layout.activity_coupon_details; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Coupon");

        // Receiving id of the clicked coupon
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            couponId = extras.getInt("EXTRA_ELEMENT_ID");
        }

        presenter = new CouponDetailsPresenter(this, new CouponDetailsModel());
        presenter.requestDataFromServer(couponId);


        showCouponCodeButton = findViewById(R.id.show_coupon_button);
        showCouponCodeButton.setOnClickListener(this);

        backgroundButton = (GradientDrawable) showCouponCodeButton.getBackground();

        // Temporary solution to hide code when activity is stopped or paused
        getBasicButtonStyle();
    }

    @Override
    public void setUpViewWithData(Coupon coupon) {
        ImageView couponImage = findViewById(R.id.imageView);

        TextView couponMarker = findViewById(R.id.discount_marker_text_view);
        couponMarker.setText("-".concat(coupon.getReductionAmount()).concat("%"));

        TextView couponDate = findViewById(R.id.valid_date_text_view);
        couponDate.setText(presenter.formatDateString(coupon.getFreshTime()));

        TextView couponTitle = findViewById(R.id.product_title_text_view);
        couponTitle.setText(coupon.getTitle());

        TextView couponNewPrice = findViewById(R.id.price_amount_text_view);
        couponNewPrice.setText(String.valueOf(coupon.getPriceAfter()).concat(" zł"));

        TextView couponBasicPrice = findViewById(R.id.old_price_amount_text_view);
        couponBasicPrice.setText(String.valueOf(coupon.getPrice()).concat(" zł"));

        TextView couponDescription = findViewById(R.id.coupon_description_text_view);
        couponDescription.setText(coupon.getDescription());
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
        replaceButtonStyle();
    }

    private void replaceButtonStyle() {
        // Setting up text which will be displayed on button when it's clicked
        staticCodeText = new SpannableString(getResources().getString(R.string.coupon_details_code_text));
        staticCodeText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, staticCodeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        promoCodeText = new SpannableString("CWVZ85F");
        promoCodeText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, promoCodeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        promoCodeText.setSpan(new StyleSpan(Typeface.BOLD), 0, promoCodeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        // Set new text and color
        showCouponCodeButton.setText(TextUtils.concat(staticCodeText, "  ", promoCodeText));
        backgroundButton.setStroke(5, getResources().getColor(R.color.colorBlack));
        backgroundButton.setColor(getResources().getColor(R.color.colorPrimary));
    }

    private void getBasicButtonStyle() {
        showCouponCodeButton.setText(getResources().getText(R.string.show_my_coupon_text));
        backgroundButton.setColor(getResources().getColor(R.color.colorAccent));
        backgroundButton.setStroke(3, getResources().getColor(R.color.colorAccent));
    }
}