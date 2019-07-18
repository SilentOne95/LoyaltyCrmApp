package com.ecommercelab.loyaltyapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "hour_table")
public class OpenHour {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("day_name")
    @Expose
    @ColumnInfo(name = "day_name")
    private String mDayName;

    @SerializedName("open_hour")
    @Expose
    @ColumnInfo(name = "open_hour")
    private String mOpenHour;

    @SerializedName("open_minute")
    @Expose
    @ColumnInfo(name = "open_minute")
    private String mOpenMinute;

    @SerializedName("close_hour")
    @Expose
    @ColumnInfo(name = "close_hour")
    private String mCloseHour;

    @SerializedName("close_minute")
    @Expose
    @ColumnInfo(name = "close_minute")
    private String mCloseMinute;

    public Integer getId() {
        return mId;
    }
    public void setId(Integer id) {
        mId = id;
    }

    public String getDayName() {
        return mDayName;
    }
    public void setDayName(String dayName) {
        mDayName = dayName;
    }

    public String getOpenHour() {
        return mOpenHour;
    }
    public void setOpenHour(String openHour) {
        mOpenHour = openHour;
    }

    public String getOpenMinute() {
        return mOpenMinute;
    }
    public void setOpenMinute(String openMinute) {
        mOpenMinute = openMinute;
    }

    public String getCloseHour() {
        return mCloseHour;
    }
    public void setCloseHour(String closeHour) {
        mCloseHour = closeHour;
    }

    public String getCloseMinute() {
        return mCloseMinute;
    }
    public void setCloseMinute(String closeMinute) {
        mCloseMinute = closeMinute;
    }
}
