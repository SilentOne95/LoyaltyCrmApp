package com.ecommercelab.loyaltyapp.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.ecommercelab.loyaltyapp.data.utils.OpenHourTypeConverter;

import java.util.List;

@Entity(tableName = "marker_table")
public class Marker implements ClusterItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int _id;

    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("lat")
    @Expose
    @ColumnInfo(name = "lat")
    private Double mLat;

    @SerializedName("lng")
    @Expose
    @ColumnInfo(name = "lng")
    private Double mLng;

    @SerializedName("shop_name")
    @Expose
    @ColumnInfo(name = "shop_name")
    private String mShopName;

    @SerializedName("address")
    @Expose
    @ColumnInfo(name = "address")
    private String mAddress;

    @SerializedName("post_code")
    @Expose
    @ColumnInfo(name = "postal_code")
    private String mPostalCode;

    @SerializedName("city")
    @Expose
    @ColumnInfo(name = "city")
    private String mCity;

    @SerializedName("mail")
    @Expose
    @ColumnInfo(name = "mail")
    private String mMail;

    @SerializedName("phone_number")
    @Expose
    @ColumnInfo(name = "phone_number")
    private String mPhoneNumber;

    @SerializedName("website")
    @Expose
    @ColumnInfo(name = "website")
    private String mWebsite;

    @SerializedName("open_hours")
    @Expose
    @ColumnInfo(name = "open_hour_list")
    @TypeConverters(OpenHourTypeConverter.class)
    private List<OpenHour> mOpenHourList = null;

    public Integer getId() {
        return mId;
    }
    public void setId(Integer id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public Double getLat() {
        return mLat;
    }
    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }
    public void setLng(Double lng) {
        mLng = lng;
    }

    public String getShopName() {
        return mShopName;
    }
    public void setShopName(String shopName) {
        mShopName = shopName;
    }

    public String getAddress() {
        return mAddress;
    }
    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPostalCode() {
        return mPostalCode;
    }
    public void setPostalCode(String postalCode) {
        mPostalCode = postalCode;
    }

    public String getCity() {
        return mCity;
    }
    public void setCity(String city) {
        mCity = city;
    }

    public String getMail() {
        return mMail;
    }
    public void setMail(String mail) {
        mMail = mail;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return mWebsite;
    }
    public void setWebsite(String website) {
        mWebsite = website;
    }

    public List<OpenHour> getOpenHourList() {
        return mOpenHourList;
    }
    public void setOpenHourList(List<OpenHour> openHourList) {
        mOpenHourList = openHourList;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(mLat, mLng);
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
