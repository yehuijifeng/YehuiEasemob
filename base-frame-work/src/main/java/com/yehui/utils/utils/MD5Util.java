/**
 * LAB139 com.alsfox.lab139.utils 2015
 */
package com.yehui.utils.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * 1.0 MD5加密工具类
 */
public class MD5Util {

    /**
     * 小写md5加密
     *
     * @param sourceStr
     * @return
     */
    public static String md5(String sourceStr) {
        String s = null;
        try {
            /**特殊字符串用encoder转码，并且把所有iso-8859码转成国际通用码utf-8*/
            sourceStr = URLEncoder.encode(sourceStr, "utf-8").replace(" ", "%20");
            sourceStr = new String(sourceStr.getBytes("ISO-8859-1"), "UTF-8");

            byte[] source = sourceStr.getBytes();
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 大写md5加密
     *
     * @param sourceStr
     * @return
     */
    public static String MD5(String sourceStr) {
        String s = null;
        try {
            /**特殊字符串用encoder转码，并且把所有iso-8859码转成国际通用码utf-8*/
            sourceStr = URLEncoder.encode(sourceStr, "utf-8").replace(" ", "%20");
            sourceStr = new String(sourceStr.getBytes("ISO-8859-1"), "UTF-8");

            byte[] source = sourceStr.getBytes();
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F'};
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}