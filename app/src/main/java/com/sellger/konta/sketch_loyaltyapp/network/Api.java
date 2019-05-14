package com.sellger.konta.sketch_loyaltyapp.network;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;
import com.sellger.konta.sketch_loyaltyapp.data.utils.HelperMarker;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    // Get menu components
    @GET("/menu")
    Single<List<MenuComponent>> getMenuComponents();

    // Get all products
    @GET("/products")
    Single<List<Product>> getAllProducts();

    // Get single product
    @GET("/products/{id}")
    Single<Product> getSingleProduct(@Path("id") int id);

    // Get all coupons
    @GET("/coupons")
    Single<List<Coupon>> getAllCoupons();

    // Get single coupon
    @GET("/coupons/{id}")
    Single<Coupon> getSingleCoupon(@Path("id") int id);

    // Get all markers
    @GET("/localizations")
    Single<List<HelperMarker>> getAllMarkers();

    // Get single marker
    @GET("/localizations/{id}")
    Single<HelperMarker> getSingleMarker(@Path("id") int id);

    // Get all pages
    @GET("page")
    Single<List<Page>> getAllPages();

    // Get static page
    @GET("/page/{id}")
    Single<Page> getSinglePage(@Path("id") int id);
}
