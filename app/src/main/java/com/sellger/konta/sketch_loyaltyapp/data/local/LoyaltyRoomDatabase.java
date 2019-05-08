package com.sellger.konta.sketch_loyaltyapp.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Marker;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Page;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.CouponDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MarkerDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.MenuDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.OpenHourDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.PageDao;
import com.sellger.konta.sketch_loyaltyapp.data.local.dao.ProductDao;

@Database(entities = {MenuComponent.class, Product.class, Coupon.class, Marker.class, OpenHour.class,
        Page.class}, version = 1)
public abstract class LoyaltyRoomDatabase extends RoomDatabase {

    public abstract MenuDao menuDao();
    public abstract ProductDao productDao();
    public abstract CouponDao couponDao();
    public abstract MarkerDao markerDao();
    public abstract OpenHourDao openHourDao();
    public abstract PageDao pageDao();

    private static volatile LoyaltyRoomDatabase INSTANCE;

    public static LoyaltyRoomDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LoyaltyRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LoyaltyRoomDatabase.class, "loyalty_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
