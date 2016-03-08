package com.yehui.utils.activity.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.BuildConfig;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yehui.utils.application.ActivityCollector;
import com.yehui.utils.http.action.RequestAction;
import com.yehui.utils.http.interfaces.RequestInterface;
import com.yehui.utils.http.request.RequestHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yehuijifeng
 * on 2015/11/25.
 * activity和fragment的代理类
 */
public class BaseHelper {

    /**
     * 默认每个activity和fragment都注册eventbus
     */
    protected EventBus eventBus;

    /**
     * 获取当前屏幕的长宽的方法
     */
    public DisplayMetrics outMetrics = new DisplayMetrics();

    /**
     * 代理的目标activity
     */
    protected BaseActivity activity;

    /**
     * 获取项目资源
     */
    protected Resources resources;

    /**
     * 系统输入法服务
     */
    protected InputMethodManager inputMethodManager;

    /**
     * 父布局
     */
    protected LayoutInflater inflater;

    /**
     * toast弹出信息
     */
    protected Toast toast;

    /**
     * 网络请求的方法接口
     */
    protected RequestInterface requestInterface;

    public BaseHelper(Context context) {
        activity = (BaseActivity) context;
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        requestInterface = RequestHandle.getRequestInterface(activity);
        eventBus = EventBus.getDefault();
        resources = activity.getResources();
        inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inflater = activity.getLayoutInflater();
    }

    /**
     * get请求，异步
     * @param action action的枚举
     */
    public void sendGetRequest(RequestAction action) {
        requestInterface.sendGetRequest(action);
    }

    /**
     * get请求，线程阻塞请求
     * @param action action的枚举
     */
    public void sendGetInstanceRequest(RequestAction action) {
        requestInterface.sendGetInstanceRequest(action);
    }
    /**
     * post请求，异步
     * @param action action的枚举
     */
    public void sendPostRequest(RequestAction action) {
        requestInterface.sendPostRequest(action);
    }

    /**
     * get请求，线程阻塞请求
     * @param action action的枚举
     */
    public void sendPostInstanceRequest(RequestAction action) {
        requestInterface.sendPostInstanceRequest(action);
    }
    /**
     * post传文件请求
     * @param files 文件组
     * @param action action的枚举
     */
    public void sendPostAddFileRequest(File[] files, RequestAction action) {
        requestInterface.sendPostAddFileRequest(files, action);
    }

    /**
     * 下载大文件
     * @param url 下载的地址
     */
    public void sendDownloadFile(String url) {
        requestInterface.downloadFile(url);
    }
    public void sendDownloadFile(RequestAction action) {
        requestInterface.downloadFile(action);
    }

    /**
     * 网络请求设置超时时间
     */
    public void setTimeOut(int value) {
        requestInterface.setTimeOut(value);
    }

    /**断开/启动，所有正在进行的请求
     * @param bl true断开，false开启
     */
    public void cancelAllRequests(boolean bl) {
        requestInterface.cancelAllRequests(bl);
    }

    /**断开某个请求
     * @param action action的枚举
     */
    public void cancelByActionRequests(RequestAction action) {
        requestInterface.cancelByActionRequests(action);
    }

    /**
     * 隐藏系统输入法
     *
     * @param view 作用于哪个view
     */
    public void hideSoftInputFromWindow(View view) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示系统输入法
     *
     * @param view 作用于哪个view
     */
    public void showSoftInputFromWindow(View view) {
        inputMethodManager.showSoftInput(view, 0);
    }


    /**
     * @return 返回当前屏幕的高
     */
    public int getWindowHeight() {
        return outMetrics.heightPixels;
    }

    /**
     * @return 返回当前屏幕的宽
     */
    public int getWindowWidth() {
        return outMetrics.widthPixels;
    }

    /**
     * @return 返回当前intent中的bundle
     */
    public Bundle getBundle() {
        Intent intent = activity.getIntent();
        return intent == null ? null : intent.getExtras();
    }

    /**
     * @param key          对应bundle中的标识
     * @param defaultValue 默认值，娶不到bundle中的值的时候返回该默认值
     */
    public int getInt(String key, int defaultValue) {
        if (getBundle() != null)
            return getBundle().getInt(key, defaultValue);
        else
            return defaultValue;
    }

    /**
     * 下同，，，，bundle的用法
     */
    public String getString(String key, String defaultValue) {
        if (getBundle() != null)
            return getBundle().getString(key, defaultValue);
        else
            return defaultValue;
    }

    /**
     * 获取上一个Activity传过来的布尔值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBundle() != null && getBundle().getBoolean(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的double值
     */
    public double getDouble(String key, double defaultValue) {
        if (getBundle() != null)
            return getBundle().getDouble(key, defaultValue);
        else
            return defaultValue;
    }

    /**
     * 获取上一个Activity传过来的float值
     */
    public float getFloat(String key, float defaultValue) {
        if (getBundle() != null)
            return getBundle().getFloat(key, defaultValue);
        else
            return defaultValue;
    }

