package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface CouponsContract {

    interface View {

        void setUpAdapter(List<Coupon> couponList, int numOfColumns);

        void setUpEmptyStateView(boolean isNeeded);

        void hideProgressBar();

        void displayToastMessage(String message);
    }

    interface Presenter {

        void requestDataFromServer();
    }
}
