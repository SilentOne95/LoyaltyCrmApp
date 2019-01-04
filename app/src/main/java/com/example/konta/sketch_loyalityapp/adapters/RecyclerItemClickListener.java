package com.example.konta.sketch_loyalityapp.adapters;

import com.example.konta.sketch_loyalityapp.modelClasses.ItemCoupon;
import com.example.konta.sketch_loyalityapp.modelClasses.ItemHome;

public interface RecyclerItemClickListener {

    interface HomeClickListener {
        void onItemHomeClick(ItemHome item);
    }

    interface ProductClickListener {
        void onItemProductClick(int productId);
    }

    interface CouponClickListener {
        void onItemCouponDetailsClick(int couponId);
        void onItemCouponCodeCheckClick(ItemCoupon item);
    }
}