    /**
     * 获取上一个Activity传过来的实现了Parcelable接口的对象
     */
    public Parcelable getParcelable(String key) {
        if (getBundle() != null)
            return getBundle().getParcelable(key);
        else
            return null;
    }

    /**
     * 获取上一个Activity传过来的字符串集合
     */
    public List<String> getStringArrayList(String key) {
        if (getBundle() != null)
            return getBundle().getStringArrayList(key);
        else
            return null;
    }

    /**
     * 获取上一个Activity传过来的实现了Parcelable接口的对象的集合
     */
    public ArrayList<? extends Parcelable> getParcelableList(String key) {
        if (getBundle() != null)
            return getBundle().getParcelableArrayList(key);
        else
            return null;
    }
    /**
     * ----------------------------------------------------------------------------
     */

    /**
     * 显式跳转Activity的方法(带Bundle)
     *
     * @param cls    要跳转的Activity的类
     * @param bundle 装载了各种参数的Bundle
     */
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * 显式跳转Activity的方法(不带任何参数)
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，带Bundle)
     *
     * @param requestCode 跳转Activity的请求码
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 显式跳转Activity的方法(带返回结果，不带参数)
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 弹出时间短暂的Toast
     */
    public void showShortToast(String text) {
        if (toast == null)
            toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        else
            toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(text);

        toast.show();
    }

    /**
     * 弹出时间较长的Toast
     */
    public void showLongToast(String text) {
        if (toast == null)
            toast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
        else
            toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(text);
        toast.show();
    }

    /**
     * 只有Debug模式下才会弹出的toast条，可以在开发阶段随意使用的toast，即使发布后未删除这个toast也是没关系的。
     */
    public void showDebugToast(String text) {
        if (BuildConfig.DEBUG)
            showLongToast(text);
    }

    /**
     * 获取项目资源的各种类型
     */
    public String[] getResourceStringArray(int resId) {
        return resources.getStringArray(resId);
    }

    public String getResourceString(int resId) {
        return resources.getString(resId);
    }

    public Drawable getResourceDrawable(int resId) {
        return resources.getDrawable(resId);
    }

    public int getResourceColor(int resId) {
        return resources.getColor(resId);
    }

    public int getResourceInteger(int resId) {
        return resources.getInteger(resId);
    }

    public float getResourceDimension(int resId) {
        return resources.getDimension(resId);
    }

    /**
     * ------------------------------------------------------------------------------------
     */

    /**
     * 在父布局中添加view
     *
     * @param resource 需要添加的布局id
     * @param root     给这个布局一个容器，规定大小和style
     */
    public View inflate(int resource, ViewGroup root) {
        return inflater.inflate(resource, root);
    }

    /**
     * 同上，第三个参数一般设置成false，设置成true会报错
     */
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return inflater.inflate(resource, root, attachToRoot);
    }

    /**EventBus工具类的四种方法
     1、onEvent
     2、onEventMainThread
     3、onEventBackgroundThread
     4、onEventAsync
     */

    /**
     * 解释：
     * <p/>
     * onEvent:如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，也就是说发布事件和接收事件线程在同一个线程。使用这个方法时，在onEvent方法中不能执行耗时操作，如果执行耗时操作容易导致事件分发延迟。
     * onEventMainThread:如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，这个在Android中是非常有用的，因为在Android中只能在UI线程中跟新UI，所以在onEvnetMainThread方法中是不能执行耗时操作的。
     * onEventBackground:如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，那么onEventBackground就会在子线程中运行，如果事件本来就是子线程中发布出来的，那么onEventBackground函数直接在该子线程中执行。
     * onEventAsync：如果使用onEventAsync函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync.
     */
    public void onEventMainThread(Object obj) {

    }

    /**
     * 注册eventbus
     */
    public void registerEventBus(Object o) {
        eventBus.register(o);
    }

    /**
     * 反注册eventbus
     */
    public void unregisterEventBus(Object o) {
        eventBus.unregister(o);
    }

    /**
     * 发送eventbus的消息
     */
    public void postEventBus(Object o) {
        eventBus.post(o);
    }

    /**
     * 判断该object是否注册eventbus
     */
    public boolean isRegistered(Object o) {
        return eventBus.isRegistered(o);
    }

    public boolean hasSubscriberForEvent(Class<?> cls) {
        return eventBus.hasSubscriberForEvent(cls);
    }

    /**
     * 获得当前activity的实例
     */
    public BaseActivity getActivity() {
        return activity;
    }

    /**
     * 监听键盘，哪个类需要复制粘贴
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            onKeyDown.getBack(true);
//        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
//            onKeyDown.getMenu(true);
//        } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//            onKeyDown.getSearch(true);
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            onKeyDown.getVolumeDown(true);
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            onKeyDown.getVolumeUP(true);
//        }
//        return false;
//    }

    /**
     * 释放activity
     * 如果该activity被内存泄漏则需要手动释放该资源
     */
    protected void releaseActivity() {
        activity = null;
    }

    /**
     * 退出程序
     */
    protected void finishAll() {
        ActivityCollector.finishAll();
    }
}
