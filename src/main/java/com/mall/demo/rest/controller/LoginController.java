package com.mall.demo.rest.controller;

import com.mall.demo.model.JsonResult;
import com.mall.demo.model.ResponseBodyData;
import com.mall.demo.model.UserInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @ApiOperation(value="获取登录令牌", notes="根据用户名密码获取令牌")
    @ApiImplicitParam
    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> ajaxLogin(UserInfo userInfo) {
        JsonResult r = new JsonResult();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getUsername(), userInfo.getPassword());
        ResponseBodyData responseBodyData = new ResponseBodyData();
        Map<String, Object> map = new HashMap<>();
        responseBodyData.setData(map);
        r.setStatus(String.valueOf(HttpStatus.OK.value()));
        try {
            subject.login(token);
            map.put("token", token);
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
}
