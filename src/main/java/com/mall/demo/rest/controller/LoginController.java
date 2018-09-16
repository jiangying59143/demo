package com.mall.demo.rest.controller;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.FlatColorBackgroundProducer;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.backgrounds.SquigglesBackgroundProducer;
import cn.apiclub.captcha.backgrounds.TransparentBackgroundProducer;
import cn.apiclub.captcha.gimpy.FishEyeGimpyRenderer;
import com.mall.demo.common.annotation.LogAnnotation;
import com.mall.demo.common.constants.Base;
import com.mall.demo.common.constants.Note;
import com.mall.demo.common.constants.ResultCode;
import com.mall.demo.common.result.Result;
import com.mall.demo.common.utils.*;
import com.mall.demo.configure.MySessionManager;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.MailService;
import com.mall.demo.service.UserInfoService;
import com.mall.demo.vo.UserTO;
import io.swagger.annotations.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.runtime.GlobalFunctions.encodeURIComponent;

@Api(value = "用户登录注册", description = "用户登录注册")
@RestController
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MailService mailService;

    @Autowired
    private HttpServletRequest request;

    @Value("${me.upload.path}")
    private String baseFolderPath;

    @Value("${phone.flag}")
    private boolean phoneFlag;

    private int smsVerificationCodeExpireTime=120;

    private int emailVerificationCodeExpireTime=5*60;

    @ApiOperation(value="发送短信验证码", notes="发送短信验证码(大概还剩下90多条，省着点用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "电话号码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/getSMSVerification")
    @LogAnnotation(module = "发送短信验证码", operation = "发送短信验证码")
    public Result getSMSVerification(@RequestParam String phoneNumber) {
        String code = StringUtils.getRandomNumCode(4);
        Result r = Result.success();
        if(phoneFlag) {
            r = SmsUtils.sendSMS(phoneNumber, code);
        }
        Map map = r.simple();
        map.put("code", code);
        map.put("expire", smsVerificationCodeExpireTime);
        if(r.getCode()==0){
            redisTemplate.opsForValue().set("getSMSVerification" + phoneNumber, code, smsVerificationCodeExpireTime, TimeUnit.SECONDS);
        }
        return r;
//        redisTemplate.opsForValue().set("getSMSVerification" + phoneNumber, code, 500, TimeUnit.SECONDS);
//        return Result.success(code);
    }

    @ApiOperation(value="发送邮箱图片验证码", notes="发送邮箱图片验证码")
    @GetMapping("/getImageVerification")
    @LogAnnotation(module = "发送邮箱图片验证码", operation = "发送邮箱图片验证码")
    public Result getImageVerification() {
        String uuid = UUID.randomUUID().toString();
        Captcha captcha = new Captcha.Builder(200, 60)
                .addText().addBackground(new FlatColorBackgroundProducer())
                .gimp(new FishEyeGimpyRenderer())
                .build();
        redisTemplate.opsForValue().set(uuid, captcha.getAnswer(), emailVerificationCodeExpireTime, TimeUnit.SECONDS);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            ImageIO.write(captcha.getImage(), "png", bao);
            Result r = Result.success();
            Map map = r.simple();
            map.put("key", uuid);
            map.put("code", captcha.getAnswer());
            map.put("expire", emailVerificationCodeExpireTime);
            byte[] b = bao.toByteArray();
            map.put("imageVerification", b);
//            FileUtils.uploadVerificationCodeImage(b, baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, uuid + ".png");
//            map.put("imageUrl", HttpUtils.getSystemUrl(request, FileUtils.UPLOAD_FILE_IMAGE, null, uuid+ ".png"));
            return r;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(ResultCode.ERROR);
        }
    }

    @ApiOperation(value="根据用户名密码获取登录令牌", notes="根据用户名密码获取令牌")
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
    @LogAnnotation(module = "登录", operation = "账户登录")
    public ResponseEntity<Result> acctLogin(@RequestParam String account, @RequestParam String password) {
        Result r = new Result();
        return executeLogin("C", account, password, r);
    }

    @ApiOperation(value="根据邮箱获取登录令牌", notes="根据邮箱密码获取令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 401, message = "20002:账号或密码错误", response=Result.class),
            @ApiResponse(code = 403, message = "20003:账号已被禁用", response=Result.class),
            @ApiResponse(code = 404, message = "20004:用户不存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/emailLogin")
    @LogAnnotation(module = "登录", operation = "邮箱登录")
    public ResponseEntity<Result> emailLogin(@RequestParam String email, @RequestParam String password) {
        Result r = new Result();
        return executeLogin("E", email, password, r);
    }

    @ApiOperation(value="根据手机获取登录令牌", notes="根据手机密码获取令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 401, message = "20002:账号或密码错误", response=Result.class),
            @ApiResponse(code = 403, message = "20003:账号已被禁用", response=Result.class),
            @ApiResponse(code = 404, message = "20004:用户不存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/phoneLogin")
    @LogAnnotation(module = "登录", operation = "手机登录")
    public ResponseEntity<Result> phoneLogin(@RequestParam String phone, @RequestParam String password) {
        Result r = new Result();
        return executeLogin("P", phone, password, r);
    }

    @ApiOperation(value="根据手机验证码登录", notes="根据手机验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 401, message = "20002:账号或密码错误", response=Result.class),
            @ApiResponse(code = 403, message = "20003:账号已被禁用", response=Result.class),
            @ApiResponse(code = 404, message = "20004:用户不存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/phoneCodeLogin")
    @LogAnnotation(module = "登录", operation = "手机验证码登录")
    public ResponseEntity<Result> phoneCodeLogin(@RequestParam String phone, @RequestParam String code) {
        Result r = new Result();
        if(StringUtils.isEmpty(phone)|| StringUtils.isEmpty(code)){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        if(!StringUtils.isEmpty(phone)){
            String code1 = redisTemplate.opsForValue().get("getSMSVerification" + phone);
            if(StringUtils.isEmpty(code1)){
                r.setResultCode(ResultCode.DATA_VERIFY_EXP_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
            if(!code1.equalsIgnoreCase(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_CODE_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }

        User user1 = userInfoService.findByPhone(phone);
        if(user1 != null){
            userInfoService.updateUser(user1);
        }else {
            user1 = new User();
            user1.setAccount(phone);
            user1.setState(User.USER_STATE_COMMON);
            user1.setPhoneNumber(phone);
            String password = StringUtils.getRandomNumCode(6);
            user1.setPassword(password);
            Long userId = userInfoService.saveUser(user1);
            if(phoneFlag) {
                //@todo 模板待修改
                r = SmsUtils.sendSMS(phone,"(初始密码)" + password);
            }
        }
        executeLogin("PC", phone, "123456", r);
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="用户邮箱注册", notes="用户邮箱注册")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 403, message = "20005:用户已存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/emailRegister")
    @LogAnnotation(module = "用户邮箱注册", operation = "用户邮箱注册")
    public ResponseEntity<Result> register(String email, String password, String key, String code) {
        Result r = new Result();
        if(StringUtils.isEmpty(email)|| StringUtils.isEmpty(password) || StringUtils.isEmpty(key) || StringUtils.isEmpty(code)){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        User temp = userInfoService.findByEmailAndState(email, User.USER_STATE_COMMON);
        if (null != temp) {
            r.setResultCode(ResultCode.USER_HAS_EXISTED);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        if(!StringUtils.isEmpty(email)){
            String code1 = redisTemplate.opsForValue().get(key);
            if(StringUtils.isEmpty(code1)){
                r.setResultCode(ResultCode.DATA_VERIFY_EXP_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
            if(!code1.equalsIgnoreCase(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_CODE_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }
        User user1 = userInfoService.findByEmailAndState(email, User.USER_STATE_DEACTIVE);
        if(user1 != null){
            user1.setAccount(email);
            user1.setPassword(password);
            user1.setRegisterType("E");
            PasswordHelper.encryptPassword(user1);
            userInfoService.updateUser(user1);
        }else {
            user1 = new User();
            user1.setAccount(email);
            user1.setRegisterType("E");
            user1.setState(User.USER_STATE_DEACTIVE);
            user1.setEmail(email);
            user1.setPassword(password);
            Long userId = userInfoService.saveUser(user1);
        }

        executeLogin("E", email, password, r);

        r.setData(Note.EMAIL_ACTIVE_USER_NOTIFICATION);
        String token = EncryptUtil.encodeDES(email + "===" + DateFormatUtils.format(new Date(), DateUtils.DATE_TIME_TO_SECOND));
        String url = HttpUtils.getSystemUrl(request, "email", null, "verify") + "?emailTime=" + encodeURIComponent(null, token).toString();;
        String html = "<a href='"+ url + "'><h1>请点击我激活您的账户<h1></a>";
        System.out.println(html);
        mailService.sendHtmlMail(email, Note.EMAIL_ACTIVE_USER_SUBJECT,
                html);

        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="用户手机号注册", notes="用户邮箱注册")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 403, message = "20005:用户已存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/phoneRegister")
    @LogAnnotation(module = "用户手机号注册", operation = "用户手机号注册")
    public ResponseEntity<Result> register(String phone, String password, String code) {
        Result r = new Result();
        if(StringUtils.isEmpty(phone)|| StringUtils.isEmpty(password)|| StringUtils.isEmpty(code)){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        User temp = userInfoService.findByPhone(phone);
        if (null != temp) {
            r.setResultCode(ResultCode.USER_HAS_EXISTED);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        if(!StringUtils.isEmpty(phone)){
            String code1 = redisTemplate.opsForValue().get("getSMSVerification" + phone);
            if(StringUtils.isEmpty(code1)){
                r.setResultCode(ResultCode.DATA_VERIFY_EXP_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
            if(!code1.equalsIgnoreCase(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_CODE_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }
        User user1 = new User();
        user1.setAccount(phone);
        user1.setRegisterType("P");
        user1.setState(User.USER_STATE_COMMON);
        user1.setPhoneNumber(phone);
        user1.setPassword(password);
        Long userId = userInfoService.saveUser(user1);
        executeLogin("P", phone, password, r);
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="第三方登录", notes="第三方登录")
    @PostMapping("/thirdLogin")
    @LogAnnotation(module = "第三方登录", operation = "第三方登录")
    public ResponseEntity<Result> thirdLogin(String type, String openId, String sex, String headerIcon, String nickName) {
        Result r = new Result();
        if(StringUtils.isEmpty(type)|| StringUtils.isEmpty(openId)|| StringUtils.isEmpty(headerIcon) || StringUtils.isEmpty(nickName)){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        User user1 = userInfoService.findByOpenIdAndThirdType(openId, type);
        String password=openId+type;
        String account = openId + "===" + type;
        if(user1 != null){
            user1.setSex(sex);
            user1.setAvatar(headerIcon);
            user1.setNickname(nickName);
            userInfoService.updateUser(user1);
        }else {
            user1 = new User();
            user1.setAccount(account);
            user1.setOpenId(openId);
            user1.setPassword(password);
            user1.setSex(sex);
            user1.setAvatar(headerIcon);
            user1.setNickname(nickName);
            user1.setRegisterType("T");
            user1.setThirdType(type);
            user1.setState(User.USER_STATE_COMMON);
            Long userId = userInfoService.saveUser(user1);
        }
        executeLogin("T", account, password, r);
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="用户注册", notes="用户注册并返回令牌\n" +
            "如果是手机注册:先调用getSMSVerification获取手机验证码;然后请求如下"+
            "{\n" +
            "  \"password\": \"123456\",\n" +
            "  \"phone\": \"13814959143\",\n" +
            "  \"verificationCode\": \"6164\"\n" +
            "}" +
            "如果是邮箱注册:先调用getImageVerification获取图片验证码;然后请求如下"+
            "{\n" +
            "  \"email\": \"123456@test.com\",\n" +
            "  \"emailCodeKey\": \"sjfslf123445DASFASDF\",\n" +
            "  \"password\": \"123456\",\n" +
            "  \"verificationCode\": \"1234\"\n" +
            "}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 403, message = "20005:用户已存在", response=Result.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/register")
    @LogAnnotation(module = "注册", operation = "用户注册")
    public ResponseEntity<Result> register(@RequestBody UserTO user) {
        Result r = new Result();
        String account = user.getAccount();
        String email = user.getEmail();
        String phone = user.getPhone();
        if(StringUtils.isEmpty(account) && StringUtils.isEmpty(email) && StringUtils.isEmpty(phone) || StringUtils.isEmpty(user.getPassword())){
            r.setResultCode(ResultCode.PARAM_IS_INVALID);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        User temp = null;
        if(!StringUtils.isEmpty(account)){
            temp = userInfoService.findByAccount(account);
        }else if(!StringUtils.isEmpty(email)){
            temp = userInfoService.findByEmailAndState(email, User.USER_STATE_COMMON);
            if(StringUtils.isEmpty(user.getVerificationCode()) || StringUtils.isEmpty(user.getEmailCodeKey())){
                r.setResultCode(ResultCode.PARAM_IS_INVALID);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }else if(!StringUtils.isEmpty(phone)){
            temp = userInfoService.findByPhone(phone);
            if(StringUtils.isEmpty(user.getVerificationCode())){
                r.setResultCode(ResultCode.PARAM_IS_INVALID);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }
        if (null != temp) {
            r.setResultCode(ResultCode.USER_HAS_EXISTED);
            return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
        }

        if(!StringUtils.isEmpty(email)){
            String code = redisTemplate.opsForValue().get(user.getEmailCodeKey());
            if(StringUtils.isEmpty(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_EXP_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
            if(!user.getVerificationCode().equalsIgnoreCase(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_CODE_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }else if(!StringUtils.isEmpty(phone)){
            String code = redisTemplate.opsForValue().get("getSMSVerification" + phone);
            if(StringUtils.isEmpty(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_EXP_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
            if(!user.getVerificationCode().equalsIgnoreCase(code)){
                r.setResultCode(ResultCode.DATA_VERIFY_CODE_ERROR);
                return ResponseEntity.status(HttpStatus.MULTI_STATUS.FORBIDDEN).body(r);
            }
        }
        String password = user.getPassword();
        User user1 = new User();
        if(!StringUtils.isEmpty(user.getEmail())){
            user1.setState(User.USER_STATE_DEACTIVE);
        }
        user1.setEmail(email);
        user1.setPhoneNumber(phone);
        user1.setAccount(account);
        user1.setPassword(password);
        user1.setNickname(user.getNickname());
        Long userId = userInfoService.saveUser(user1);
        if (userId > 0) {
            String type="C";
            if(StringUtils.isEmpty(account)) {
                if(!StringUtils.isEmpty(user.getEmail())){
                    account = user.getEmail();
                    type="E";
                }else if(!StringUtils.isEmpty(user.getPhone())){
                    account = user.getPhone();
                    type="P";
                }
            }
            executeLogin(type, account, password, r);
        } else {
            r.setResultCode(ResultCode.USER_Register_ERROR);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }
        FileUtils.deleteArticleTempFolder(baseFolderPath, FileUtils.UPLOAD_FILE_IMAGE, null,user.getEmailCodeKey()+".png");
        if(!StringUtils.isEmpty(user.getEmail())){
            r.setData(Note.EMAIL_ACTIVE_USER_NOTIFICATION);
            String token = EncryptUtil.encodeDES(email + "===" + DateFormatUtils.format(new Date(), DateUtils.DATE_TIME_TO_SECOND));
            String url = HttpUtils.getSystemUrl(request, "email", null, "verify") + "?emailTime=" + encodeURIComponent(null, token).toString();;
            String html = "<a href='"+ url + "'><h1>请点击我激活您的账户<h1></a>";
            System.out.println(html);
            mailService.sendHtmlMail(email, Note.EMAIL_ACTIVE_USER_SUBJECT,
                    html);
        }
        return ResponseEntity.ok(r);
    }

    @ApiOperation(value="用户退出系统", notes="用户登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Oauth-Token", value = "令牌", required = true, dataType = "String", paramType = "header")
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

    private ResponseEntity<Result> executeLogin(String type, String account, String password, Result r) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(type+"==="+account, password);
        try {
            subject.login(token);
            Map map = r.simple();
            map.put(MySessionManager.AUTHORIZATION, subject.getSession().getId());
            SecurityUtils.getSubject().getSession().setAttribute(Base.CURRENT_USER, subject.getPrincipal());
        }catch (UnknownAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_EXIST);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(r);
        } catch (LockedAccountException e) {
            r.setResultCode(ResultCode.USER_ACCOUNT_FORBIDDEN);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(r);
        } catch (DisabledAccountException e) {
            r.setResultCode(ResultCode.USER_NOT_ACTIVE);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
        } catch (AuthenticationException e) {
            r.setResultCode(ResultCode.USER_LOGIN_ERROR);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
        } catch (Exception e) {
            r.setResultCode(ResultCode.ERROR);
            return ResponseEntity.status(HttpStatus.CREATED).body(r);
        }
        r.setResultCode(ResultCode.SUCCESS);
        return ResponseEntity.ok(r);

    }

}
