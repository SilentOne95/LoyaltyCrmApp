package com.example.konta.sketch_loyalityapp.ui.products;

import com.example.konta.sketch_loyalityapp.network.Api;
import com.example.konta.sketch_loyalityapp.network.RetrofitClient;
import com.example.konta.sketch_loyalityapp.pojo.adapter.ProductData;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_NUM_OF_COLUMNS;

public class ProductsModel implements ProductsContract.Model {

    private ProductsPresenter presenter;

    @Override
    public Disposable fetchDataFromServer(ProductsPresenter presenter) {
        this.presenter = presenter;
        return getObservable().subscribeWith(getObserverProductAdapter());
    }

    private Single<ProductData> getObservable() {
        return Single.zip(getObservableMenu(), getObservableProducts(),
                new BiFunction<List<MenuComponent>, List<Product>, ProductData>() {
                    @Override
                    public ProductData apply(List<MenuComponent> componentList, List<Product> productList) {
                        return new ProductData(componentList, productList);
                    }
                });
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

    private DisposableSingleObserver<ProductData> getObserverProductAdapter() {
        return new DisposableSingleObserver<ProductData>() {
            @Override
            public void onSuccess(ProductData productData) {
                formatProductsData(productData);
            }

            @Override
            public void onError(Throwable e) { }
        };
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
