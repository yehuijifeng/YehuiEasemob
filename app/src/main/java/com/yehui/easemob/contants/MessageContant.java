package com.yehui.easemob.contants;

/**
 * Created by Luhao on 2016/3/8.
 * 消息常量
 */
public class MessageContant {

    public static final int sendMsgByText = 1;//发送文本消息以及表情
    public static final int sendMsgByVoice = sendMsgByText + 1;//发送语音消息
    public static final int sendMsgByImage = sendMsgByVoice + 1;//发送图片消息
    public static final int sendMsgByLocation = sendMsgByImage + 1;//发送地理位置
    public static final int sendMsgByFile = sendMsgByLocation + 1;//发送文件
    public static final int receiveMsgByText = sendMsgByFile+1;//接收文本消息以及表情
    public static final int receiveMsgByVoice = receiveMsgByText + 1;//接收语音消息
    public static final int receiveMsgByImage = receiveMsgByVoice + 1;//接收图片消息
    public static final int receiveMsgByLocation = receiveMsgByImage + 1;//接收地理位置
    public static final int receiveMsgByFile = receiveMsgByLocation + 1;//接收文件
    public static final int receiveMsgByAll = receiveMsgByFile + 1;//接收所有消息
    public static final int receiveMsgByNew = receiveMsgByAll + 1;//接收新消息
    public static final int receiveMsgByDeliveryAck = receiveMsgByNew + 1;//已发送
    public static final int receiveMsgByOffline = receiveMsgByDeliveryAck + 1;//接收离线消息
    public static final int receiveMsgByNewCMDM = receiveMsgByOffline + 1;//透传消息
    public static final int receiveMsgByReadAck = receiveMsgByNewCMDM + 1;//已读
    public static final int receiveMsgByListChanged = receiveMsgByReadAck + 1;//会话列表改变

}
