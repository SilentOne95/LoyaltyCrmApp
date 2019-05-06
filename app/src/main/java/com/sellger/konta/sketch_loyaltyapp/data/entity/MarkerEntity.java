package com.sellger.konta.sketch_loyaltyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.sellger.konta.sketch_loyaltyapp.pojo.map.OpenHour;

import java.util.List;

@Entity(tableName = "marker_table")
public class MarkerEntity implements ClusterItem {

    @PrimaryKey
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
    @ColumnInfo(name = "open_hours")
    private List<OpenHour> mOpenHours = null;

    public MarkerEntity(double lat, double lng, int id) {
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

    public String getPostCode() {
        return mPostalCode;
    }
    public void setPostCode(String postalCode) {
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
