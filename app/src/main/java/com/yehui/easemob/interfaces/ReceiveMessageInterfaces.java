package com.yehui.easemob.interfaces;

import com.easemob.chat.EMMessage;

/**
 * Created by Luhao on 2016/3/7.
 * 收消息状态监听帮助类
 */
public interface ReceiveMessageInterfaces {

    /**
     * 接收所有的event事件：
     */
    void allEvent();

    /**
     * 有选择性的接收某些类型event事件：（该方法接收新消息）
     * <p>
     * EMNotifierEvent.Event.EventDeliveryAck;//已发送回执event注册
     * EMNotifierEvent.Event.EventNewCMDMessage;//接收透传event注册
     * EMNotifierEvent.Event.EventNewMessage;//接收新消息event注册
     * EMNotifierEvent.Event.EventOfflineMessage;//接收离线消息event注册
     * EMNotifierEvent.Event.EventReadAck;//已读回执event注册
     * EMNotifierEvent.Event.EventConversationListChanged;//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
     */
    void eventNewMessage();

    /**
     * 解除监听事件
     * <p>
     * 如果不想收到回调，则执行解除监听事件
     */
    void unregisterEvent();


    /**
     * 指定未读消息数清零(指定会话消息未读数清零)
     *
     * @param usernameOrGroupid 用户名名和群id
     */
    void markAllMessagesAsRead(String usernameOrGroupid);

    /**
     * 所有未读消息数清零
     */
    void markAllConversationsAsRead();


    /**
     * 清空会话聊天记录
     * 适用于清屏
     *
     * @param usernameOrGroupid
     * @return
     */
    boolean clearConversation(String usernameOrGroupid);

    /**
     * 删除单个聊天记录
     * 适用于删除对user的聊天记录
     *
     * @param usernameOrGroupid
     */
    boolean deleteConversation(String usernameOrGroupid);

    /**
     * 删除当前会话的某条记录
     *
     * @param usernameOrGroupid
     * @param message
     */
    void removeMessageById(String usernameOrGroupid, EMMessage message);

    /**
     * 删除所有聊天记录
     */
    void deleteAllConversation();
}
