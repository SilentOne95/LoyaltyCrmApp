package com.ecommercelab.loyaltyapp.ui.couponDetails;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.ecommercelab.loyaltyapp.data.entity.Coupon;

public interface CouponDetailsContract {

    interface View {

        void setUpViewWithData(Coupon coupon);

        void hideProgressBar();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);

        Bitmap encodeAsBitmap(String contents) throws WriterException;
    }
}
