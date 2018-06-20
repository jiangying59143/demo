package com.mall.demo.controller;

import com.mall.demo.dao.UserRepository;
import com.mall.demo.model.JsonResult;
import com.mall.demo.model.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class RestfulController {

    @Autowired
    private UserRepository userRepository;

    @ApiIgnore//使用该注解忽略这个API
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
//    @Cacheable(value = "zh",key = "'user_' + #id")
    public ResponseEntity<JsonResult> getUser(@PathVariable Long id) {
        JsonResult r = new JsonResult();
        try {
            Optional<User> user=userRepository.findById(id);
            r.setResult(user);
            r.setStatus("ok");
        } catch (Exception e) {
            r.setResult(e.getClass().getName() + ":" + e.getMessage());
            r.setStatus("error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public User createUser(User user){
        return userRepository.save(user);
    }

    @ApiIgnore//使用该注解忽略这个API swagger
    @RequestMapping("/getUsers")
    @Cacheable(value = "users",keyGenerator = "keyGenerator") //redis注解
    public List<User> getUsers() {
        List<User> users=userRepository.findAll();
        System.out.println("若下面没出现“无缓存的时候调用”字样且能打印出数据表示测试成功");
        return users;
    }

    @ApiIgnore//使用该注解忽略这个API
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

