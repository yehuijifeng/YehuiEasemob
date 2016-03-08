package com.yehui.easemob.interfaces;

import android.app.Activity;

/**
 * Created by Luhao on 2016/3/4.
 * 服务器返回状态接口
 */
public interface ServerStatusInterfaces {

    /**
     * 注册
     */
    void createAccountOnServer(String username, String pwd);

    /**
     * 登陆聊天服务器
     */
    void login(String userName, String password);

    /**
     * 登陆聊天服务器,用于单点登录
     */
    void loginByTesting(String userName, String password);

    /**
     * 登出
     */
    void outlogin(Activity activity);
}
