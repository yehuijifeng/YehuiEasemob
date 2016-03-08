package com.yehui.utils.http;

import android.text.TextUtils;

import com.yehui.utils.utils.MD5Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求的参数设置工具类
 */
public class SignUtil {

    /**
     * 签名
     */
    public static final String KEY_SIGN = "sign";

    /**
     * key
     */
    public static final String KEY_PRIVATE = "key";

    /**
     * key的值
     */
    public static final String KEY_PRIVATE_STR = "";

    /**
     * 时间戳
     */
    public static final String KEY_TIMESTAMP = "timestamp";

    /**
     * 创建加密的参数,返回ip地址action后的值
     * @param param
     * @return
     */
    public static String createEncryptionParam(Map<String, Object> param) {
        Collection<String> keySet = param.keySet();//键的集合
        List<String> list = new ArrayList<>(keySet);//放入键集合到arraylist中
        Collections.sort(list);//对key键值按字典升序排序
        String paramStr = "";
        //循环得到post请求的参数字符串
        for (int i = 0; i < list.size(); i++) {
            paramStr += list.get(i) + "=" + param.get(list.get(i)) + "&";
        }
        //去掉最后一个&符号
        if (!TextUtils.isEmpty(paramStr))
            paramStr = paramStr.substring(0, paramStr.length() - 1);
        //将参数生成签名
        String sign = "&" + KEY_SIGN + "=" + MD5Util.MD5(paramStr);

        //如果参数中包含了key，则去掉key，key在传参数的时候不能被看到，只有在生成签名的时候才用到；防止外人获得签名；
        if (paramStr.contains("&" + KEY_PRIVATE))
            paramStr = paramStr.replace("&" + KEY_PRIVATE + "=" + KEY_PRIVATE_STR,"");
        //合并post请求的地址
        return "?" + paramStr + sign;
    }

    /**
     * 根据传递的字段生成签名
     * @param param
     * @return
     */
    public static String createSign(Map<String, Object> param){
        Collection<String> keySet = param.keySet();//键的集合
        List<String> list = new ArrayList<>(keySet);//放入键集合到arraylist中
        Collections.sort(list);//对key键值按字典升序排序
        String paramStr = "";
        //循环得到post请求的参数字符串
        for (int i = 0; i < list.size(); i++) {
            paramStr += list.get(i) + "=" + param.get(list.get(i)) + "&";
        }
        //去掉最后一个&符号
        if (!TextUtils.isEmpty(paramStr))
            paramStr = paramStr.substring(0, paramStr.length() - 1);
        return MD5Util.MD5(paramStr);
    }


    /**
     * 获得请求参数的map集合
     */
    public static Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(KEY_PRIVATE, KEY_PRIVATE_STR);//key
        //params.put(KEY_TIMESTAMP, System.currentTimeMillis());//时间戳
        return params;
    }

}
