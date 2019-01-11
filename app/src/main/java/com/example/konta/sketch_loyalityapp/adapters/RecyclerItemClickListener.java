package com.example.konta.sketch_loyalityapp.adapters;

import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;

public interface RecyclerItemClickListener {

    interface HomeRetrofitClickListener {
        void onItemHomeClick(MenuComponent item);
    }

    interface ProductRetrofitClickListener {
        void onItemProductClick(int productId);
    }

    interface CouponRetrofitClickListener {
        void onItemCouponDetailsClick(int couponId);
        void onItemCouponCodeCheckClick(String couponCode);
    }
}
