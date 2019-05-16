package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyDataSource;
import com.sellger.konta.sketch_loyaltyapp.data.LoyaltyRepository;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;

public class CouponsPresenter implements CouponsContract.Presenter {

    @Nullable
    private CouponsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    CouponsPresenter(@Nullable CouponsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
        this.view = view;
        this.loyaltyRepository = loyaltyRepository;
    }

    @Override
    public void requestDataFromServer() {
        loyaltyRepository.getAllCoupons(new LoyaltyDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<?> data) {
                List<Coupon> couponList = (List<Coupon>) data;

                loyaltyRepository.getMenu(new LoyaltyDataSource.LoadDataCallback() {
                    @Override
                    public void onDataLoaded(List<?> data) {
                        for (Object object : data) {
                            if (((MenuComponent) object).getType().equals(LAYOUT_TYPE_COUPONS)) {
                                refactorFetchedData(couponList, ((MenuComponent) object).getNumberOfColumns());
                            }
                        }
                    }

                    @Override
                    public void onDataNotAvailable() {
                        hideProgressBar();
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
            }
        });
    }

    @Override
    public void refactorFetchedData(List<Coupon> couponList, int numOfColumns) {
        ArrayList<Coupon> validCouponsList;

        if (numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        validCouponsList = isCouponDataValid(couponList);

        hideProgressBar();
        passDataToAdapter(validCouponsList, numOfColumns);
    }

    @Override
    public ArrayList<Coupon> isCouponDataValid(List<Coupon> couponList) {
        ArrayList<Coupon> validCouponsList = new ArrayList<>();
        SimpleDateFormat currentCouponDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pl", "PL"));
        Date couponDate = new Date();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();

        for (Coupon coupon : couponList) {

            try {
                couponDate = currentCouponDateFormat.parse(coupon.getFreshTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!date.after(couponDate)) {
                coupon.setFreshTime(newDateFormat.format(couponDate));
                validCouponsList.add(coupon);
            }
        }

        return validCouponsList;
    }

    @Override
    public void hideProgressBar() {
        if (view != null) {
            view.hideProgressBar();
        }
    }

    @Override
    public void passDataToAdapter(List<Coupon> couponList, int numOfColumns) {
        if (view != null) {
            view.setUpAdapter(couponList, numOfColumns);
        }
    }
}
