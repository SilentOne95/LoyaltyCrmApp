package com.example.konta.sketch_loyalityapp.AdditionalUI;

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
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;

import com.example.konta.sketch_loyalityapp.R;

public class CouponDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button showCouponCodeButton;
    GradientDrawable backgroundButton;
    Spannable staticCodeText, promoCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);
        setTitle("Details");

        showCouponCodeButton = findViewById(R.id.show_coupon_button);
        showCouponCodeButton.setOnClickListener(this);

        backgroundButton = (GradientDrawable) showCouponCodeButton.getBackground();

        // Temporary solution to hide code when activity is stopped or paused
        getBasicButtonStyle();
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