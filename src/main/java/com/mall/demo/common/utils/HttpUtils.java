package com.mall.demo.common.utils;

import com.mall.demo.model.privilege.User;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String getSystemUrl(HttpServletRequest request, String filePath, User user, String fileName) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String strBackUrl = "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + httpRequest.getContextPath() + "/"
                + filePath + "/"
                + user.getId() + "/"
                + fileName;
        return strBackUrl;
    }
}
