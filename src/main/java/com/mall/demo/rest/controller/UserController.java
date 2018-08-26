package com.mall.demo.rest.controller;

import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.constants.Base;
import com.mall.demo.constants.ResultCode;
import com.mall.demo.model.privilege.UserInfo;
import com.mall.demo.model.rest.Result;
import com.mall.demo.service.UserInfoService;
import com.mall.demo.util.UserUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * 用户api
 *
 * @author shimh
 * <p>
 * 2018年1月23日
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping
    @LogAnnotation(module = "用户", operation = "获取所有用户")
    @RequiresRoles(Base.ROLE_ADMIN)
    public Result listUsers() {
        Iterable<UserInfo> users = userInfoService.findAll();
        return Result.success(users);
    }

    @ApiOperation(value="获取一个用户", notes="根据id获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}")
//    @LogAnnotation(module = "用户", operation = "根据id获取用户")
//    @RequiresRoles(Base.ROLE_ADMIN)
//    @RequiresPermissions("userInfo:get")//权限管理;
    public Result getUserById(@PathVariable("id") Long id) {

        Result r = new Result();

        if (null == id) {
            r.setResultCode(ResultCode.PARAM_IS_BLANK);
            return r;
        }

        Optional<UserInfo> user = userInfoService.getUserById(id);

        UserInfo userInfo = user.get();
        r.setResultCode(ResultCode.SUCCESS);
        r.setData(userInfo);
        return r;
    }

    @GetMapping("/currentUser")
//    @FastJsonView(
//            include = {@FastJsonFilter(clazz = User.class, props = {"id", "account", "nickname", "avatar"})})
    @LogAnnotation(module = "用户", operation = "获取当前登录用户")
    public Result getCurrentUser(HttpServletRequest request) {

        Result r = new Result();

        UserInfo currentUser = UserUtils.getCurrentUser();

        r.setResultCode(ResultCode.SUCCESS);
        r.setData(currentUser);
        return r;
    }

    @PostMapping("/create")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "用户", operation = "添加用户")
    public Result saveUser(@Validated @RequestBody UserInfo user) {

        Long userId = userInfoService.saveUser(user);

        Result r = Result.success();
        r.simple().put("userId", userId);
        return r;
    }

    @PostMapping("/update")
    @RequiresRoles(Base.ROLE_ADMIN)
    @LogAnnotation(module = "用户", operation = "修改用户")
    public Result updateUser(@RequestBody UserInfo user) {
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
