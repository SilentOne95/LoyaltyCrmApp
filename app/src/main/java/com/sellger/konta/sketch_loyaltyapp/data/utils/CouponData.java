package com.sellger.konta.sketch_loyaltyapp.data.utils;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;

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
