package com.example.konta.sketch_loyalityapp.data.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thursday {

    @SerializedName("open_hour")
    @Expose
    private String openHour;
    @SerializedName("open_minute")
    @Expose
    private String openMinute;
    @SerializedName("close_hour")
    @Expose
    private String closeHour;
    @SerializedName("close_minute")
    @Expose
    private String closeMinute;

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getOpenMinute() {
        return openMinute;
    }

    public void setOpenMinute(String openMinute) {
        this.openMinute = openMinute;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getCloseMinute() {
        return closeMinute;
    }

    public void setCloseMinute(String closeMinute) {
        this.closeMinute = closeMinute;
    }

}
