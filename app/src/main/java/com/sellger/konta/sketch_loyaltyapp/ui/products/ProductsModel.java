package com.sellger.konta.sketch_loyaltyapp.ui.products;

import com.sellger.konta.sketch_loyaltyapp.network.Api;
import com.sellger.konta.sketch_loyaltyapp.network.RetrofitClient;
import com.sellger.konta.sketch_loyaltyapp.pojo.adapter.ProductData;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_NUM_OF_COLUMNS;

public class ProductsModel implements ProductsContract.Model {

    private ProductsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(ProductsPresenter presenter) {
        this.presenter = presenter;

        return getObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productData -> {
                    formatProductsData(productData);
                    presenter.hideProgressBar();
                }, throwable -> presenter.hideProgressBar());
    }

    private Single<ProductData> getObservable() {
        return Single.zip(getObservableMenu(), getObservableProducts(),
                Single.timer(1000, TimeUnit.MILLISECONDS),
                ((componentList, productList, timer) -> new ProductData(componentList, productList)));
    }

    private Single<List<MenuComponent>> getObservableMenu() {
        return RetrofitClient.getInstance().create(Api.class)
                .getMenuComponents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<List<Product>> getObservableProducts() {
        return RetrofitClient.getInstance().create(Api.class)
                .getAllProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void formatProductsData(ProductData productData) {
        int numOfColumns = 1;

        for (MenuComponent component : productData.getComponents()) {
            if (component.getType().equals("products")) {
                numOfColumns = component.getNumberOfColumns();
                break;
            }
        }

        if ( numOfColumns < 1 || numOfColumns > 3) {
            numOfColumns = DEFAULT_NUM_OF_COLUMNS;
        }

        presenter.passDataToAdapter(productData.getProducts(), numOfColumns);
    }
}
