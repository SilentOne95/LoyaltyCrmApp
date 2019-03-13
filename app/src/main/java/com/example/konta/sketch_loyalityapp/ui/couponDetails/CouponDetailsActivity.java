package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class CouponDetailsActivity extends BaseActivity implements CouponDetailsContract.View, View.OnClickListener {

    private static final String TAG = CouponDetailsActivity.class.getSimpleName();

    CouponDetailsPresenter presenter;

    private Button showCouponCodeButton;
    private GradientDrawable backgroundButton;
    private Spannable staticCodeText, promoCodeText;

    private ImageView couponImage;
    private TextView couponMarker, couponDate, couponTitle, couponNewPrice, couponBasicPrice, couponDescription;
    private ProgressBar progressBar;
    private View layoutContainer;

    private int couponId;
    private String couponCode;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

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

        backgroundButton = (GradientDrawable) showCouponCodeButton.getBackground();

        // Temporary solution to hide code when activity is stopped or paused
        getBasicButtonStyle();

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
        } else {
            Picasso.get().load(R.drawable.image_coupon).into(couponImage);
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
        } else {
            couponDescription.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(coupon.getCouponCode())) {
            couponCode = coupon.getCouponCode();
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
        replaceButtonStyle();
    }

    private void replaceButtonStyle() {
        // Setting up text which will be displayed on button when it's clicked
        staticCodeText = new SpannableString(getResources().getString(R.string.coupon_details_code_text));
        staticCodeText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, staticCodeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        promoCodeText = new SpannableString(couponCode);
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