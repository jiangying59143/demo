package com.mall.demo.rest.controller;

import com.alibaba.fastjson.JSON;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.configure.MySessionManager;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.UserInfoService;
import com.mall.demo.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@Api(value = "用户登录注册", description = "用户登录注册")
@RestController
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value="获取登录令牌", notes="根据用户名密码获取令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "登录名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 401, message = "20002:账号或密码错误", response=Result.class),
            @ApiResponse(code = 403, message = "20003:账号已被禁用", response=Result.class),
            @ApiResponse(code = 404, message = "20004:用户不存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/login")
    @LogAnnotation(module = "登录", operation = "用户登录")
    public ResponseEntity<Result> ajaxLogin(@RequestParam String account, @RequestParam String password) {
        Result r = new Result();
        return executeLogin(account, password, r);
    }

    @ApiOperation(value="用户注册", notes="用户注册并返回令牌")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 403, message = "20005:用户已存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/register")
    @LogAnnotation(module = "注册", operation = "用户注册")
    public ResponseEntity<Result> register(@RequestBody UserVO user) {
        Result r = new Result();
        User temp = userInfoService.findByAccount(user.getAccount());
        if (null != temp) {
            r.setResultCode(ResultCode.USER_HAS_EXISTED);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }
        String account = user.getAccount();
        String password = user.getPassword();
        User user1 = new User();
        user1.setAccount(account);
        user1.setPassword(password);
        user1.setNickname(user.getNickname());

        Long userId = userInfoService.saveUser(user1);

        if (userId > 0) {
            executeLogin(account, password, r);
        } else {
            r.setResultCode(ResultCode.USER_Register_ERROR);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="用户退出系统", notes="用户登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "20001:用户未登录"),
            @ApiResponse(code = 201, message = "1:失败")})
    @GetMapping("/logout")
    @LogAnnotation(module = "退出", operation = "退出")
    public ResponseEntity<Result> logout() {

        Result r = new Result();
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }catch (Exception e){
            r.setResultCode(ResultCode.USER_Register_ERROR);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }

        r.setResultCode(ResultCode.SUCCESS);
        return ResponseEntity.ok(r);
    }

//    @PostMapping("/login")
//    @LogAnnotation(module = "登录", operation = "登录")
    public Result login(@RequestBody UserVO user) {
        Result r = new Result();
        executeLogin(user.getAccount(), user.getPassword(), r);
        return r;
    }

    @ApiIgnore
    @GetMapping(value = "/unauth")
    public Result unauth() {
        Result r = new Result();
        r.setResultCode(ResultCode.USER_NOT_LOGGED_IN);
        return r;
    }

    @ApiIgnore
    @GetMapping(value = "forbidden")
    public Result forbidden() {
        Result r = new Result();
        r.setResultCode(ResultCode.USER_NO_PRIVI);
        return r;
    }

    private ResponseEntity<Result> executeLogin(String account, String password, Result r) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        r.setResultCode(ResultCode.SUCCESS);
        try {
            subject.login(token);
            r.simple().put(MySessionManager.AUTHORIZATION, subject.getSession().getId());
            SecurityUtils.getSubject().getSession().setAttribute(Base.CURRENT_USER, subject.getPrincipal());
        }catch (UnknownAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
        } catch (LockedAccountException e) {
            r.setResultCode(ResultCode.USER_ACCOUNT_FORBIDDEN);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(r);
        } catch (AuthenticationException e) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
        } catch (Exception e) {
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }
        return ResponseEntity.ok(r);

    }

}
