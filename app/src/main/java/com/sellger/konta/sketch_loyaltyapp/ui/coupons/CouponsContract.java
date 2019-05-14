package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import java.util.ArrayList;
import java.util.List;

public interface CouponsContract {

    interface View {

        void initViews();

        void setUpAdapter(List<Coupon> couponList, int numOfColumns);

        void setUpEmptyStateView(boolean isNeeded);

        void hideProgressBar();
    }

    interface Presenter {

        void requestDataFromServer();

        void refactorFetchedData(List<Coupon> couponList, int numOfColumns);

        ArrayList<Coupon> isCouponDataValid(List<Coupon> couponList);

        void hideProgressBar();

        void passDataToAdapter(List<Coupon> couponList, int numOfColumns);
    }
}
