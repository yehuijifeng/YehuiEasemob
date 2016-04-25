package com.yehui.easemob.contants;

/**
 * Created by Luhao on 2016/3/8.
 * 消息常量
 */
public class MessageContant {

    public static final int sendMsgByText = 100;//发送文本消息以及表情
    public static final int sendMsgByVoice = sendMsgByText + 1;//发送语音消息
    public static final int sendMsgByImage = sendMsgByVoice + 1;//发送图片消息
    public static final int sendMsgByLocation = sendMsgByImage + 1;//发送地理位置
    public static final int sendMsgByFile = sendMsgByLocation + 1;//发送文件
    public static final int sendMsgByVideo = sendMsgByFile + 1;//发送视频
    public static final int receiveMsgByText = sendMsgByVideo + 1;//接收文本消息以及表情
    public static final int receiveMsgByVoice = receiveMsgByText + 1;//接收语音消息
    public static final int receiveMsgByImage = receiveMsgByVoice + 1;//接收图片消息
    public static final int receiveMsgByLocation = receiveMsgByImage + 1;//接收地理位置
    public static final int receiveMsgByFile = receiveMsgByLocation + 1;//接收文件
    public static final int receiveMsgByVideo = receiveMsgByFile + 1;//接收视频
    public static final int receiveMsgByAll = receiveMsgByFile + 1;//接收所有消息
    public static final int receiveMsgByNew = receiveMsgByAll + 1;//接收新消息
    public static final int receiveMsgByDeliveryAck = receiveMsgByNew + 1;//已发送
    public static final int receiveMsgByOffline = receiveMsgByDeliveryAck + 1;//接收离线消息
    public static final int receiveMsgByNewCMDM = receiveMsgByOffline + 1;//透传消息
    public static final int receiveMsgByReadAck = receiveMsgByNewCMDM + 1;//已读
    public static final int receiveMsgByListChanged = receiveMsgByReadAck + 1;//会话列表改变
    public static final int loadMoreData = receiveMsgByListChanged + 1;//加载更多聊天记录
    public static final int sendRevokeMessageBySuccess = loadMoreData + 1;//发送撤回消息的请求成功
    public static final int sendRevokeMessageByFinal = sendRevokeMessageBySuccess + 1;//发送撤回消息的请求失败
    public static final int recriveRevokeMessage = sendRevokeMessageByFinal + 1;//接收到了撤回消息的请求

    public static final String sendRevokeMessage = "easemob_revoke"; // 撤回消息的 key
    public static final String sendRevokeMessageById = "easemob_revoke_message_id"; // 需要撤销的消息的 msg_id key
    public static final String sendRevokeMessageByName = "easemob_revoke_message_name"; // 需要撤销的消息的 msg_name key
    public static final String revokeStr = "[对方撤回一条消息]";
    public static final String revokeStrSend = "对方撤回一条消息";
    public static final String revokeStrSuccess = "成功撤回一条消息";
}
