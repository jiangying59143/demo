package com.mall.demo.configure;

import com.mall.demo.common.utils.PasswordHelper;
import com.mall.demo.model.privilege.SysPermission;
import com.mall.demo.model.privilege.SysRole;
import com.mall.demo.model.privilege.User;
import com.mall.demo.service.UserInfoService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserInfoService userInfoService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User)principals.getPrimaryPrincipal();

        for(SysRole role: user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            if(role.getPermissions() != null) {
                for (SysPermission p : role.getPermissions()) {
                    authorizationInfo.addStringPermission(p.getPermission());
                }
            }
        }
        return authorizationInfo;
    }

    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        //获取用户的输入的账号.
        String type_account = (String)token.getPrincipal();
        User user=null;
        String[] typeAccArr = type_account.split("===");
        String type=typeAccArr[0];
        if("C".equals(type)) {
            user = userInfoService.findByAccount(typeAccArr[1]);
        }else if("E".equals(type)) {
            user = userInfoService.findByEmail(typeAccArr[1]);
        }else if("P".equals(type) || "PC".equals(type)) {
            user = userInfoService.findByPhone(typeAccArr[1]);
        }else if("T".equals(type)){
            user = userInfoService.findByOpenIdAndThirdType(typeAccArr[1], typeAccArr[2]);
        }
        user.setRegisterType(type);
        if("PC".equals(type)) {
            user.setPassword("123456");
            PasswordHelper.encryptPassword(user);
        }
        System.out.println("----->>user="+ user);
        if (user == null) {
            return null;
        }
        if (user.getState() == user.USER_STATE_LOCKED) { //账户冻结
            throw new LockedAccountException();
        }

        if (user.getState() == User.USER_STATE_DEACTIVE) { //账户未激活
            throw new DisabledAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    /*@Override
    protected void doClearCache(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        super.doClearCache(principals);
        clearCachedAuthenticationInfo(principals);
        clearCachedAuthorizationInfo(principals);
    }
   @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        super.clearCachedAuthenticationInfo(principals);
        Object key = principals.getPrimaryPrincipal();
        getAuthenticationCache().remove(key);
    }
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        super.clearCachedAuthorizationInfo(principals);
        Object key = getAuthorizationCacheKey(principals);
        getAuthorizationCache().remove(key);
    }

    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return principals.getPrimaryPrincipal();
    }*/

}
