package com.mall.demo.common.utils;

import com.mall.demo.model.privilege.User;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String getSystemUrl(HttpServletRequest request, String filePath, String fileName) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User currentUser = UserUtils.getCurrentUser();
        String strBackUrl = "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + httpRequest.getContextPath() + "/"
                + filePath + "/"
                + currentUser.getId() + "/"
                + fileName;
        return strBackUrl;
    }
}
