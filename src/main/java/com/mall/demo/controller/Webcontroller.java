package com.mall.demo.controller;

import com.mall.demo.dao.UserRepository;
import com.mall.demo.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 1) 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面，或者html，
 * 配置的视图解析器 InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容。
 *
 * 2) 如果需要返回到指定页面，则需要用 @Controller配合视图解析器InternalResourceViewResolver才行。
 *     如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解。
 */
@Controller
public class Webcontroller {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String test(Model model){
        model.addAttribute("user", new User());
        return "test";
    }

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public User createUser(User user){
        return userRepository.save(user);
    }

    @RequestMapping("/exception")
    public void exeption(Model model) throws Exception {
        throw new Exception("xxx");
    }

}
