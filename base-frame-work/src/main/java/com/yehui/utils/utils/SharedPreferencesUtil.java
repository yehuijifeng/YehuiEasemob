package com.yehui.utils.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by yehuijifeng
 * on 2015年10月27日
 * 键值对存储工具类
 */
public class SharedPreferencesUtil {

    /**
     * 防止被实例化
     */
    private SharedPreferencesUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    public SharedPreferencesUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void saveInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void saveFloat(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.apply();
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }

    public void clear() {
        mEditor.clear();
    }
}
