package com.example.konta.sketch_loyalityapp.ui.couponsFragment;

import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

import java.util.List;

public class CouponsPresenter implements CouponsContract.Presenter,
        CouponsContract.Model.OnFinishedListener {

    private CouponsContract.View view;
    private CouponsContract.Model model;

    CouponsPresenter(CouponsContract.View view, CouponsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void requestDataFromServer() {
        model.fetchDataFromServer(this);
    }

    @Override
    public void onFinished(List<Coupon> couponList) {
        view.setUpAdapter(couponList);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
