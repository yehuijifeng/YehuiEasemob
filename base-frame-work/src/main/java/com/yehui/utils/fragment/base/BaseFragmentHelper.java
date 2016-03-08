package com.yehui.utils.fragment.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yehui.utils.activity.base.BaseHelper;

/**
 * Created by yehuijifeng
 * on 2015/12/5
 */
public class BaseFragmentHelper extends BaseHelper {

    private Fragment fragment;

    public BaseFragmentHelper(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    /**
     * 显式跳转Activity的方法(带Bundle)
     *
     * @param cls    要跳转的Activity的类
     * @param bundle 装载了各种参数的Bundle
     */
    @Override
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivity(intent);
    }


    /**
     * 显式跳转Activity的方法(不带任何参数)
     *
     * @param cls 要跳转的Activity的类
     */
    @Override
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，不带参数)
     * @param cls         要跳转的Activity的类
     * @param requestCode 跳转Activity的请求码
     */
    @Override
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，带Bundle)
     *
     * @param cls         要跳转的Activity的类
     * @param bundle      装载了各种参数的Bundle
     * @param requestCode 跳转Activity的请求码
     */
    @Override
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        fragment.startActivityForResult(intent, requestCode);
    }

}
