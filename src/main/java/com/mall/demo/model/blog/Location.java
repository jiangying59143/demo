package com.mall.demo.model.blog;

import com.mall.demo.model.base.BaseTO;

import javax.persistence.Entity;

@Entity
public class Location extends BaseTO {

    private String Location;

    private Long latitude; //纬度 南北

    private Long longitude; //经度 东西

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }
}
