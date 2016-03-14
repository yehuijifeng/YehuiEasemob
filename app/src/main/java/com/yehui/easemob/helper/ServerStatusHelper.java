package com.yehui.easemob.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.interfaces.ServerStatusInterfaces;
import com.yehui.utils.utils.LogUtil;

import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao
 * on 2016/2/21.
 * 环信服务器状态监听帮助类
 */
public class ServerStatusHelper implements ServerStatusInterfaces {

    private static volatile ServerStatusHelper instance = null;


    private ServerStatusHelper() {
        eventBus = EventBus.getDefault();
        serverBean = new ServerBean();
    }

    public synchronized static ServerStatusHelper getInstance() {
        if (instance == null) {
            synchronized (ServerStatusHelper.class) {
                if (instance == null) {
                    instance = new ServerStatusHelper();
                }
            }
        }
        return instance;
    }

    private EventBus eventBus;
    private Context appContext;
    private ServerBean serverBean;
    private Thread thread;

    private void initServer() {
        serverBean = new ServerBean();
    }

    /**
     * 初始化SDK
     * <p/>
     * 要求在application的oncreate方法中做初始化
     */
    public void initEasemob(Context applicationContext) {
        appContext = applicationContext;
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase("com.yehui.easemob")) {
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMChat.getInstance().init(applicationContext);
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //建议初始化sdk的时候设置成每个会话默认load一条消息，节省加载会话的时间
        options.setNumberOfMessagesLoaded(1);
    }

