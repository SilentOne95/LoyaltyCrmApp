package com.example.konta.sketch_loyalityapp.adapters;

import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;

public interface RecyclerItemClickListener {

    interface HomeRetrofitClickListener {
        void onItemHomeClick(MenuComponent item, int selectedViewId);
    }

    interface ProductRetrofitClickListener {
        void onItemProductClick(int productId);
    }

    interface CouponRetrofitClickListener {
        void onItemCouponDetailsClick(int couponId);
        void onItemCouponCodeCheckClick(String couponCode);
    }
}
