package com.yehui.easemob.contants;

/**
 * Created by Luhao on 2016/3/8.
 * 消息常量
 */
public class MessageContant {

    public static final int getMsgByText = 1;//发送文本消息以及表情
    public static final int getMsgByVoice = getMsgByText + 1;//发送语音消息
    public static final int getMsgByImage = getMsgByVoice + 1;//发送图片消息
    public static final int getMsgByLocation = getMsgByImage + 1;//发送地理位置
    public static final int getMsgByFile = getMsgByLocation + 1;//发送文件
    public static final int setMsgByAll = getMsgByFile + 1;//接收所有消息
    public static final int setMsgByNew = setMsgByAll + 1;//接收新消息
    public static final int setMsgByDeliveryAck = setMsgByNew + 1;//已发送
    public static final int setMsgByOffline = setMsgByDeliveryAck + 1;//接收离线消息
    public static final int setMsgByNewCMDM = setMsgByOffline + 1;//透传消息
    public static final int setMsgByReadAck = setMsgByNewCMDM + 1;//已读
    public static final int setMsgByListChanged = setMsgByReadAck + 1;//会话列表改变

}
