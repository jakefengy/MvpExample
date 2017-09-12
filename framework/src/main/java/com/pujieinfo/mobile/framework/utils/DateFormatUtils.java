package com.pujieinfo.mobile.framework.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 2017-05-26
 */
public class DateFormatUtils {

    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String timestamp2Date(long timestamp, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(timestamp * 1000L);
        return s;
    }

    // 格式化时间
    public static String formatTime(long nTime) {
        if (0 == nTime)
            return "";

        if (isToday(nTime)) {    // 今天
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(nTime);
            SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return dateFmt.format(time.getTime());
        } else if (isYesterday(nTime)) {    // 昨天
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(nTime);
            SimpleDateFormat dateFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return "昨天 " + dateFmt.format(time.getTime());
        } else {
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(nTime);
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return dateFmt.format(time.getTime());
        }
    }

    // 判断时间是否是今天
    public static boolean isToday(long nTime) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(nTime);

        Calendar c2 = Calendar.getInstance();

        return isSameDay(c1, c2);
    }

    // 判断时间是否是昨天
    public static boolean isYesterday(long nTime) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(nTime);

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, -1);

        return isSameDay(c1, c2);
    }

    // 判断两个时间是否同一天
    public static boolean isSameDay(Calendar c1, Calendar c2) {
        return ((c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH))
                && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
                && (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)));
    }
}
