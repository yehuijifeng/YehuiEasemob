package com.yehui.easemob.contants;

/**
 * Created by Luhao on 2016/4/25.
 */
public class GroupContant {
    public static final int GROUP_REMOVE_USER = 300;//当前用户被管理员移除出群聊
    public static final int GROUP_RECEIVE_ADD = GROUP_REMOVE_USER + 1;//收到加入群聊的邀请
    public static final int GROUP_RECEIVE_ADD_NO = GROUP_RECEIVE_ADD + 1;//群聊邀请被拒绝
    public static final int GROUP_RECEIVE_ADD_YES = GROUP_RECEIVE_ADD_NO + 1;//群聊邀请被接受
    public static final int GROUP_DELETE = GROUP_RECEIVE_ADD_YES + 1;//群聊被创建者解散
    public static final int GROUP_REMOVE_USER_SET = GROUP_DELETE + 1;//收到加群申请
    public static final int GROUP_REMOVE_USER_SET_YES = GROUP_REMOVE_USER_SET + 1;//加群申请被同意
    public static final int GROUP_REMOVE_USER_SET_NO = GROUP_REMOVE_USER_SET_YES + 1;// 加群申请被拒绝

}
