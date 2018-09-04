package com.mall.demo.rest.controller;

import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;
import com.alibaba.fastjson.support.spring.annotation.FastJsonView;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.privilege.User;
import com.mall.demo.common.result.Result;
import com.mall.demo.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Api(value = "用户", description = "用户")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value="获取所有用户", notes="获取所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = User.class, props = {"roleList"}),
                    @FastJsonFilter(clazz = User.class, props = {"password"})},
            include = {
                    @FastJsonFilter(clazz = User.class, props = {"nickname","account"})}
            )
    @GetMapping
    @LogAnnotation(module = "用户", operation = "获取所有用户")
    @RequiresRoles(Base.ROLE_ADMIN)
    public Result listUsers() {
        Iterable<User> users = userInfoService.findAll();
        return Result.success(users);
    }

    @ApiOperation(value="获取一个用户", notes="根据id获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}")
    @LogAnnotation(module = "用户", operation = "根据id获取用户")
    @FastJsonView(
            exclude = {
                    @FastJsonFilter(clazz = User.class, props = {"roleList"}),
                    @FastJsonFilter(clazz = User.class, props = {"password"})})
    @RequiresRoles(Base.ROLE_ADMIN)
    public Result getUserById(@PathVariable("id") Long id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        Optional<User> user = userInfoService.getUserById(id);

        User userInfo = user.get();
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(userInfo);
        return r;
    }

    @ApiOperation(value="获取当前用户", notes="根据token获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/currentUser")
    @FastJsonView(
            include = {@FastJsonFilter(clazz = User.class, props = {"id", "account", "nickname", "avatar"})})
    @LogAnnotation(module = "用户", operation = "获取当前登录用户")
    public Result getCurrentUser(HttpServletRequest request) {

        Result r = new Result();
        User currentUser = UserUtils.getCurrentUser();
        r.setResultCode(ResultCode.SUCCESS);
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(currentUser);
        return r;
    }

    @ApiIgnore
    @PostMapping("/create")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "用户", operation = "添加用户")
    public Result saveUser(@Validated @RequestBody User user) {
        Long userId = userInfoService.saveUser(user);
        Result r = Result.success();
        r.simple().put("userId", userId);
        return r;
    }

    @ApiIgnore
    @PostMapping("/update")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "用户", operation = "修改用户")
    public Result updateUser(@RequestBody User user) {
        Result r = new Result();
        if (null == user.getId()) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return r;
        }
        Long userId = userInfoService.updateUser(user);
        r.setResultCode(ResultCode.SUCCESS);
        r.simple().put("userId", userId);
        return r;
    }

    @ApiIgnore
    @GetMapping("/delete/{id}")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "用户", operation = "删除用户")
    public Result deleteUserById(@PathVariable("id") Long id) {
        Result r = new Result();
        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }
        userInfoService.deleteUserById(id);
        r.setResultCode(ResultCode.SUCCESS);
        return r;
    }

}
