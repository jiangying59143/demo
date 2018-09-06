package com.mall.demo.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class HttpUtils {

    public static String getSystemUrl(HttpServletRequest request, String filePath, String fileName) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String strBackUrl = "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + httpRequest.getContextPath()
                + filePath + File.separatorChar
                + fileName;
        return strBackUrl;
    }
}
