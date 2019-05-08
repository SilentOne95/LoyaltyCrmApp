package com.sellger.konta.sketch_loyaltyapp.data.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.sellger.konta.sketch_loyaltyapp.data.entity.OpenHour;

import java.util.List;

public class HelperMarker implements ClusterItem {

    @SerializedName("id")
    @Expose
    private Integer mId;

    @SerializedName("title")
    @Expose
    private String mTitle;

    @SerializedName("lat")
    @Expose
    private Double mLat;

    @SerializedName("lng")
    @Expose
    private Double mLng;

    @SerializedName("shop_name")
    @Expose
    private String mShopName;

    @SerializedName("address")
    @Expose
    private String mAddress;

    @SerializedName("post_code")
    @Expose
    private String mPostalCode;

    @SerializedName("city")
    @Expose
    private String mCity;

    @SerializedName("mail")
    @Expose
    private String mMail;

    @SerializedName("phone_number")
    @Expose
    private String mPhoneNumber;

    @SerializedName("website")
    @Expose
    private String mWebsite;

    @SerializedName("open_hours")
    @Expose
    private List<OpenHour> mOpenHours = null;

    public HelperMarker(double lat, double lng, int id) {
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

    public List<OpenHour> getOpenHours() {
        return mOpenHours;
    }
    public void setOpenHours(List<OpenHour> openHours) {
        mOpenHours = openHours;
    }

    @Override
    public LatLng getPosition() { return new LatLng(mLat, mLng); }

    @Override
    public String getSnippet() { return null; }
}
