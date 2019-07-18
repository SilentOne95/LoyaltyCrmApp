package com.ecommercelab.loyaltyapp.network;

import com.ecommercelab.loyaltyapp.data.entity.Coupon;
import com.ecommercelab.loyaltyapp.data.entity.Marker;
import com.ecommercelab.loyaltyapp.data.entity.MenuComponent;
import com.ecommercelab.loyaltyapp.data.entity.Product;
import com.ecommercelab.loyaltyapp.data.entity.Page;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface Api {

    // Get menu components
    @Streaming
    @GET("/menu")
    Single<List<MenuComponent>> getMenuComponents();

    // Get all products
    @Streaming
    @GET("/products")
    Single<List<Product>> getAllProducts();

    // Get single product
    @Streaming
    @GET("/products/{id}")
    Single<Product> getSingleProduct(@Path("id") int id);

    // Get all coupons
    @Streaming
    @GET("/coupons")
    Single<List<Coupon>> getAllCoupons();

    // Get single coupon
    @Streaming
    @GET("/coupons/{id}")
    Single<Coupon> getSingleCoupon(@Path("id") int id);

    // Get all markers
    @Streaming
    @GET("/localizations")
    Single<List<Marker>> getAllMarkers();

    // Get single marker
    @Streaming
    @GET("/localizations/{id}")
    Single<Marker> getSingleMarker(@Path("id") int id);

    // Get all pages
    @Streaming
    @GET("page")
    Single<List<Page>> getAllPages();

    // Get static page
    @Streaming
    @GET("/page/{id}")
    Single<Page> getSinglePage(@Path("id") int id);
}
