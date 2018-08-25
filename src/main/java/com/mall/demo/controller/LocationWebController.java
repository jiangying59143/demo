package com.mall.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LocationWebController {

    @RequestMapping("/map")
    public String initMap(){
        return "initMap";
    }

    @RequestMapping("/location")
    public String initLocation(){
        return "location";
    }

    @RequestMapping("/plocation")
    public String plocation(){
        return "preciseLocation";
    }

}
