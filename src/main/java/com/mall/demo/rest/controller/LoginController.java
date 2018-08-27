package com.mall.demo.rest.controller;

import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.configure.MySessionManager;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.UserInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserInfoService userInfoService;

//    @ApiOperation(value="获取登录令牌", notes="根据用户名密码获取令牌")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
//    })
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result ajaxLogin(@RequestParam String loginName, @RequestParam String password) {
        Result r = new Result();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        Map<String, Object> map = new HashMap<>();
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(map);
        try {
            subject.login(token);
            map.put("token", subject.getSession().getId());
            SecurityUtils.getSubject().getSession().setAttribute(Base.CURRENT_USER, subject.getPrincipal());
        }catch (UnknownAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
        } catch (LockedAccountException e) {
            r.setResultCode(ResultCode.USER_ACCOUNT_FORBIDDEN);
        } catch (AuthenticationException e) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
        } catch (Exception e) {
            r.setResultCode(ResultCode.ERROR);
        }
        return r;
    }

    @PostMapping("/login")
    @LogAnnotation(module = "登录", operation = "登录")
    public Result login(@RequestBody User user) {
        Result r = new Result();
        executeLogin(user.getAccount(), user.getPassword(), r);
        return r;
    }

    private void executeLogin(String account, String password, Result r) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

        try {
            subject.login(token);

            User currentUser = userInfoService.findByAccount(account);
            subject.getSession().setAttribute(Base.CURRENT_USER, currentUser);

            r.setResultCode(ResultCode.SUCCESS);
            r.simple().put(MySessionManager.AUTHORIZATION, subject.getSession().getId());
        } catch (UnknownAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
        } catch (LockedAccountException e) {
            r.setResultCode(ResultCode.USER_ACCOUNT_FORBIDDEN);
        } catch (AuthenticationException e) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
        } catch (Exception e) {
            r.setResultCode(ResultCode.ERROR);
        }

    }

}
