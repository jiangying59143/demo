package com.mall.demo.util;

import com.mall.demo.constants.Base;
import com.mall.demo.model.privilege.UserInfo;
import org.apache.shiro.SecurityUtils;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public class UserUtils {

    public static UserInfo getCurrentUser() {
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getSession().getAttribute(Base.CURRENT_USER);
        return user;
    }
}
