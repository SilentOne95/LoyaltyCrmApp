package com.jemsushi.loyaltyapp.adapter;

import com.jemsushi.loyaltyapp.data.entity.MenuComponent;

public interface RecyclerItemClickListener {

    interface HomeRetrofitClickListener {
        void onItemHomeClick(MenuComponent item, int selectedViewId);
    }

    interface ProductRetrofitClickListener {
        void onItemProductClick(int productId);
    }

    interface CouponRetrofitClickListener {
        void onItemCouponDetailsClick(int couponId);
        void onItemCouponCodeCheckClick(int position, String imageUrl);
    }
}
