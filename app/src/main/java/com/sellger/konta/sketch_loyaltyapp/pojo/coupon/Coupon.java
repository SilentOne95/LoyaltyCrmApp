package com.sellger.konta.sketch_loyaltyapp.pojo.coupon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon {

    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("fresh_time")
    @Expose
    private String freshTime;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("price_after")
    @Expose
    private Float priceAfter;
    @SerializedName("reduction_amount")
    @Expose
    private String reductionAmount;
    @SerializedName("reduction_type")
    @Expose
    private String reductionType;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("title")
    @Expose
    private String title;

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFreshTime() {
        return freshTime;
    }

    public void setFreshTime(String freshTime) {
        this.freshTime = freshTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPriceAfter() {
        return priceAfter;
    }

    public void setPriceAfter(Float priceAfter) {
        this.priceAfter = priceAfter;
    }

    public String getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(String reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public String getReductionType() {
        return reductionType;
    }

    public void setReductionType(String reductionType) {
        this.reductionType = reductionType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}