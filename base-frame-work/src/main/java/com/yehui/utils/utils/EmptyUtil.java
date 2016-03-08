package com.yehui.utils.utils;

import android.view.View;

/**
 * Created by yehuijifeng
 * on 2015/12/22.
 * 判断空指针的工具类
 */
public class EmptyUtil {
    /**
     * 防止被实例化
     */
    private EmptyUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 字符串
     *
     * @param str
     * @return
     */
    public static boolean isStringEmpty(String str) {
        if (str == null || ("").equals(str)) return true;
        return false;
    }

    /**
     * 控件，包括TextView/EditView
     *
     * @param textview
     * @return
     */
    public static boolean isTextEmpty(View textview) {
        try {
            if (textview == null || ("").equals(textview.toString().trim())) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static int abs(int t) {
        return t * (1 - ((t >>> 31) << 1));
    }
}
