package com.example.konta.sketch_loyalityapp.adapters;

import com.example.konta.sketch_loyalityapp.modelClasses.ItemCoupon;
import com.example.konta.sketch_loyalityapp.modelClasses.ItemHome;
import com.example.konta.sketch_loyalityapp.modelClasses.ItemProduct;

public interface RecyclerItemClickListener {

    interface HomeClickListener {
        void onItemHomeClick(ItemHome item);
    }

    interface ProductClickListener {
        void onItemProductClick(ItemProduct item);
    }

    interface CouponClickListener {
        void onItemCouponDetailsClick(ItemCoupon item);
        void onItemCouponCodeCheckClick(ItemCoupon item);
    }
}
