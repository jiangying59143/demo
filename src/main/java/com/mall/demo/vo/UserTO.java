package com.mall.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="user对象",description="用户对象user")
public class UserTO {

    @ApiModelProperty(value="用户登录账号",name="account",example="zhangsan")
    private String account;

    @ApiModelProperty(value="用户密码",name="password",example="123456")
    private String password;

    @ApiModelProperty(value="用户名",name="nickname",example="张三")
    private String nickname;

    @ApiModelProperty(value="用户邮箱",name="email",example="123456@test.com")
    private String email;

    @ApiModelProperty(value="用户电话",name="phone",example="13812312312")
    private String phone;

    @ApiModelProperty(value="验证码(邮箱和电话注册必填)",name="verificationCode",example="1234")
    private String verificationCode;

    @ApiModelProperty(value="emailCodeKey(邮箱注册必填)",name="key",example="sjfslf123445DASFASDF")
    private String emailCodeKey;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEmailCodeKey() {
        return emailCodeKey;
    }

    public void setEmailCodeKey(String emailCodeKey) {
        this.emailCodeKey = emailCodeKey;
    }
}