    /**
     * 获取 processAppName
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
//                     Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
//                     info.processName +"  Label: "+c.toString());
                    processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * debugMode == true 时为打开，sdk 会在log里输入调试信息
     *
     * @param bl 在做代码混淆的时候需要设置成false
     */
    public void setDebugMode(boolean bl) {
        EMChat.getInstance().setDebugMode(bl);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
    }

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMChat.getInstance().isLoggedIn();
    }

    /**
     * 注册
     * 注意事项：
     * 注册用户名会自动转为小写字母
     * 注册模式分两种，开放注册和授权注册。只有开放注册时，才可以客户端注册。
     */
    @Override
    public void createAccountOnServer(final String username, final String pwd) {
        //注册失败
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initServer();
                try {
                    // 调用sdk注册方法
                    EMChatManager.getInstance().createAccountOnServer(username, pwd);
                    serverBean.setOperation(true);
                } catch (final EaseMobException e) {
                    serverBean.setOperation(false);
                    int errorCode = e.getErrorCode();
                    String errorStr;
                    if (errorCode == EMError.NONETWORK_ERROR) {
                        errorStr = "网络异常，请检查网络！";
                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                        errorStr = "用户已存在！";
                    } else if (errorCode == EMError.UNAUTHORIZED) {
                        errorStr = "注册失败，无权限！";
                    } else {
                        errorStr = "注册失败: " + e.getMessage();
                    }
                    serverBean.setStatusMsg("错误代码：" + errorCode + errorStr);
                } finally {
                    serverBean.setStatusCode(EasemobContant.createAccountOnServer);
                    eventBus.post(serverBean);
                }
            }
        });
        thread.start();
    }

    /**
     * 登陆聊天服务器
     */
    @Override
    public void login(final String userName, final String password) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initServer();
                EMChatManager.getInstance().login(userName, password, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMGroupManager.getInstance().loadAllGroups();//加载所有群组
                        EMChatManager.getInstance().loadAllConversations();//加载所有对话
                        LogUtil.d("登陆聊天服务器成功！");
                        UserInfoBean userInfoBean = new UserInfoBean();
                        userInfoBean.setUserName(userName);
                        userInfoBean.setUserPwd(password);
                        serverBean.setOperation(true);
                        serverBean.setStatusCode(EasemobContant.userLogin);
                        serverBean.setUserInfoBean(userInfoBean);
                        eventBus.post(serverBean);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        LogUtil.d("正在登陆聊天服务器");
                    }

                    @Override
                    public void onError(int code, String messages) {
                        LogUtil.d("登陆聊天服务器失败！");
                        serverBean.setOperation(false);
                        serverBean.setStatusCode(EasemobContant.userLogin);
                        serverBean.setStatusMsg("登陆聊天服务器失败!");
                        eventBus.post(serverBean);
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * 登陆聊天服务器,用于单点登录
     */
    @Override
    public void loginByTesting(final String userName, final String password) {

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                initServer();
                EMChatManager.getInstance().login(userName, password, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMGroupManager.getInstance().loadAllGroups();//加载所有群组
                        EMChatManager.getInstance().loadAllConversations();//加载所有对话
                        LogUtil.d("登陆聊天服务器成功！");
                        serverBean.setOperation(true);
                        serverBean.setStatusCode(EasemobContant.loginTesting);
                        eventBus.post(serverBean);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        LogUtil.d("正在登陆聊天服务器");
                    }

                    @Override
                    public void onError(int code, String messages) {
                        LogUtil.d("登陆聊天服务器失败！");
                        serverBean.setOperation(false);
                        serverBean.setStatusCode(EasemobContant.loginTesting);
                        serverBean.setStatusMsg("登陆聊天服务器失败!");
                        eventBus.post(serverBean);
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * 自动登录
     * <p/>
     * 自动登录在以下几种情况下会被取消
     * <p/>
     * 用户调用了SDK的登出动作;
     * <p/>
     * 用户在别的设备上更改了密码, 导致此设备上自动登陆失败;
     * <p/>
     * 用户的账号被从服务器端删除;
     * <p/>
     * 用户从另一个设备登录，把当前设备上登陆的用户踢出
     */
    public void setAutoLogin(boolean bl) {
        EMChat.getInstance().setAutoLogin(bl);
    }

    /**
     * 重连
     * 当掉线时，Android SDK会自动重连，无需进行任何操作。
     */
    public void addConnectionListener(EMConnectionListener emConnectionListener) {
        //注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener(emConnectionListener));
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        private EMConnectionListener emConnectionListener;

        private MyConnectionListener(EMConnectionListener emConnectionListener) {
            this.emConnectionListener = emConnectionListener;
        }

        @Override
        public void onConnected() {
            //已连接到服务器
            emConnectionListener.onConnected();
        }

        @Override
        public void onDisconnected(final int error) {
            emConnectionListener.onDisconnected(error);
        }
    }

    /**
     * 登出
     * false unbindDeviceToken
     * 是否解绑设备token(使用GCM才有)
     * callback
     * callback回调状态
     */
    @Override
    public void outlogin(final Activity activity) {
        initServer();
        /**此方法为同步方法*/
        //EMChatManager.getInstance().logout();
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**此方法为异步方法*/
                EMChatManager.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        serverBean.setOperation(true);
                        serverBean.setStatusCode(EasemobContant.loginOut);
                        serverBean.setStatusMsg("退出登录成功");
                        serverBean.setActivity(activity);
                        eventBus.post(serverBean);
                    }

                    @Override
                    public void onError(int i, String s) {
                        serverBean.setOperation(false);
                        serverBean.setStatusCode(EasemobContant.loginOut);
                        serverBean.setStatusMsg(s);
                        serverBean.setStatusMsg("退出登录失败");
                        serverBean.setActivity(activity);
                        eventBus.post(serverBean);
                    }

                    @Override
                    public void onProgress(int i, String s) {
                    }
                });
            }
        }).start();
    }

    /**
     * 监听服务器链接状态
     */
    public void getConnectionStatus() {
        initServer();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerStatusHelper.getInstance().addConnectionListener(new EMConnectionListener() {
                    @Override
                    public void onConnected() {
                        serverBean.setStatusCode(EMError.NO_ERROR);//0
                        serverBean.setStatusMsg("已连接到服务器");
                        eventBus.post(serverBean);
                    }

                    @Override
                    public void onDisconnected(final int error) {
                        if (error == EMError.USER_REMOVED) {
                            // 显示帐号已经被移除
                            serverBean.setStatusCode(EMError.USER_REMOVED);//-1023
                            serverBean.setStatusMsg("帐号已经被移除");
                        } else if (error == EMError.CONNECTION_CONFLICT) {
                            // 显示帐号在其他设备登陆
                            serverBean.setStatusCode(EMError.CONNECTION_CONFLICT);//-1014
                            serverBean.setStatusMsg("你的帐号在其他设备登陆");
                        } else if (error == EMError.UNABLE_CONNECT_TO_SERVER) {
                            serverBean.setStatusCode(EMError.UNABLE_CONNECT_TO_SERVER);//-1003
                            //连接不到聊天服务器
                            serverBean.setStatusMsg("连接不到聊天服务器");
                        } else if (error == EMError.NONETWORK_ERROR) {
                            //当前网络不可用，请检查网络设置
                            serverBean.setStatusCode(EMError.NONETWORK_ERROR);// -1001
                            serverBean.setStatusMsg("当前网络不可用，请检查网络设置");
                        }
                        eventBus.post(serverBean);
                    }
                });
            }
        });
        thread.start();
    }
}
