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
//    public static String stampToTime(long stamp) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String d = format.format(stamp);
//        try {
//            return com.yehui.utils.utils.DateUtil.getTimeReduction(d);
//        } catch (ParseException e) {
//            d = d.substring(10, 15);
//            return d;
//        }
//    }
    public static String stampToTime(long stamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String stampTime = format.format(stamp);
        String y = stampTime.substring(0, 4);
        String m = stampTime.substring(4, 6);
        String d = stampTime.substring(6, 8);
        String h = stampTime.substring(8, 10);
        String mm = stampTime.substring(10, 12);
        String s = stampTime.substring(12, 14);

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String returnTime = df.format(new Date());
        String ys = returnTime.substring(0, 4);
        String ms = returnTime.substring(4, 6);
        String ds = returnTime.substring(6, 8);
        String hs = returnTime.substring(8, 10);
        String mms = returnTime.substring(10, 12);
        String ss = returnTime.substring(12, 14);
        if (y.equals(ys)) {
            if (m.equals(ms)) {
                if (d.equals(ds)) {
                    if (h.equals(hs) && mm.equals(mms)) {
                        int sss = Integer.parseInt(s);
                        int ssss = Integer.parseInt(ss);
                        return ssss - sss + "秒前";
                    } else {
                        return h + " : " + mm;
                    }
                } else {
                    int dd = Integer.parseInt(d);
                    int ddd = Integer.parseInt(ds);
                    if (ddd - dd == 1) {
                        return "昨天";
                    } else if (ddd - dd == 2) {
                        return "前天";
                    } else {
                        return ddd - dd + "天前";
                    }
                }
            } else {
                return ms + "月";
            }
        } else {
            return ys + "年";
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
