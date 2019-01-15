package com.example.konta.sketch_loyalityapp.ui.coupons;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

import java.util.List;

public interface CouponsContract {

    interface View {
        void setUpAdapter(List<Coupon> couponList);
    }

    interface Presenter {
        void requestDataFromServer();
    }

    interface Model {

        void fetchDataFromServer(BaseCallbackListener.ListItemsOnFinishListener<Coupon> onFinishedListener);
    }
}
