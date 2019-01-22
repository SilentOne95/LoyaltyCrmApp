package com.example.konta.sketch_loyalityapp.ui.coupons;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.base.BaseCallbackListener;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;

import java.util.List;

public class CouponsPresenter implements CouponsContract.Presenter,
        BaseCallbackListener.ListItemsOnFinishListener<Coupon> {

    @Nullable
    private CouponsContract.View view;
    private CouponsContract.Model model;

    CouponsPresenter(@Nullable CouponsContract.View view, CouponsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Coupon> couponList) {
        if (view != null) {
            view.setUpAdapter(couponList);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
