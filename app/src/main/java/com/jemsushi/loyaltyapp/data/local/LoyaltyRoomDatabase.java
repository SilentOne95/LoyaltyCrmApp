package com.jemsushi.loyaltyapp.data.local;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.jemsushi.loyaltyapp.data.entity.Coupon;
import com.jemsushi.loyaltyapp.data.entity.Marker;
import com.jemsushi.loyaltyapp.data.entity.MenuComponent;
import com.jemsushi.loyaltyapp.data.entity.OpenHour;
import com.jemsushi.loyaltyapp.data.entity.Page;
import com.jemsushi.loyaltyapp.data.entity.Product;
import com.jemsushi.loyaltyapp.data.local.dao.CouponDao;
import com.jemsushi.loyaltyapp.data.local.dao.MarkerDao;
import com.jemsushi.loyaltyapp.data.local.dao.MenuDao;
import com.jemsushi.loyaltyapp.data.local.dao.OpenHourDao;
import com.jemsushi.loyaltyapp.data.local.dao.PageDao;
import com.jemsushi.loyaltyapp.data.local.dao.ProductDao;
import com.jemsushi.loyaltyapp.data.utils.OpenHourTypeConverter;

@Database(entities = {MenuComponent.class, Product.class, Coupon.class, Marker.class, OpenHour.class,
        Page.class}, version = 1)
@TypeConverters({OpenHourTypeConverter.class})
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
