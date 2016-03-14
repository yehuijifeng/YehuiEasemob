package com.yehui.easemob.helper;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.interfaces.ReceiveMessageInterfaces;
import com.yehui.utils.utils.LogUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao
 * on 2016/2/22.
 * 单聊帮助类
 * 收文字,语音,图片,位置
 * 接收消息(单聊/群聊)
 */
public class ReceiveMessageHelper implements ReceiveMessageInterfaces {

    /**
     * 环信推荐用监听事件接收消息
     */
    private ReceiveMessageHelper() {
        messageBean = new MessageBean();
        eventBus = new EventBus();
    }

    private static volatile ReceiveMessageHelper instance = null;

    public synchronized static ReceiveMessageHelper getInstance() {
        if (instance == null) {
            synchronized (ReceiveMessageHelper.class) {
                if (instance == null) {
                    instance = new ReceiveMessageHelper();
                }
            }
        }
        return instance;
    }

    private EMMessage message;
    private MessageBean messageBean;
    private EventBus eventBus;

    private void initSerMsg() {
        messageBean = new MessageBean();
    }

    /**
     * 接收所有的event事件：
     * /**
     * 有选择性的接收某些类型event事件：（该方法接收新消息）
     * EMNotifierEvent.Event.EventDeliveryAck;//已发送回执event注册
     * EMNotifierEvent.Event.EventNewCMDMessage;//接收透传event注册
     * EMNotifierEvent.Event.EventNewMessage;//接收新消息event注册
     * EMNotifierEvent.Event.EventOfflineMessage;//接收离线消息event注册
     * EMNotifierEvent.Event.EventReadAck;//已读回执event注册
     * EMNotifierEvent.Event.EventConversationListChanged;//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
     */
    @Override
    public void allEvent() {
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {
            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message;
                if (event.getData() instanceof EMMessage) {
                    message = (EMMessage) event.getData();
                    initSerMsg();
                    LogUtil.d("receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                } else if (event.getData() instanceof List) {
                    LogUtil.d("received offline messages");
                    List<EMMessage> messages = (List<EMMessage>) event.getData();
                    messageBean.setGetMsgCode(MessageContant.receiveMsgByOffline);
                    messageBean.setMessageList(messages);
                    eventBus.post(messageBean);
                    return;
                } else return;
                switch (event.getEvent()) {
                    case EventNewMessage://接收新消息event注册
                        messageBean.setGetMsgCode(MessageContant.receiveMsgByNew);
                        break;
                    case EventNewCMDMessage://接收透传event注册
                        LogUtil.d("收到透传消息");
                        messageBean.setEmMessage(message);
                        messageBean.setGetMsgCode(MessageContant.receiveMsgByNewCMDM);
                        // 获取消息body
                        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                        final String action = cmdMsgBody.action;// 获取自定义action
                        break;
                    case EventDeliveryAck://已发送回执event注册
                        message.setDelivered(true);
                        messageBean.setGetMsgCode(MessageContant.receiveMsgByDeliveryAck);
                        break;
                    case EventReadAck://已读回执event注册
                        message.setAcked(true);
                        messageBean.setGetMsgCode(MessageContant.receiveMsgByReadAck);
                        break;
                    default:
                        messageBean.setEmMessage(message);
                        //eventBus.post(messageBean);
                        break;
                }

            }
        });
    }


    /**
     * 有选择性的接收
     */
    @Override
    public void eventNewMessage() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub

            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage});//接收新消息
    }

    /**
     * 会话列表改变
     */
    public void EventConversationListChanged() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                messageBean.setGetMsgCode(MessageContant.receiveMsgByListChanged);
                messageBean.setEmMessage(message);
                eventBus.post(messageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventConversationListChanged});//已发送回执
    }

    /**
     * 解除监听事件
     * <p/>
     * 如果不想收到回调，则执行解除监听事件
     */
    @Override
    public void unregisterEvent() {
        initSerMsg();
        EMChatManager.getInstance().unregisterEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub

            }
        });
    }


    /**
     * 指定未读消息数清零(指定会话消息未读数清零)
     *
     * @param usernameOrGroupid 用户名名和群id
     */
    @Override
    public void markAllMessagesAsRead(String usernameOrGroupid) {
        initSerMsg();
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        conversation.markAllMessagesAsRead();
    }

    /**
     * 所有未读消息数清零
     */
    @Override
    public void markAllConversationsAsRead() {
        EMChatManager.getInstance().markAllConversationsAsRead();
    }


    /**
     * 清空会话聊天记录
     * 适用于清屏
     *
     * @param usernameOrGroupid
     * @return
     */
    @Override
    public boolean clearConversation(String usernameOrGroupid) {
        //清空和某个user的聊天记录(包括本地)，不删除conversation这个会话对象
        return EMChatManager.getInstance().clearConversation(usernameOrGroupid);
    }

    /**
     * 删除单个聊天记录
     * 适用于删除对user的聊天记录
     *
     * @param usernameOrGroupid
     */
    @Override
    public boolean deleteConversation(String usernameOrGroupid) {
        //删除和某个user的整个的聊天记录(包括本地)
        return EMChatManager.getInstance().deleteConversation(usernameOrGroupid);
    }

    /**
     * 删除当前会话的某条记录
     *
     * @param usernameOrGroupid
     * @param message
     */
    @Override
    public void removeMessageById(String usernameOrGroupid, EMMessage message) {
        //删除当前会话的某条聊天记录
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        conversation.removeMessage(message.getMsgId());
    }

    /**
     * 删除所有聊天记录
     */
    @Override
    public void deleteAllConversation() {
        //删除所有会话记录(包括本地)
        EMChatManager.getInstance().deleteAllConversation();
    }


}
