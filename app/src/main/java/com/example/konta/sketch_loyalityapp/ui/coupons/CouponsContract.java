package com.example.konta.sketch_loyalityapp.ui.coupons;

import com.example.konta.sketch_loyalityapp.pojo.adapter.CouponData;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface CouponsContract {

    interface View {

        void setUpAdapter(List<Coupon> couponList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void setProgressBarVisibility(boolean isNeeded);
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(List<Coupon> couponList, int numOfColumns);
        void isProgressBarNeeded(boolean isNeeded);
    }

    interface Model {

        Disposable fetchDataFromServer(CouponsPresenter presenter);

        void formatCouponsData(CouponData couponData);
    }
}
