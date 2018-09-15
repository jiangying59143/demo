package com.mall.demo.controller;

import com.mall.demo.common.utils.EncryptUtil;
import com.mall.demo.common.utils.StringUtils;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.UserInfoService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.util.Date;

import static jdk.nashorn.internal.runtime.GlobalFunctions.decodeURIComponent;

@Controller
@RequestMapping("/email")
public class EmailActiveUserController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/verify")
    public String verify(@RequestParam String emailTime){
        if(!StringUtils.isEmpty(emailTime)) {
            System.out.println(emailTime);
            emailTime = decodeURIComponent(null, emailTime).toString();
            System.out.println(emailTime);
            System.out.println(EncryptUtil.decodeDES(emailTime));
            emailTime = EncryptUtil.decodeDES(emailTime);
            if (emailTime.contains("===")) {
                String[] ss = emailTime.split("===");
                User user = userInfoService.findByEmail(ss[0]);
                if(user != null){
                    try{
                        Date date = DateUtils.parseDate(ss[1], com.mall.demo.common.utils.DateUtils.DATE_TIME_TO_SECOND);
                        if(DateUtils.addMinutes(date, 5).after(new Date())){
                            user.setState(User.USER_STATE_COMMON);
                            userInfoService.updateUser(user);
                            return "emailSuccessTemplate";
                        }
                    }catch (Exception e){

                    }
                }
            }
        }
        return "emailFailTemplate";
    }


}
