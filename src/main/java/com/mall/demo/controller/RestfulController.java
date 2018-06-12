package com.mall.demo.controller;

import com.mall.demo.dao.UserRepository;
import com.mall.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Component
public class RestfulController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    @RequestMapping("/user/{id}")
    @Cacheable(value = "zh",key = "'user_' + #id")
    public Optional<User> getUser(@PathVariable Long id) {
        Optional<User> user=userRepository.findById(id);
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return user;
    }

    @RequestMapping("/getUsers")
    @Cacheable(value = "users",keyGenerator = "keyGenerator")
    public List<User> getUsers() {
        List<User> users=userRepository.findAll();
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return users;
    }

    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }

}

