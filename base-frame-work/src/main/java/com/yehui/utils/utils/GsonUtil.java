package com.yehui.utils.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Yehuijifeng
 * on 2015/10/27.
 * 全部基于gson三方包的解析
 */
public class GsonUtil {

    private static Gson gson = new Gson();
    private static JSONObject jsonObject;
    private static JSONArray jsonArray;

    /**
     * 实体类数据转换成json数据
     * 传入实体类
     */
    public static String toClassJson(Class cla) {
        try {
            return gson.toJson(cla);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * object类型转换json数据
     *
     * @param obj
     * @return
     */
    public static String toObjectJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * json数据转换成jsonobject（单个）
     *
     * @param str
     * @return
     */
    public static JSONObject toJSONObject(String str) {
        try {
            return jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * json数据转换成jsonobject（列表）
     *
     * @param str
     * @return
     */
    public static JSONArray toJSONArray(String str) {
        try {
            return jsonArray = new JSONArray(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将json数组放进实体类中
     */
    public static <T> T fromJsonObject(String jsonObject, Class<T> cla) {
        try {
            return gson.fromJson(jsonObject, cla);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
