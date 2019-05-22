package com.sellger.konta.sketch_loyaltyapp.ui.couponDetails;

import androidx.annotation.NonNull;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponDetailsPresenter implements CouponDetailsContract.Presenter {

    private static final String TAG = CouponDetailsPresenter.class.getSimpleName();

    @NonNull
    private CouponDetailsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    CouponDetailsPresenter(@NonNull CouponDetailsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer(int couponId) {
        loyaltyRepository.getSingleCoupon(couponId, new LoyaltyDataSource.GetSingleDataCallback() {
            @Override
            public void onDataLoaded(Object object) {
                hideProgressBar();
                passDataToView((Coupon) object);
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                view.displayToastMessage(TOAST_ERROR);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }

    @Override
    public void passDataToView(Coupon coupon) {
        view.setUpViewWithData(coupon);
    }
}
