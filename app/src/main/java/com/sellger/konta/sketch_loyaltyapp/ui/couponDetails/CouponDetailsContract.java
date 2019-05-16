package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import io.reactivex.disposables.Disposable;

public interface CouponDetailsContract {

    interface View {

        void initViews();

        void hideProgressBar();

        void setUpViewWithData(Coupon coupon);

        Bitmap encodeAsBitmap(String contents) throws WriterException;

        void switchBottomSheetState();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer(int couponId);

        void hideProgressBar();

        void passDataToView(Coupon coupon);
    }
}
