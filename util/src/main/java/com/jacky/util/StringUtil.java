package com.jacky.util;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by lixinquan on 2018/10/29.
 */
public final class StringUtil {

    public static int parseInt(String string) {
        if(TextUtils.isEmpty(string)) return 0;
        try{
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double parseDouble(String string) {
        if(TextUtils.isEmpty(string)) return 0;
        try{
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 从URL解析出参数的值
     * @param url
     * @return
     */
    public static Map<String, String> parseUrlParams(String url) {
        if(TextUtils.isEmpty(url)) return Collections.emptyMap();

        int i = url.indexOf('?');
        if(i == -1) return Collections.emptyMap();
        String[] pp = url.substring(i + 1).split("&");
        Map<String, String> params = new ArrayMap<>();
        for(String p : pp) {
            String ppp[] = p.split("=");
            if(ppp.length < 2) continue;
            params.put(ppp[0], ppp[1]);
        }
        return params;
    }

    /**
     * 格式化没有豪秒的时间值
     * @param pattern
     * @param phpTime
     * @return
     */
    public static String formatSecondTime(String pattern, int phpTime) {
        return formatMillisTime(pattern, 1000L * phpTime);
    }

    /**
     * 格式化带毫秒的时间值
     * @param pattern
     * @param time
     * @return
     */
    public static String formatMillisTime(String pattern, long time) {
        if(time == 0) return "";
        DateFormat format = new SimpleDateFormat(pattern, Locale.CHINESE);
        return format.format(new Date(time));
    }
}
