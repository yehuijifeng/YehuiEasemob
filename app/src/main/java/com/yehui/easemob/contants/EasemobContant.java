package com.yehui.easemob.contants;

/**
 * Created by Luhao on 2016/3/3.
 * 好友体系状态码管理
 */
public class EasemobContant {

    public static final int onContactAgreed = 1;//好友请求被同意
    public static final int onContactRefused = onContactAgreed + 1;//好友请求被拒绝
    public static final int onContactInvited = onContactRefused + 1;//收到好友邀请
    public static final int onContactDeleted = onContactInvited + 1;//好友将本人删除
    public static final int onContactAdded = onContactDeleted + 1;//好友添加了本人
    public static final int acceptFriendRequest = onContactAdded + 1;//同意好友的请求
    public static final int refuseInvitation = acceptFriendRequest + 1;//拒绝好友的添加
    public static final int addUserToBlackList = refuseInvitation + 1;//添加好友到黑名单
    public static final int deleteUserFromBlackList = addUserToBlackList + 1;//好友移除黑名单
    public static final int deleteContact = deleteUserFromBlackList + 1;//删除好友
    public static final int addContact = deleteContact + 1;//添加好友成功
    public static final int createAccountOnServer = addContact + 1;//注册
    public static final int userLogin = createAccountOnServer + 1;//登陆
    public static final int loginTesting = userLogin + 1;//单点登录
    public static final int getFriendList = loginTesting + 1;//获取好友列表
    public static final int getBlackListUsernames = getFriendList + 1;//获取黑名单列表
    public static final int loginOut = getBlackListUsernames + 1;//登出
}
