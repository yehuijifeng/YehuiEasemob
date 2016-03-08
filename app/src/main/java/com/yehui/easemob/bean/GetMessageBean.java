package com.yehui.easemob.bean;

/**
 * Created by Luhao on 2016/3/8.
 */
public class GetMessageBean {
    private int getMsgCode;
    private String getMsgErrorStr;
    private int getMsgErrorInt;
    private boolean isSend;
    private String userName;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGetMsgCode() {
        return getMsgCode;
    }

    public void setGetMsgCode(int getMsgCode) {
        this.getMsgCode = getMsgCode;
    }

    public String getGetMsgErrorStr() {
        return getMsgErrorStr;
    }

    public void setGetMsgErrorStr(String getMsgErrorStr) {
        this.getMsgErrorStr = getMsgErrorStr;
    }

    public int getGetMsgErrorInt() {
        return getMsgErrorInt;
    }

    public void setGetMsgErrorInt(int getMsgErrorInt) {
        this.getMsgErrorInt = getMsgErrorInt;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
