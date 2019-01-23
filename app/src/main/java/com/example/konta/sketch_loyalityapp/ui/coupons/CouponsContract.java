package com.example.konta.sketch_loyalityapp.ui.coupons;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface CouponsContract {

    interface View {

        void setUpAdapter(List<Coupon> couponList);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(List<Coupon> couponList);
    }

    interface Model {

        Disposable fetchDataFromServer(CouponsPresenter presenter);
    }
}
