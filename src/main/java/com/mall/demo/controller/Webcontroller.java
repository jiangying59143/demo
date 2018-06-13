package com.mall.demo.controller;

import com.mall.demo.dao.UserRepository;
import com.mall.demo.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class Webcontroller {

    @Autowired
    private UserRepository userRepository;

    @ApiIgnore
    @RequestMapping("/")
    public String test(Model model){
        return "test";
    }

}
