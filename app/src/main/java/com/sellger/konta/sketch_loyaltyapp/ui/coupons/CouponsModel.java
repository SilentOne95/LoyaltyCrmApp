package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.data.utils.CouponData;
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
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;

public class CouponsModel implements CouponsContract.Model {

    private CouponsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(CouponsPresenter presenter) {
        this.presenter = presenter;

        return getObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(couponData -> {
                    formatCouponsData(couponData);
                    presenter.hideProgressBar();
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<CouponData> getObservable() {
        return Single.zip(getObservableMenu(), getObservableCoupons(),
                Single.timer(1000, TimeUnit.MILLISECONDS),
                (componentList, couponList, timer) -> new CouponData(componentList, couponList));
    }

    private Single<List<MenuComponent>> getObservableMenu() {
        return RetrofitClient.getInstance().create(Api.class)
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<Coupon>> getObservableCoupons() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllCoupons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void formatCouponsData(CouponData couponData) {
        int numOfColumns = 1;
        ArrayList<Coupon> validCouponsList;

        for (MenuComponent component : couponData.getComponents()) {
            if (component.getType().equals("coupons")) {
                numOfColumns = component.getNumberOfColumns();
                break;
            }
        }

        if ( numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        validCouponsList = isCouponDataValid(couponData.getCoupons());

        presenter.passDataToAdapter(validCouponsList, numOfColumns);
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
}
