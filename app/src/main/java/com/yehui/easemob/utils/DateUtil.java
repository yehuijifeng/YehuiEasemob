package com.yehui.easemob.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Luhao on 2016/3/15.
 */
public class DateUtil {

    /**
     * 时间戳转化为Sting或Date
     *
     * @param stamp
     * @return
     */
    public static String stampToTime(long stamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(stamp);
        try {
            return com.yehui.utils.utils.DateUtil.getTimeReduction(d);
        } catch (ParseException e) {
            d=d.substring(10,15);
            return d;
        }
    }

    /**
     * Date或者String转化为时间戳
     *
     * @param time
     * @return
     */
    public static long timeToStamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
