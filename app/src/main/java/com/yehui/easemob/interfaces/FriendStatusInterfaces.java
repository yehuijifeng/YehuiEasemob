package com.yehui.easemob.interfaces;

import java.util.List;

/**
 * Created by Luhao on 2016/3/4.
 * 好友体系状态监听接口
 */
public interface FriendStatusInterfaces {

    /**
     * 好友列表
     * 获取好友的状态，1：正常；2，刷新；3，重载
     */
    void getFriendsLists(int status);

    /**
     * 好友请求同意
     *
     * @param username 好友名称
     */
    void onContactAgreeds(String username);

    /**
     * 好友请求被拒绝
     *
     * @param username 好友名称
     */
    void onContactRefuseds(String username);

    /**
     * 收到好友请求
     *
     * @param username
     * @param reason   请求理由
     */
    void onContactInviteds(String username, String reason);


    /**
     * 被好友删除
     *
     * @param usernameList
     */
    void onContactDeleteds(List<String> usernameList);

    /**
     * 被好友添加
     *
     * @param usernameList
     */
    void onContactAddeds(List<String> usernameList);

    /**
     * 添加好友
     *
     * @param toAddUsername
     * @param reason
     */
    void addFriends(String toAddUsername, String reason);

    /**
     * 删除好友
     *
     * @param toAddUsername
     */
    void deleteFriends(String toAddUsername);

    /**
     * 同意好友请求
     *
     * @param username 添加的好友的username
     */
    void acceptFriendRequests(final String username);

    /**
     * 拒绝好友请求
     *
     * @param username 添加的好友的username
     */

    void refuseFriendRequests(final String username);

    /**
     * 获取黑名单列表
     * 从本地获取黑名单中的用户的usernames
     */
    void getBlackListUserNames();

    /**
     * 把用户加入到黑名单
     * 从本地获取黑名单中的用户的usernames
     * 第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
     * 我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
     */
    void addUserToBlackLists(final String username, final boolean isSendMessage);

    /**
     * 把用户从黑名单中移除
     * 从本地获取黑名单中的用户的usernames
     * 第二个参数如果为true，则把用户加入到黑名单后双方发消息时对方都收不到；false,则
     * 我能给黑名单的中用户发消息，但是对方发给我时我是收不到的
     */
    void deleteUserFromBlackLists(final String username);

}
