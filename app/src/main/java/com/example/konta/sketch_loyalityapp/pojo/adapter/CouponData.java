package com.example.konta.sketch_loyalityapp.pojo.adapter;

import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

import java.util.List;

public class CouponData {

    private List<MenuComponent> components;
    private List<Coupon> coupons;

    public CouponData(List<MenuComponent> componentList, List<Coupon> couponList) {
        components = componentList;
        coupons = couponList;
    }

    public void setComponents(List<MenuComponent> components) { this.components = components; }
    public void setCoupons(List<Coupon> coupons) { this.coupons = coupons; }

    public List<MenuComponent> getComponents() { return components; }
    public List<Coupon> getCoupons() { return coupons; }
}
