package com.mall.demo.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DateUtils {

    public static final String DATE_TIME_TO_MINUTE="yyyy-MM-dd hh:mm";
    public static final String DATE_TIME_TO_SECOND="yyyy-MM-dd hh:mm:ss";

    public static void main(String[] args) {
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_TIME_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_TIME_NO_T_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DateFormatUtils.SMTP_DATETIME_FORMAT.getPattern()));
        System.out.println(DateFormatUtils.format(new Date(), DATE_TIME_TO_MINUTE));
    }
}
