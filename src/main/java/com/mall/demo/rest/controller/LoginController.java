package com.mall.demo.rest.controller;

import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.constants.Base;
import com.mall.demo.constants.ResultCode;
import com.mall.demo.model.JsonResult;
import com.mall.demo.model.ResponseBodyData;
import com.mall.demo.model.privilege.UserInfo;
import com.mall.demo.model.rest.Result;
import com.mall.demo.service.UserInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value="获取登录令牌", notes="根据用户名密码获取令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> ajaxLogin(@RequestParam String loginName, @RequestParam String password) {
        JsonResult r = new JsonResult();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        ResponseBodyData responseBodyData = new ResponseBodyData();
        Map<String, Object> map = new HashMap<>();
        r.setStatus(String.valueOf(HttpStatus.OK.value()));
        responseBodyData.setData(map);
        r.setResult(responseBodyData);
        try {
            subject.login(token);
            map.put("token", subject.getSession().getId());
            responseBodyData.setMessage("登录成功");
        } catch (IncorrectCredentialsException e) {
            responseBodyData.setMessage("密码错误");
        } catch (LockedAccountException e) {
            responseBodyData.setMessage("登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            responseBodyData.setMessage("该用户不存在");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            r.setStatus(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="获取一个用户", notes="根据id获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "令牌", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}")
    @LogAnnotation(module = "用户", operation = "根据id获取用户")
    @RequiresRoles(Base.ROLE_ADMIN)
    @RequiresPermissions("userInfo:get")//权限管理;
    public ResponseEntity<JsonResult>  getUserById(@PathVariable("id") Long id) {

        JsonResult r = new JsonResult();
        if (null == id) {
            r.setStatus("10002");
            return ResponseEntity.ok(r);
        }

        Optional<UserInfo> user = userInfoService.getUserById(id);

        UserInfo userInfo = user.get();
        r.setResult(userInfo.getName());
        r.setStatus("200");
        return ResponseEntity.ok(r);
    }
}
