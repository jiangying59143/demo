package com.mall.demo.configure;

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
            for(SysPermission p:role.getPermissions()){
                authorizationInfo.addStringPermission(p.getPermission());
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
        String account = (String)token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userInfoService.findByAccount(account);
        System.out.println("----->>user="+ user);
        if (user == null) {
            return null;
        }
        if (user.getState() == 2) { //账户冻结
            throw new LockedAccountException();
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
