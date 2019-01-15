package com.example.konta.sketch_loyalityapp.ui.couponDetails;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

public interface CouponDetailsContract {

    interface View {
        void setUpViewWithData(Coupon coupon);
    }

    interface Presenter {
        void requestDataFromServer(int couponId);

        String formatDateString(String dateString);
    }

    interface Model {

        void fetchDataFromServer(BaseCallbackListener.SingleItemOnFinishListener<Coupon> onFinishedListener,
                                 int couponId);
    }
}
