package com.jacky.util;

import androidx.collection.ArrayMap;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jacky on 2018/10/29.
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

    public static long parseLong(String string){
        if(TextUtils.isEmpty(string)) return 0;
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将格式化的时间字符串逆编码为long值
     */
    public static long parseDateString(String time, String pattern) {
        long d = 0;
        try {
            Date date = new SimpleDateFormat(pattern, Locale.CHINESE).parse(time);
            d = date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    /**
     * 用于判断2个double类型的值 是否相同
     */
    public static boolean equals(double value1, double value2) {
        return Math.abs(value1 - value2) < 0.0000001;
    }
    /**
     * 用于判断2个float类型的值 是否相同
     */
    public static boolean equals(float value1, float value2) {
        return Math.abs(value1 - value2) < 0.0000001;
    }
    /**
     * 从URL解析出参数的值
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
     * 格式化秒的时间值
     * @param seconds 秒
     */
    public static String formatSeconds(String pattern, int seconds) {
        return formatMillisecond(pattern, 1000L * seconds);
    }

    /**
     * 格式化毫秒的时间值
     * @param millisecond 毫秒
     */
    public static String formatMillisecond(String pattern, long millisecond) {
        if(millisecond == 0) return "";
        DateFormat format = new SimpleDateFormat(pattern, Locale.CHINESE);
        return format.format(new Date(millisecond));
    }

    /**
     * 将字符串数组使用逗号拼接成字符串
     */
    public static String toString(String[] strings) {
        return toString(strings, ",");
    }

    /**
     * 将字符串数组使用其他分隔符拼接成字符串
     * @param strings 字符串数组
     * @param separator 分隔符
     */
    public static String toString(String[] strings, String separator) {
        if(strings == null || strings.length == 0) return "";
        int iMax = strings.length - 1;
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; ;i++) {
            stringBuilder.append(strings[i]);
            if(i == iMax) return stringBuilder.toString();
            stringBuilder.append(separator);
        }
    }

    /**
     * 将指定的秒数转换成00:00:00格式时间，例如：65转换后为00:01:05
     */
    public static String formatDuration(int duration) {
        if (duration < 0) throw new AssertionError("duration < 0");

        int time = duration % 3600;
        int hour = duration / 3600;
        int minute = time / 60;
        int second = time % 60;
        return String.format(Locale.CHINESE,"%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * 将指定的秒数转换成00:00格式时间，例如：65转换后为01:05
     */
    public static String formatDurationWithoutHour(int duration) {
        if (duration < 0) throw new AssertionError("duration < 0");

        int minute = duration / 60;
        int second = duration % 60;
        return String.format(Locale.CHINESE,"%02d:%02d", minute, second);
    }
}
