package com.yehui.easemob.interfaces;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;

import java.util.List;

/**
 * Created by Luhao on 2016/3/7.
 * 发消息状态监听接口
 */
public interface GetMessageInterfaces {

    /**
     * 发送文本消息及表情
     *
     * @param content  发送的内容
     * @param username 接收人
     */
     void getConversationByText(String username, String content);


    /**
     * 发送语音消息
     *
     * @param username 接收人
     * @param filePath 语音文件路径
     * @param length   文件长度，大小
     */
     void getConversationByVoice(String username, String filePath, int length);

    /**
     * 发送图片消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     */
     void getConversationByImage(String username, String filePath);

    /**
     * 发送地理位置
     *
     * @param username        接收人
     * @param locationAddress 位置地址
     * @param latitude        维度
     * @param longitude       经度
     */
     void getConversationByLocation(String username, String locationAddress, double latitude, double longitude);

    /**
     * 发送图片消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     */
    void getConversationByFile(String username, String filePath);

    /**
     * 获取会话列表
     */
     List<EMConversation> loadConversationList();


    /**
     * 获取聊天记录
     */
     List<EMMessage> getEMMessageList(String usernameOrGroupid);

    /**
     * 获取聊天记录
     */
     List<EMMessage> getEMMessageList(String usernameOrGroupid, String startMsgId, int pagesize);

    /**
     * 获取未读消息数量
     *
     * @param usernameOrGroupid 用户名名和群id
     */
     int getUnreadMsgCount(String usernameOrGroupid);
    /**
     * 获取消息总数
     *
     * @param usernameOrGroupid 用户名名和群id
     */
    int getMsgCount(String usernameOrGroupid);

    /**
     * 设置某条消息为已读
     */
    void getMarkAsRead(String usernameOrGroupid, EMMessage message, boolean markAsRead);
}
