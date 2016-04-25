package com.yehui.easemob.bean;

import com.easemob.chat.EMGroup;

/**
 * Created by Luhao on 2016/4/25.
 */
public class GroupBean {
    private String groupId;//群id
    private String groupName;//群名称
    private EMGroup emGroup;//群实例
    private String userName;//申请人id
    private String[] newmembers;//邀请进群成员ids
    private String requestStr;//申请理由
    private String inviter;//邀请你加入群的人
    private int groupStatus;//群状态

    public int getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRequestStr() {
        return requestStr;
    }

    public void setRequestStr(String requestStr) {
        this.requestStr = requestStr;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public EMGroup getEmGroup() {
        return emGroup;
    }

    public void setEmGroup(EMGroup emGroup) {
        this.emGroup = emGroup;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getNewmembers() {
        return newmembers;
    }

    public void setNewmembers(String[] newmembers) {
        this.newmembers = newmembers;
    }
}
