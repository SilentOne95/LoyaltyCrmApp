package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import androidx.annotation.NonNull;

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
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponsPresenter implements CouponsContract.Presenter {

    private static final String TAG = CouponsPresenter.class.getSimpleName();

    @NonNull
    private CouponsContract.View view;

    @NonNull
    private LoyaltyRepository loyaltyRepository;

    private ArrayList<Coupon> validCouponsList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pl", "PL"));
    private Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"));

    CouponsPresenter(@NonNull CouponsContract.View view, @NonNull LoyaltyRepository loyaltyRepository) {
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
                        view.displayToastMessage(TOAST_ERROR);
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                view.displayToastMessage(TOAST_ERROR);
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
        calendar.add(Calendar.DATE, 1);

        for (Coupon coupon : couponList) {
            Date couponDate = parseDate(coupon.getFreshTime());

            if (!parseDate(simpleDateFormat.format(calendar.getTime())).after(couponDate)) {
                coupon.setFreshTime(simpleDateFormat.format(couponDate));
                validCouponsList.add(coupon);
            }
        }

        return validCouponsList;
    }

    @Override
    public Date parseDate(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void hideProgressBar() {
        view.hideProgressBar();
    }

    @Override
    public void passDataToAdapter(List<Coupon> couponList, int numOfColumns) {
        view.setUpAdapter(couponList, numOfColumns);
    }
}
