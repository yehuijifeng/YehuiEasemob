package com.yehui.easemob.bean;

import android.app.Activity;

/**
 * Created by Luhao on 2016/3/4.
 * 服务器实体类
 */
public class ServerBean {
    private String serverName;
    private int statusCode;
    private String statusMsg;
    private boolean isOperation;
    private UserInfoBean userInfoBean;
    private Activity activity;//登出专用，记录从哪个页面调用的登出，用于finish（）；

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public boolean isOperation() {
        return isOperation;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }
}
