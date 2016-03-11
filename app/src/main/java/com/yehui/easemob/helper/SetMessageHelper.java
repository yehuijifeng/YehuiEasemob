package com.yehui.easemob.helper;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.bean.GetMessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.interfaces.SetMessageInterfaces;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao
 * on 2016/2/22.
 * 单聊帮助类
 * 收文字,语音,图片,位置
 * 接收消息(单聊/群聊)
 */
public class SetMessageHelper implements SetMessageInterfaces {

    /**
     * 环信推荐用监听事件接收消息
     */
    private SetMessageHelper() {
        getMessageBean = new GetMessageBean();
        eventBus = new EventBus();
    }

    private static volatile SetMessageHelper instance = null;

    public synchronized static SetMessageHelper getInstance() {
        if (instance == null) {
            synchronized (SetMessageHelper.class) {
                if (instance == null) {
                    instance = new SetMessageHelper();
                }
            }
        }
        return instance;
    }

    private EMMessage message;
    private GetMessageBean getMessageBean;
    private EventBus eventBus;

    private void initSerMsg() {
        getMessageBean = new GetMessageBean();
    }

    /**
     * 接收所有的event事件：
     */
    @Override
    public void AllEvent() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByAll);
                getMessageBean.setEmMessage(message);
            }
        });
    }

    /**
     * 有选择性的接收某些类型event事件：（该方法接收新消息）
     * EMNotifierEvent.Event.EventDeliveryAck;//已发送回执event注册
     * EMNotifierEvent.Event.EventNewCMDMessage;//接收透传event注册
     * EMNotifierEvent.Event.EventNewMessage;//接收新消息event注册
     * EMNotifierEvent.Event.EventOfflineMessage;//接收离线消息event注册
     * EMNotifierEvent.Event.EventReadAck;//已读回执event注册
     * EMNotifierEvent.Event.EventConversationListChanged;//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）
     */
    @Override
    public void EventNewMessage() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
//                TextMessageBody textMessageBody= (TextMessageBody) message.getBody();
//                textMessageBody.getMessage();
//                FileMessageBody fileMessageBody= (FileMessageBody) message.getBody();
//                fileMessageBody.getLocalUrl();
//                fileMessageBody.setDownloadCallback(new EMCallBack() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
//
//                    }
//
//                    @Override
//                    public void onProgress(int i, String s) {
//
//                    }
//                });
                getMessageBean.setGetMsgCode(MessageContant.setMsgByNew);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage});//接收新消息
    }

    /**
     * 已发送回执
     */
    public void EventDeliveryAck() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByDeliveryAck);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventDeliveryAck});//已发送回执
    }

    /**
     * 透传消息
     */
    public void EventNewCMDMessage() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByNewCMDM);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewCMDMessage});//已发送回执
    }

    /**
     * 离线消息
     */
    public void EventOfflineMessage() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByOffline);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventOfflineMessage});//已发送回执
    }

    /**
     * 已读
     */
    public void EventReadAck() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByReadAck);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventReadAck});//已发送回执
    }

    /**
     * 会话列表改变
     */
    public void  EventConversationListChanged() {
        initSerMsg();
        EMChatManager.getInstance().registerEventListener(new EMEventListener() {

            @Override
            public void onEvent(EMNotifierEvent event) {
                // TODO Auto-generated method stub
                message = (EMMessage) event.getData();
                getMessageBean.setGetMsgCode(MessageContant.setMsgByListChanged);
                getMessageBean.setEmMessage(message);
                eventBus.post(getMessageBean);
            }
        }, new EMNotifierEvent.Event[]{EMNotifierEvent.Event. EventConversationListChanged});//已发送回执
    }

    /**
     * 解除监听事件
     * <p>
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
