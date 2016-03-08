package com.yehui.utils.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yehui.utils.activity.base.BaseHelper;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by yehuijifeng
 * on 2015/12/6.
 * fragment的基本类
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 子类需要实现的抽象类
     */

    /**
     * 布局id,传入了ToolBarActivity中重写的setContentView中，以便于添加toolbar
     */
    protected abstract int setFragmentLayoutId();

    /**
     * 初始化view，传入父类view
     */
    protected abstract void initView(View parentView);

    /**
     * 初始化data
     */
    protected abstract void initData();

    /**
     * ------------------------------------------------------------------------------------------
     */
    /**
     * 父类activity对象
     */
    protected FragmentActivity parentActivity;

    /**
     * activity和fragment的代理类
     */
    protected BaseHelper helper;

    /**
     * 获取屏幕宽高
     */
    protected DisplayMetrics outMetrics = new DisplayMetrics();

    /**
     * gson,解析json数据或者类转json时能用到
     */
    protected Gson gson = new Gson();

    /**
     * eventBus工具类
     */
    protected EventBus eventBus;

    /**
     * imageloader工具类的初始化
     */
    protected ImageLoader imageLoader;

    /**
     * 父布局填充
     */
    protected ViewGroup root;

    /**
     * 该fragment是否处于显示状态
     */
    protected boolean isVisible;

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(setFragmentLayoutId(), container, false);
    }

    /**
     * 视图创建
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initProperties(view);
        initView(view);
        initData();
    }

    /**
     * 选中状态
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        //到显示状态为true，不可见为false
        isVisible = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initProperties(View parentView) {
        parentActivity = getActivity();
        helper = new BaseFragmentHelper(getActivity(), this);
        root = (ViewGroup) parentView;
        imageLoader = ImageLoader.getInstance();
        eventBus = EventBus.getDefault();
        outMetrics = helper.outMetrics;
        if (!isRegistered(this)) {
            registerEventBus(this);
        }
    }

    /**
     * 此方法以后才能获取activity
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    protected int getWindowHeight() {
        return helper.getWindowHeight();
    }

    protected int getWindowWidth() {
        return helper.getWindowWidth();
    }

    /**
     * 隐藏/显示软键盘的方法
     */
    protected void hideSoftInputFromWindow(View view) {
        helper.hideSoftInputFromWindow(view);
    }

    protected void showSoftInputFromWindow(View view) {
        helper.showSoftInputFromWindow(view);
    }

    public String[] getResourceStringArray(int resId) {
        return helper.getResourceStringArray(resId);
    }

    public String getResourceString(int resId) {
        return helper.getResourceString(resId);
    }

    public Drawable getResourceDrawable(int resId) {
        return helper.getResourceDrawable(resId);
    }

    public int getResourceColor(int resId) {
        return helper.getResourceColor(resId);
    }

    public int getResourceInteger(int resId) {
        return helper.getResourceInteger(resId);
    }

    public float getResourceDimension(int resId) {
        return helper.getResourceDimension(resId);
    }

    public View inflate(int resource, ViewGroup root) {
        return helper.inflate(resource, root);
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return helper.inflate(resource, root, attachToRoot);
    }

    public void registerEventBus(Object o) {
        helper.registerEventBus(o);
    }

    public void unregisterEventBus(Object o) {
        helper.unregisterEventBus(o);
    }

    public void postEventBus(Object o) {
        helper.postEventBus(o);
    }

    public void hasSubscriberForEvent(Class<?> cls) {
        helper.hasSubscriberForEvent(cls);
    }

    public boolean isRegistered(Object o) {
        return helper.isRegistered(o);
    }

    protected Bundle getBundle() {
        return helper.getBundle();
    }

    protected int getInt(String key, int defaultValue) {
        return helper.getInt(key, defaultValue);
    }

    protected String getString(String key, String defaultValue) {
        return helper.getString(key, defaultValue);
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        return helper.getBoolean(key, defaultValue);
    }

    protected double getDouble(String key, double defaultValue) {
        return helper.getDouble(key, defaultValue);
    }

    protected float getFloat(String key, float defaultValue) {
        return helper.getFloat(key, defaultValue);
    }

    protected Parcelable getParcelable(String key) {
        return helper.getParcelable(key);
    }

    protected ArrayList<? extends Parcelable> getParcelableList(String key) {
        return helper.getParcelableList(key);
    }

    /**
     * 去设置网络
     */
    public void toSetNetWork() {
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(wifiSettingsIntent);
    }


    /**
     * 显式跳转Activity的方法(不带任何参数)
     *
     * @param cls 要跳转的Activity的类
     */
    public void startActivity(Class<?> cls) {
        helper.startActivity(cls);
    }

    /**
     * 显式跳转Activity的方法(带Bundle)
     *
     * @param cls    要跳转的Activity的类
     * @param bundle 装载了各种参数的Bundle
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        helper.startActivity(cls, bundle);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，不带参数)
     *
     * @param cls         要跳转的Activity的类
     * @param requestCode 跳转Activity的请求码
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        helper.startActivityForResult(cls, requestCode);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，带Bundle)
     *
     * @param cls         要跳转的Activity的类
     * @param bundle      装载了各种参数的Bundle
     * @param requestCode 跳转Activity的请求码
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        helper.startActivityForResult(cls, bundle, requestCode);
    }

    /**
     * 弹出时间短暂的Toast
     *
     * @param text
     */
    public void showShortToast(String text) {
        helper.showShortToast(text);
    }

    /**
     * 弹出时间较长的Toast
     *
     * @param text
     */
    public void showLongToast(String text) {
        helper.showLongToast(text);
    }


    /**
     * 防止eventbus报错
     *
     * @param obj
     */
    public void onEventMainThread(Object obj) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRegistered(this)) {
            unregisterEventBus(this);
        }
    }
}
