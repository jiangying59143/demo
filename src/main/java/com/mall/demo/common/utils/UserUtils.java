package com.mall.demo.common.utils;

import com.mall.demo.common.constants.Base;
import com.mall.demo.model.privilege.User;
import org.apache.shiro.SecurityUtils;

/**
 * @author shimh
 * <p>
 * 2018年1月25日
 */
public class UserUtils {

    public static User getCurrentUser() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Base.CURRENT_USER);
        return user;
    }
}
