package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;

import javax.persistence.Entity;

@Entity
public class Location extends BaseTO {

    private String location;

    private Double latitude; //纬度 南北

    private Double longitude; //经度 东西

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
