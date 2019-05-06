package com.sellger.konta.sketch_loyaltyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "coupon_table")
public class Coupon {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @ColumnInfo(name = "id")
    private Integer mId;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String mTitle;

    @SerializedName("image")
    @Expose
    @ColumnInfo(name = "image")
    private String mImage;

    @SerializedName("coupon_code")
    @Expose
    @ColumnInfo(name = "coupon_code")
    private String mCouponCode;

    @SerializedName("short_description")
    @Expose
    @ColumnInfo(name = "short_description")
    private String mShortDescription;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    private String mDescription;

    @SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    private Float mPrice;

    @SerializedName("price_after")
    @Expose
    @ColumnInfo(name = "price_after")
    private Float mPriceAfter;

    @SerializedName("reduction_amount")
    @Expose
    @ColumnInfo(name = "reduction_amount")
    private String mReductionAmount;

    @SerializedName("reduction_type")
    @Expose
    @ColumnInfo(name = "reduction_type")
    private String mReductionType;

    @SerializedName("fresh_time")
    @Expose
    @ColumnInfo(name = "fresh_time")
    private String mFreshTime;

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

    public String getImage() {
        return mImage;
    }
    public void setImage(String image) {
        mImage = image;
    }

    public String getCouponCode() {
        return mCouponCode;
    }
    public void setCouponCode(String couponCode) {
        mCouponCode = couponCode;
    }

    public String getShortDescription() {
        return mShortDescription;
    }
    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getDescription() {
        return mDescription;
    }
    public void setDescription(String description) {
        mDescription = description;
    }

    public Float getPrice() {
        return mPrice;
    }
    public void setPrice(Float price) {
        mPrice = price;
    }

    public Float getPriceAfter() {
        return mPriceAfter;
    }
    public void setPriceAfter(Float priceAfter) {
        mPriceAfter = priceAfter;
    }

    public String getReductionAmount() {
        return mReductionAmount;
    }
    public void setReductionAmount(String reductionAmount) {
        mReductionAmount = reductionAmount;
    }

    public String getReductionType() {
        return mReductionType;
    }
    public void setReductionType(String reductionType) {
        mReductionType = reductionType;
    }

    public String getFreshTime() {
        return mFreshTime;
    }
    public void setFreshTime(String freshTime) {
        mFreshTime = freshTime;
    }
}