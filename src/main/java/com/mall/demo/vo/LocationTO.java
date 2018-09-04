package com.mall.demo.vo;

import com.mall.demo.model.blog.Location;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="地址对象",description="地址对象")
public class LocationTO {

    @ApiModelProperty(value="位置描述",name="Location", example = "苏州")
    private String location;

    @ApiModelProperty(value="纬度",name="latitude", example = "11.11")
    private Double latitude; //纬度 南北

    @ApiModelProperty(value="经度",name="longitude", example = "22.22")
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

    public Location achieveLocation(){
        Location location = new Location();
        location.setLatitude(this.latitude);
        location.setLongitude(this.longitude);
        location.setLocation(this.location);
        return location;
    }
}
