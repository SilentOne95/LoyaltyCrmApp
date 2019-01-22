package com.example.konta.sketch_loyalityapp.network;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.pojo.map.Marker;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.example.konta.sketch_loyalityapp.pojo.staticPage.Page;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    // Get menu components
    @GET("/app_loyalty_app/get_all/0")
    Single<List<MenuComponent>> getMenuComponents();

    // Get all products
    @GET("/app_loyalty_product/get_all/0")
    Single<List<Product>> getAllProducts();

    // Get single product
    @GET("/app_loyalty_product/get/{id}")
    Single<Product> getSingleProduct(@Path("id") int id);

    // Get all coupons
    @GET("/app_loyalty_product_coupon/get_all/0")
    Single<List<Coupon>> getAllCoupons();

    // Get single coupon
    @GET("/app_loyalty_product_coupon/get/{id}")
    Single<Coupon> getSingleCoupon(@Path("id") int id);

    // Get all markers
    @GET("/app_loyalty_map/get_all/0")
    Single<List<Marker>> getAllMarkers();

    // Get static page
    @GET("/app_loyalty_page/get/{id}")
    Single<Page> getStaticPage(@Path("id") int id);
}
