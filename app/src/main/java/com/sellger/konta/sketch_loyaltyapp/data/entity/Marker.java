package com.sellger.konta.sketch_loyaltyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "marker_table")
public class Marker {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "lat")
    private Double mLat;

    @ColumnInfo(name = "lng")
    private Double mLng;

    @ColumnInfo(name = "shop_name")
    private String mShopName;

    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "postal_code")
    private String mPostalCode;

    @ColumnInfo(name = "city")
    private String mCity;

    @ColumnInfo(name = "mail")
    private String mMail;

    @ColumnInfo(name = "phone_number")
    private String mPhoneNumber;

    @ColumnInfo(name = "website")
    private String mWebsite;

    public Marker(double lat, double lng, int id) {
        mLat = lat;
        mLng = lng;
        mId = id;
    }

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
}
