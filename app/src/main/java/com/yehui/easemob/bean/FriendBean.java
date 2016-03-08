package com.yehui.easemob.bean;

import java.util.List;

/**
 * Created by Luhao on 2016/3/4.
 * 好友体系管理实体类
 */
public class FriendBean {
    private int friendId;
    private String friendName;
    private List<String> friendNameist;
    private int statusCode;
    private String statusMsg;
    private boolean isOperation;

    public List<String> getFriendNameist() {
        return friendNameist;
    }

    public void setFriendNameist(List<String> friendNameist) {
        this.friendNameist = friendNameist;
    }

    public boolean isOperation() {
        return isOperation;
    }

    public void setOperation(boolean operation) {
        isOperation = operation;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
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
}
