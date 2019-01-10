package com.example.konta.sketch_loyalityapp.adapters;

import com.example.konta.sketch_loyalityapp.adapterModel.ItemCoupon;
import com.example.konta.sketch_loyalityapp.adapterModel.ItemHome;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;

public interface RecyclerItemClickListener {

    interface HomeRetrofitClickListener {
        void onItemHomeClick(MenuComponent item);
    }

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

    interface CouponRetrofitClickListener {
        void onItemCouponDetailsClick(int couponId);
        void onItemCouponCodeCheckClick(Coupon coupon);
    }
}
