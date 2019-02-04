package com.example.konta.sketch_loyalityapp.ui.coupons;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.adapter.CouponData;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_NUM_OF_COLUMNS;

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
                    presenter.isProgressBarNeeded(false);
                }, throwable -> presenter.isProgressBarNeeded(false));
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

        for (MenuComponent component : couponData.getComponents()) {
            if (component.getType().equals("coupons")) {
                numOfColumns = component.getNumberOfColumns();
                break;
            }
        }

        if ( numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        presenter.passDataToAdapter(couponData.getCoupons(), numOfColumns);
    }
}
