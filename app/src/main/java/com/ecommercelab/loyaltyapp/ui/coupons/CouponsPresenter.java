package com.ecommercelab.loyaltyapp.ui.coupons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.ecommercelab.loyaltyapp.data.LoyaltyDataSource;
import com.ecommercelab.loyaltyapp.data.LoyaltyRepository;
import com.ecommercelab.loyaltyapp.data.entity.Coupon;
import com.ecommercelab.loyaltyapp.data.entity.MenuComponent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.ecommercelab.loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;
import static com.ecommercelab.loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;
import static com.ecommercelab.loyaltyapp.Constants.TOAST_ERROR;

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

    /**
     * Called from {@link CouponsFragment#onCreate(Bundle)} to fetch required data from {@link LoyaltyRepository}.
     */
    @Override
    public void requestDataFromServer(Context context) {
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
                        manageViewsDataNotAvailable(context);
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                hideProgressBar();
                manageViewsDataNotAvailable(context);
            }
        });
    }

    /**
     * Called from {@link #requestDataFromServer(Context)} whenever data is not available and need to
     * notify user about this.
     *
     * @param context of the app
     */
    private void manageViewsDataNotAvailable(Context context) {
        if (!isNetworkAvailable(context)) {
            view.changeVisibilityNoNetworkConnectionView(true);
        } else {
            view.displayToastMessage(TOAST_ERROR);
        }
    }

    /**
     * Called from {@link CouponsFragment#checkIfNetworkIsAvailableAndGetData()} to check
     * if network connection is active.
     *
     * @param context of the app
     * @return boolean depends on network status
     */
    @Override
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Called from {@link #requestDataFromServer(Context)} to refactor fetched data.
     *
     * @param couponList   of fetched items of {@link Coupon}
     * @param numOfColumns that data is going to be displayed in
     */
    private void refactorFetchedData(List<Coupon> couponList, int numOfColumns) {
        ArrayList<Coupon> validCouponsList;

        if (numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        validCouponsList = isCouponDataValid(couponList);

        hideProgressBar();
        passDataToAdapter(validCouponsList, numOfColumns);
    }

    /**
     * Called from {@link #refactorFetchedData(List, int)} to filter valid coupons.
     *
     * @param couponList of items to filter
     * @return list of valid coupons
     */
    private ArrayList<Coupon> isCouponDataValid(List<Coupon> couponList) {
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

    /**
     * Called from {@link #isCouponDataValid(List)} as helper method to parse dates.
     *
     * @param date to parse
     * @return date formatted to desired pattern
     */
    private Date parseDate(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Called from {@link #requestDataFromServer(Context)} to hide progress bar when data is fetched or not.
     */
    private void hideProgressBar() {
        view.changeVisibilityProgressBar(false);
    }

    /**
     * Called from {@link #refactorFetchedData(List, int)} to pass refactored data to adapter,
     *
     * @param couponList   of items are going to be displayed using adapter
     * @param numOfColumns that data is going to be displayed in
     */
    private void passDataToAdapter(List<Coupon> couponList, int numOfColumns) {
        view.setUpAdapter(couponList, numOfColumns);
    }
}
