package com.yehui.easemob.bean;

import com.easemob.chat.EMMessage;

import java.util.List;

/**
 * Created by Luhao on 2016/3/8.
 */
public class MessageBean {
    private int getMsgCode;
    private String getMsgErrorStr;
    private int getMsgErrorInt;
    private boolean isSend;
    private String userName;
    private String content;
    private int backStatus;//0,正在发送，-1，发送失败，1成功
    private EMMessage emMessage;
    private List<EMMessage> messageList;//离线消息

    public List<EMMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<EMMessage> messageList) {
        this.messageList = messageList;
    }

    public EMMessage getEmMessage() {
        return emMessage;
    }

    public void setEmMessage(EMMessage emMessage) {
        this.emMessage = emMessage;
    }

    public int getBackStatus() {
        return backStatus;
    }

    public void setBackStatus(int backStatus) {
        this.backStatus = backStatus;
    }

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
