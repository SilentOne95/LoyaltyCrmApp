package com.sellger.konta.sketch_loyaltyapp.network;

import com.sellger.konta.sketch_loyaltyapp.pojo.coupon.Coupon;
import com.sellger.konta.sketch_loyaltyapp.pojo.map.Marker;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;
import com.sellger.konta.sketch_loyaltyapp.pojo.staticPage.Page;

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
    Single<List<Marker>> getAllMarkers();

    // Get static page
    @GET("/page/{id}")
    Single<Page> getStaticPage(@Path("id") int id);
}