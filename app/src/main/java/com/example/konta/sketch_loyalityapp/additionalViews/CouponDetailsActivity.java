package com.example.konta.sketch_loyalityapp.additionalViews;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
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

import com.example.konta.sketch_loyalityapp.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CouponDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private String json, layoutTitle;
    Button showCouponCodeButton;
    GradientDrawable backgroundButton;
    Spannable staticCodeText, promoCodeText;

    // Temporary variables using to get json data from assets
    private int couponPosition = 0;
    private static final String jsonFileData = "coupons.json";

    // Fields necessary to store values that will be displayed to user
    private int mCouponImageResourceId, mCouponDiscount;
    private String mCouponValidDate, mCouponTitle, mCouponDescription;
    private double mCouponBasicPrice, mCouponNewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);

        // Temporary solution - setting up sample data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            couponPosition = extras.getInt("EXTRA_ELEMENT_ID");
        }

        // Reading JSON file from assets
        json = ((MyApplication) getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data
        extractDataFromJson();

        setTitle(layoutTitle);
        setDataToRelatedViews();

        showCouponCodeButton = findViewById(R.id.show_coupon_button);
        showCouponCodeButton.setOnClickListener(this);

        backgroundButton = (GradientDrawable) showCouponCodeButton.getBackground();

        // Temporary solution to hide code when activity is stopped or paused
        getBasicButtonStyle();
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

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            String image = object.getString("contentImage");
            mCouponImageResourceId = resources
                    .getIdentifier(image, "drawable", this.getPackageName());

            mCouponDescription = object.getString("contentFullDescription");

            JSONArray array = object.getJSONArray("coupons");
            JSONObject insideObj = array.getJSONObject(couponPosition);

            mCouponTitle = insideObj.getString("contentTitle");
            mCouponBasicPrice = insideObj.getDouble("contentBasicPrice");
            mCouponNewPrice = insideObj.getDouble("contentFinalPrice");
            mCouponDiscount = insideObj.getInt("contentDiscount");
            mCouponValidDate = insideObj.getString("contentValidDate");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setDataToRelatedViews() {
        ImageView couponImage = findViewById(R.id.imageView);
        couponImage.setImageResource(mCouponImageResourceId);

        TextView couponMarker = findViewById(R.id.discount_marker_text_view);
        couponMarker.setText("-".concat(Integer.toString(mCouponDiscount)).concat("%"));

        TextView couponDate = findViewById(R.id.valid_date_text_view);
        couponDate.setText(mCouponValidDate);

        TextView couponTitle = findViewById(R.id.product_title_text_view);
        couponTitle.setText(mCouponTitle);

        TextView couponNewPrice = findViewById(R.id.price_amount_text_view);
        couponNewPrice.setText(String.valueOf(mCouponNewPrice).concat(" zł"));

        TextView couponBasicPrice = findViewById(R.id.old_price_amount_text_view);
        couponBasicPrice.setText(String.valueOf(mCouponBasicPrice).concat(" zł"));

        TextView couponDescription = findViewById(R.id.coupon_description_text_view);
        couponDescription.setText(mCouponDescription);
    }
}