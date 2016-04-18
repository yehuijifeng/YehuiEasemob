package com.yehui.easemob.interfaces;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.bean.MessageBean;

import java.util.List;

/**
 * Created by Luhao on 2016/3/7.
 * 发消息状态监听接口
 */
public interface SendMessageInterfaces {


    /**
     * 发送文本消息及表情
     *
     * @param username 接收人
     * @param content  发送的内容
     * @param isReSend 是否是重新发送
     * @param msgId    重新发送的信息id
     * @return
     */
    MessageBean sendConversationByText(String username, String content, boolean isReSend, String msgId);

    /**
     * 发送语音消息
     *
     * @param username 接收人
     * @param filePath 语音文件路径
     * @param length   文件长度，大小
     * @param isReSend 是否是重新发送
     * @param msgId    重新发送的信息id
     */
    MessageBean sendConversationByVoice(String username, String filePath, int length, boolean isReSend, String msgId);

    /**
     * 发送图片消息
     *
     * @param username            接收人
     * @param filePath            图片文件路径
     * @param isSendOriginalImage 是否发送原图
     * @param isReSend            是否是重新发送
     * @param msgId               重新发送的信息id
     */
    MessageBean sendConversationByImage(String username, String filePath, boolean isSendOriginalImage, boolean isReSend, String msgId);

    /**
     * 发送地理位置
     *
     * @param username        接收人
     * @param locationAddress 位置地址
     * @param latitude        维度
     * @param longitude       经度
     * @param isReSend        是否是重新发送
     * @param msgId           重新发送的信息id
     */
    MessageBean sendConversationByLocation(String username, String locationAddress, double latitude, double longitude, boolean isReSend, String msgId);

    /**
     * 发送文件消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     * @param isReSend 是否是重新发送
     * @param msgId    重新发送的信息id
     */
    MessageBean sendConversationByFile(String username, String filePath, boolean isReSend, String msgId);

    /**
     * 发送视频消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     * @param isReSend 是否是重新发送
     * @param msgId    重新发送的信息id
     */
    MessageBean sendConversationByVideo(String username, String filePath, boolean isReSend, String msgId);

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
