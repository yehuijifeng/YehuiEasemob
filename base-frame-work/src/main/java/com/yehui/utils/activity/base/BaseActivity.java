package com.yehui.utils.activity.base;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yehui.utils.R;
import com.yehui.utils.application.ActivityCollector;
import com.yehui.utils.contacts.SettingContact;
import com.yehui.utils.http.action.RequestAction;
import com.yehui.utils.http.bean.DownloadFileBean;
import com.yehui.utils.http.bean.UploadFileBean;
import com.yehui.utils.http.request.ResponseComplete;
import com.yehui.utils.http.request.ResponseFailure;
import com.yehui.utils.http.request.ResponseResult;
import com.yehui.utils.http.request.ResponseSuccess;
import com.yehui.utils.utils.SharedPreferencesUtil;
import com.yehui.utils.view.loadingview.MyLoadingView;
import com.yehui.utils.view.titleview.MyTitleView;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yehuijifeng
 * on 2015/11/25.
 * 最基础的Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 子类需要实现的抽象类
     */
    /**
     * 布局id,传入了ToolBarActivity中重写的setContentView中，以便于添加toolbar
     */
    protected abstract void setContentView();

    /**
     * 初始化title
     */
    protected abstract String setTitleText();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化data
     */
    protected abstract void initData();

    /**
     * -------------------------------------------------------------------------------------
     */
    /**
     * 根布局
     */
    protected ViewGroup root;

    /**
     * 获取屏幕宽高
     */
    protected DisplayMetrics outMetrics;

    /**
     * gson,解析json数据或者类转json时能用到
     */
    protected Gson gson;

    /**
     * BaseActivity和BaseFragment的代理类，不用管。
     */
    public BaseHelper helper;

    /**
     * eventBus工具类
     */
    protected EventBus eventBus;

    /**
     * imageloader工具类的初始化
     */
    protected ImageLoader imageLoader;

    /**
     * activity的title
     */
    protected MyTitleView mTitleView;

    /**
     * 父布局填充
     */
    protected LayoutInflater inflater;

    /**
     * 本地广播接收者
     * 例子：
     * （接收）：
     * YehuiBroadcast yehuiBroadcast = new YehuiBroadcast();
     * IntentFilter filter = new IntentFilter("yehui.utils.broadcast");
     * localBroadcastManager.registerReceiver(yehuiBroadcast, filter);
     * 广播接收器：
     * class YehuiBroadcast extends BroadcastReceiver{}
     * 实现的方法：
     *
     * @Override public void onReceive(Context context, Intent intent) {}
     * <p/>
     * （发送）：
     * Intent intent = new Intent("yehui.utils.broadcast");
     * intent.putExtra("yehuiBroadcase","这是广播接收者传递过来的消息");
     * localBroadcastManager.sendBroadcast(intent);
     */
    protected LocalBroadcastManager localBroadcastManager;

    /**
     * title的类型，枚举类型,初始化给默认标题类型
     */
    protected MyTitleView.TitleMode titleMode;

    /**
     * loading页
     */
    private MyLoadingView myLoadingView;

    /**
     * 获得当前title类型
     */
    public MyTitleView.TitleMode getTitleMode() {
        return titleMode;
    }

    /**
     * 更改当前title类型
     */
    protected void setTitleMode(MyTitleView.TitleMode titleMode) {
        this.titleMode = titleMode;
        addTitleMode();
    }

    /**
     * 小数点的确定位数
     */
    protected DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
     * sharedPreferences键值对的存储工具
     */
    protected SharedPreferencesUtil sharedPreferences;


    protected SharedPreferencesUtil getSharedPreferences() {
        if (sharedPreferences == null)
            return sharedPreferences = new SharedPreferencesUtil(this, SettingContact.YEHUI_SHARE);
        return sharedPreferences;
    }

    protected SharedPreferencesUtil getSharedPreferences(String name) {
        if (sharedPreferences == null)
            return sharedPreferences = new SharedPreferencesUtil(this, name);
        return sharedPreferences;
    }

    protected void setSharedPreferences(String name) {
        this.sharedPreferences = new SharedPreferencesUtil(this, name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //title,功能操作模式叠加
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //横屏：
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //竖屏：
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView();
        ActivityCollector.addActivity(this);
        initProperties();
        initView();
        initData();
    }

    /**
     * baseactivity的初始化
     */
    private void initProperties() {
        helper = new BaseHelper(this);
        getSharedPreferences();
        root = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        /**
         * 注意：
         * 如果activity布局中没有加入：  <include layout="@layout/layout_my_loading" />
         * myLoadingView则为null
         * 子类调用myLoadingView中的方法则会报NullException
         */
        myLoadingView = (MyLoadingView) findViewById(R.id.my_loading_layout);
        eventBus = helper.eventBus;
        mTitleView = (MyTitleView) findViewById(R.id.my_title_view);
        if (mTitleView != null) {
            if (setCustomToolbar() != null) {
                onCreateCustomToolBar(setCustomToolbar());
            } else {
                setTitleMode(MyTitleView.TitleMode.NORMAL);
                mTitleView.setTitleMode(getTitleMode());
                mTitleView.setTitleText(setTitleText() + "");
            }
        }
        outMetrics = helper.outMetrics;
        imageLoader = ImageLoader.getInstance();
        inflater = this.getLayoutInflater();
        if (!helper.isRegistered(this)) {
            helper.registerEventBus(this);
        }
    }

    /**
     * 显示正在加载中
     */
    protected void loadingShow() {
        if (myLoadingView == null) return;
        myLoadingView.loadingVISIBLE();
    }

    /**
     * 隐藏正在加载中
     */
    protected void loadingClose() {
        if (myLoadingView == null) return;
        myLoadingView.loadingGONE();
    }

    /**
     * 加载失败的点击事件
     */
    protected void loadingFailOnClick(View.OnClickListener l) {
        if (myLoadingView == null) return;
        myLoadingView.loadingFailOnClick(l);
    }

    /**
     * 加载失败的提示语
     */
    protected void loadingFail(String textStr, String btnStr) {
        if (myLoadingView == null) return;
        myLoadingView.loadingFail(textStr, btnStr);
    }

    protected void loadingFail(String textStr) {
        myLoadingView.loadingFail(textStr, null);
    }

    protected void loadingFail() {
        myLoadingView.loadingFail(null, null);
    }

    /**
     * 加载出空数据的时候
     */
    protected void loadingEmpty(String textStr, String btnStr) {
        if (myLoadingView == null) return;
        myLoadingView.loadingEmpty(textStr, btnStr);
    }

    protected void loadingEmpty(String textStr) {
        loadingEmpty(textStr, "");
    }

    protected void loadingEmpty() {
        loadingEmpty("", "");
    }

    /**
     * 正在加载中
     */
    protected void loadingView(String textStr) {
        if (myLoadingView == null) return;
        myLoadingView.loadingView(textStr);
    }

    protected void loadingView() {
        myLoadingView.loadingView("");
    }

    /**
     * 获得gson实例
     *
     * @return
     */
    protected Gson getGson() {
        return gson = new Gson();
    }

    /**
     * 获得本地广播接收者实例
     */
    protected LocalBroadcastManager getLBM() {
        return localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    /**
     * 从资源中获取Bitmap
     */
    protected Bitmap getBitmapByImageID(int drawableId) {
        try {
            return BitmapFactory.decodeResource(getResources(), drawableId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置标题类型，本类调用，外部不可改变
     */
    private void addTitleMode() {
        if (mTitleView != null)
            mTitleView.setTitleMode(getTitleMode());
    }

    /**
     * @param view 创建自定义的toolbar
     */
    protected void onCreateCustomToolBar(View view) {
        mTitleView.setNewView(view);
    }

    /**
     * 供子类调用的title,自定义toolbar
     */
    protected View setCustomToolbar() {
        return null;
    }


    /**
     * EventBus工具类接收消息的特定类，需要注册后才能使用
     */

    /**
     * get请求异步请求
     *
     * @param action action的枚举
     */
    public void sendGetRequest(RequestAction action) {
        helper.sendGetRequest(action);
    }

    /**
     * get请求线程阻塞
     */
    public void sendGetInstanceRequest(RequestAction action) {
        helper.sendGetInstanceRequest(action);
    }

    /**
     * post请求
     *
     * @param action action的枚举
     */
    public void sendPostRequest(RequestAction action) {
        helper.sendPostRequest(action);
    }

    public void sendPostInstanceRequest(RequestAction action) {
        helper.sendPostInstanceRequest(action);
    }

    /**
     * post传文件请求
     *
     * @param files  文件组
     * @param action action的枚举
     */
    public void sendPostAddFileRequest(File[] files, RequestAction action) {
        helper.sendPostAddFileRequest(files, action);
    }

    /**
     * 下载大文件
     *
     * @param url 下载的地址
     */
    public void sendDownloadFile(String url) {
        helper.sendDownloadFile(url);
    }

    public void sendDownloadFile(RequestAction action) {
        helper.sendDownloadFile(action);
    }

    /**
     * 网络请求设置超时时间
     */
    public void setTimeOut(int value) {
        helper.setTimeOut(value);
    }

    /**
     * 断开/启动，所有正在进行的请求
     *
     * @param bl true断开，false开启
     */
    public void cancelAllRequests(boolean bl) {
        helper.cancelAllRequests(bl);
    }

    /**
     * 断开某个请求
     *
     * @param action action的枚举
     */
    public void cancelByActionRequests(RequestAction action) {
        helper.cancelByActionRequests(action);
    }

    /**
     * 需要实现eventbus中的一个方法，不然报错
     */

    /**
     * 事件总线的方法，此方法为接收请求结果的方法。
     *
     * @param result 返回结果
     */
    public void onEventMainThread(ResponseResult result) {
        if (result instanceof ResponseSuccess) {
            onRequestSuccess((ResponseSuccess) result);
        } else if (result instanceof ResponseFailure) {
            onRequestFailure((ResponseFailure) result);
        } else if (result instanceof ResponseComplete) {
            onRequestCompleted((ResponseComplete) result);
        }
    }

    /**
     * 接受下载文件的进度显示
     */
    public void onEventMainThread(DownloadFileBean downloadFileBean) {
        onRequestDownFile(downloadFileBean);
    }

    /**
     * 接受上传文件的进度显示
     */
    public void onEventMainThread(UploadFileBean uploadFileBean) {
        onRequestUploadFile(uploadFileBean);
    }

    /**EventBus工具类的四种方法
     1、onEvent
     2、onEventMainThread
     3、onEventBackgroundThread
     4、onEventAsync
     */

    /**
     * 解释：
     * <p>
     * onEvent:如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，也就是说发布事件和接收事件线程在同一个线程。使用这个方法时，在onEvent方法中不能执行耗时操作，如果执行耗时操作容易导致事件分发延迟。
     * onEventMainThread:如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，这个在Android中是非常有用的，因为在Android中只能在UI线程中跟新UI，所以在onEvnetMainThread方法中是不能执行耗时操作的。
     * onEventBackgroundThread:如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，那么onEventBackground就会在子线程中运行，如果事件本来就是子线程中发布出来的，那么onEventBackground函数直接在该子线程中执行。
     * onEventAsync：如果使用onEventAsync函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync.
     */
    /**
     * 注册事件总线的方法。
     */
    public void registerEventBus(Object o) {
        helper.registerEventBus(o);
    }

    /**
     * 反注册事件总线的方法
     */
    public void unregisterEventBus(Object o) {
        helper.unregisterEventBus(o);
    }

    /**
     * 发送事件消息的方法，所有注册了事件总线并且onEventMainThread等4个方法中的参数类型为传入该方法参数的类型时，将会受到该方法发送的通知。
     */
    public void postEventBus(Object o) {
        helper.postEventBus(o);
    }

    /**
     * 判断某个类是否已注册事件总线
     */
    public boolean isRegistered(Object o) {
        return helper.isRegistered(o);
    }


    /**
     * 根据app的实际情况是否将这三个方法设置成抽象方法（如果app全程每个页面都有请求网络，则设置成抽象类，子类必须实现，简化工作）
     */
    /**
     * 请求成功的回调，子类应该实现此方法做相应的处理
     */
    protected void onRequestSuccess(ResponseSuccess success) {
    }

    /**
     * 请求成功的回调，子类应该实现此方法做相应的处理
     */
    protected void onRequestFailure(ResponseFailure failure) {
    }

    /**
     * 请求成功的回调，子类应该实现此方法做相应的处理
     */
    protected void onRequestCompleted(ResponseComplete complete) {
    }

    /**
     * 下载文件的进度回调
     */
    protected void onRequestDownFile(DownloadFileBean downloadFileBean) {
    }

    /**
     * 上传文件的进度回调
     */
    protected void onRequestUploadFile(UploadFileBean uploadFileBean) {
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

    /**
     * 获取屏幕高度
     */
    protected int getWindowHeight() {
        return helper.getWindowHeight();
    }

    /**
     * 获取屏幕宽度
     */
    protected int getWindowWidth() {
        return helper.getWindowWidth();
    }

    /**
     * 获取上一个Activity传过来的Bundle
     */
    protected Bundle getBundle() {
        return helper.getBundle();
    }

    /**
     * 获取上一个Activity传过来的字符串集合
     */
    protected List<String> getStringArrayList(String key) {
        return helper.getStringArrayList(key);
    }

    /**
     * 获取上一个Activity传过来的int值
     *
     * @param key          键
     * @param defaultValue 默认值
     */
    protected int getInt(String key, int defaultValue) {
        return helper.getInt(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的String值
     */
    protected String getString(String key, String defaultValue) {
        return helper.getString(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的布尔值
     */
    protected boolean getBoolean(String key, boolean defaultValue) {
        return helper.getBoolean(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的double值
     */
    protected double getDouble(String key, double defaultValue) {
        return helper.getDouble(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的float值
     */
    protected float getFloat(String key, float defaultValue) {
        return helper.getFloat(key, defaultValue);
    }

    /**
     * 获取上一个Activity传过来的实现了Parcelable接口的对象
     */
    protected Parcelable getParcelable(String key) {
        return helper.getParcelable(key);
    }

    /**
     * 获取上一个Activity传过来的实现了Parcelable接口的对象的集合
     */
    protected ArrayList<? extends Parcelable> getParcelableList(String key) {
        return helper.getParcelableList(key);
    }

    /**
     * 获取资源文件下的字符串数组
     *
     * @param resId 资源ID 示例:R.array.lab_payment_methods
     */
    public String[] getResourceStringArray(int resId) {
        return helper.getResourceStringArray(resId);
    }

    /**
     * 获取资源文件下的字符串
     *
     * @param resId 资源ID。示例:R.string.xxx
     */
    public String getResourceString(int resId) {
        return helper.getResourceString(resId);
    }

    /**
     * 获取资源文件下的Drawable对象
     *
     * @param resId 资源ID. 示例:R.drawable.xxx
     * @return Drawable对象
     */
    public Drawable getResourceDrawable(int resId) {
        return helper.getResourceDrawable(resId);
    }

    /**
     * 获取资源文件下的颜色值
     *
     * @param resId 资源ID。 示例:R.color.xxx
     * @return
     */
    public int getResourceColor(int resId) {
        return helper.getResourceColor(resId);
    }

    /**
     * 获取资源文件下的int值
     *
     * @param resId 资源ID。 示例:R.integer.xxx
     * @return
     */
    public int getResourceInteger(int resId) {
        return helper.getResourceInteger(resId);
    }

    /**
     * 获取资源文件下的尺寸值
     *
     * @param resId 资源ID。 示例:R.dimen.xxx
     * @return
     */
    public float getResourceDimension(int resId) {
        return helper.getResourceDimension(resId);
    }

    /**
     * 在父容器中加入view
     * root设置这个view的填充器
     */
    public View inflate(int resource, ViewGroup root) {
        return helper.inflate(resource, root);
    }

    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return helper.inflate(resource, root, attachToRoot);
    }

    /**
     * 显式跳转Activity的方法(不带任何参数)
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
     */
    public void showShortToast(String text) {
        helper.showShortToast(text);
    }

    /**
     * 弹出时间较长的Toast
     */
    public void showLongToast(String text) {
        helper.showLongToast(text);
    }

    /**
     * 只有Debug模式下才会弹出的toast条，可以在开发阶段随意使用的toast，即使发布后未删除这个toast也是没关系的。
     */
    public void showDebugToast(String text) {
        helper.showDebugToast(text);
    }

    /**
     * 退出程序
     */
    public void finishAll() {
        helper.finishAll();
        //overridePendingTransition(0, R.anim.activity_exit_anim);
    }


    private boolean isFastClick = true;

    protected boolean isFastClick() {
        return isFastClick;
    }

    /**
     * 供子类调用的方法，是否需要快速点击回避事件
     *
     * @param isFastClick
     */
    protected void setIsFastClick(boolean isFastClick) {
        this.isFastClick = isFastClick;
    }

    /**
     * 快速点击回避
     * 防止快速点击重复页面
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && isFastClick()) {
            if (isFastClick(300)) return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private long lastTime;

    private boolean isFastClick(int limit) {
        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime - lastTime;
        lastTime = currentTime;
        return timeDiff < limit;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper.isRegistered(this)) {
            helper.unregisterEventBus(this);
        }
        helper.releaseActivity();
        helper = null;
        ActivityCollector.removeActivity(this);
    }

}
