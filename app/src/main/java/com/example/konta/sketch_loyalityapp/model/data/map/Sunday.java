package com.example.konta.sketch_loyalityapp.model.data.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sunday {

    @SerializedName("open_hour")
    @Expose
    private Integer openHour;
    @SerializedName("open_minute")
    @Expose
    private Integer openMinute;
    @SerializedName("close_hour")
    @Expose
    private Integer closeHour;
    @SerializedName("close_minute")
    @Expose
    private Integer closeMinute;

    public Integer getOpenHour() {
        return openHour;
    }

    public void setOpenHour(Integer openHour) {
        this.openHour = openHour;
    }

    public Integer getOpenMinute() {
        return openMinute;
    }

    public void setOpenMinute(Integer openMinute) {
        this.openMinute = openMinute;
    }

    public Integer getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(Integer closeHour) {
        this.closeHour = closeHour;
    }

    public Integer getCloseMinute() {
        return closeMinute;
    }

    public void setCloseMinute(Integer closeMinute) {
        this.closeMinute = closeMinute;
    }

}
