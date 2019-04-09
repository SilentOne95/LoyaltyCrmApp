package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import com.sellger.konta.sketch_loyaltyapp.pojo.adapter.CouponData;
import com.sellger.konta.sketch_loyaltyapp.pojo.coupon.Coupon;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public interface CouponsContract {

    interface View {

        void initViews();

        void setUpAdapter(List<Coupon> couponList, int numOfColumns);
        void setUpEmptyStateView(boolean isNeeded);
        void hideProgressBar();
    }

    interface Presenter {

        void requestDataFromServer();
        void passDataToAdapter(List<Coupon> couponList, int numOfColumns);
        void hideProgressBar();
    }

    interface Model {

        Disposable fetchDataFromServer(CouponsPresenter presenter);

        void formatCouponsData(CouponData couponData);
        ArrayList<Coupon> isCouponDataValid(List<Coupon> couponList);
    }
}
