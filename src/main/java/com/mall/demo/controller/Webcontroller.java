package com.mall.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Webcontroller {

    @RequestMapping("/")
    public String test(Model model){
        return "test";
    }
}
