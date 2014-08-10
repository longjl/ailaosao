package com.ailaosao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by longjianlin on 14-7-24.
 * V 1.0
 * *********************************
 * Desc: 友好时间
 * *********************************
 */
public class PrintTime {
    private final static SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat MY_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    public static String getNiceDate(Object o) {
        if (null == o) return "";
        String dateString = o.toString();
        String result = null;
        try {
            Date date = TIME_STAMP_FORMAT.parse(dateString);
            long currentTime = new Date().getTime() - date.getTime();
            int time = (int) (currentTime / 1000);
            if (time < 60) {
                result = "刚刚";
            } else if (time >= 60 && time < 3600) {
                result = time / 60 + "分钟前";
            } else if (time >= 3600 && time < 86400) {
                result = time / 3600 + "小时前";
            } else if (time >= 86400 && time < 864000) {
                result = time / 86400 + "天前";
            } else {
                result = MY_DATE_FORMAT.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String call(String params) {
        if (params == null || params.length() == 0) {
            return null;
        }
        return getNiceDate(params);
    }
}
