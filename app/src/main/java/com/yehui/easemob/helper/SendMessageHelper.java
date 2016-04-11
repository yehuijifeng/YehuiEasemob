package com.yehui.easemob.helper;

import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.MessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.interfaces.SendMessageInterfaces;
import com.yehui.utils.utils.DateUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Luhao
 * on 2016/2/22.
 * 单聊帮助类
 * 发文字,语音,图片,位置
 * 发送消息(单聊/群聊)
 */
public class SendMessageHelper implements SendMessageInterfaces {

    private SendMessageHelper() {
        messageBean = new MessageBean();
        eventBus = EventBus.getDefault();
    }

    private static volatile SendMessageHelper instance = null;

    public synchronized static SendMessageHelper getInstance() {
        if (instance == null) {
            synchronized (SendMessageHelper.class) {
                if (instance == null) {
                    instance = new SendMessageHelper();
                }
            }
        }
        return instance;
    }

    private EventBus eventBus;
    private MessageBean messageBean;

    /**
     * 初始化信息
     */
    private void initSendMsg() {
        messageBean = new MessageBean();
    }


    @Override
    public MessageBean sendConversationByText(String username, String content, boolean isReSend, String msgId) {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);

        EMMessage emMessage = EMMessage.createSendMessage(EMMessage.Type.TXT);
        MessageBody messageBody = new TextMessageBody(content);
        emMessage.addBody(messageBody);
        emMessage.direct = EMMessage.Direct.SEND;
        //设置接收人
        emMessage.setReceipt(username);
        if (isReSend) {
            if (TextUtils.isEmpty(msgId)) return null;
            emMessage.setMsgId(msgId);//如果是重发，则套用原来的id
        } else
            emMessage.setMsgId(DateUtil.getTimeString());//以时间戳作为id
        //把消息加入到此会话对象中
        conversation.addMessage(emMessage);
        //初始化发送信息
        initSendMsg();
        messageBean.setBackStatus(0);
        messageBean.setGetMsgCode(MessageContant.sendMsgByText);
        messageBean.setUserName(username);
        messageBean.setContent(content);
        messageBean.setEmMessage(emMessage);
        //发送消息
        EMChatManager.getInstance().sendMessage(emMessage, new EMCallBack() {
            @Override
            public void onSuccess() {
                messageBean.setBackStatus(1);
                eventBus.post(messageBean);
            }

            @Override
            public void onError(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(-1);
                eventBus.post(messageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(0);
                eventBus.post(messageBean);
            }
        });
        return messageBean;
    }


    /**
     * 发送语音消息
     *
     * @param username 接收人
     * @param filePath 语音文件路径
     * @param length   文件长度，大小
     */
    @Override
    public MessageBean sendConversationByVoice(String username, String filePath, int length, boolean isReSend, String msgId) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        VoiceMessageBody body = new VoiceMessageBody(new File(filePath), length);
        message.addBody(body);
        //设置发送状态
        message.direct = EMMessage.Direct.SEND;
        //设置接收人
        message.setReceipt(username);
        if (isReSend) {
            if (TextUtils.isEmpty(msgId)) return null;
            message.setMsgId(msgId);//如果是重发，则套用原来的id
        } else
            message.setMsgId(DateUtil.getTimeString());//以时间戳作为id
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        messageBean.setBackStatus(0);
        messageBean.setGetMsgCode(MessageContant.sendMsgByVoice);
        messageBean.setUserName(username);
        messageBean.setContent(filePath);
        messageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                messageBean.setBackStatus(1);
                eventBus.post(messageBean);
            }

            @Override
            public void onError(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(-1);
                eventBus.post(messageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(0);
                eventBus.post(messageBean);
            }
        });
        return messageBean;
    }

    /**
     * 发送图片消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     *                 是否是重新发送
     *                 聊天id
     */
    @Override
    public MessageBean getConversationByImage(String username, String filePath, boolean isSendOriginalImage, boolean isReSend, String msgId) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        message.addBody(body);
        //设置发送状态
        message.direct = EMMessage.Direct.SEND;
        //设置接收人
        message.setReceipt(username);
        if (isReSend) {
            if (TextUtils.isEmpty(msgId)) return null;
            message.setMsgId(msgId);//如果是重发，则套用原来的id
        } else
            message.setMsgId(DateUtil.getTimeString());//以时间戳作为id
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        messageBean.setBackStatus(0);
        messageBean.setUserName(username);
        messageBean.setLoadUrl(filePath);
        messageBean.setEmMessage(message);
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        body.setSendOriginalImage(isSendOriginalImage);
        //初始化发送信息
        messageBean.setGetMsgCode(MessageContant.sendMsgByImage);
        messageBean.setEmMessage(message);

        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                messageBean.setBackStatus(1);
                eventBus.post(messageBean);
            }

            @Override
            public void onError(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(-1);
                eventBus.post(messageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(0);
                eventBus.post(messageBean);
            }
        });
        return messageBean;
    }

    /**
     * 发送地理位置
     *
     * @param username        接收人
     * @param locationAddress 位置地址
     * @param latitude        维度
     * @param longitude       经度
     */
    @Override
    public MessageBean getConversationByLocation(String username, String locationAddress, double longitude, double latitude, boolean isReSend, String msgId) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude,longitude);
        message.addBody(locBody);
        message.setReceipt(username);
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        messageBean.setGetMsgCode(MessageContant.sendMsgByLocation);
        messageBean.setUserName(username);
        messageBean.setContent(locationAddress);
        messageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                messageBean.setBackStatus(1);
                eventBus.post(messageBean);
            }

            @Override
            public void onError(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(-1);
                eventBus.post(messageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(0);
                eventBus.post(messageBean);
            }
        });
        return messageBean;
    }

    /**
     * 发送文件消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     */
    @Override
    public MessageBean getConversationByFile(String username, String filePath, boolean isReSend, String msgId) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
//        if (chatType == TTYPE_GROUP)
//          message.setChatType(EMMessage.ChatType.GroupChat);

        //设置接收人的username
        message.setReceipt(username);
        // add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
        message.addBody(body);
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        messageBean.setGetMsgCode(MessageContant.sendMsgByFile);
        messageBean.setUserName(username);
        messageBean.setContent(filePath);
        messageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                messageBean.setBackStatus(1);
                eventBus.post(messageBean);
            }

            @Override
            public void onError(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(-1);
                eventBus.post(messageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                messageBean.setGetMsgErrorInt(i);
                messageBean.setGetMsgErrorStr(s);
                messageBean.setBackStatus(0);
                eventBus.post(messageBean);
            }
        });
        return messageBean;
    }

    @Override
    public MessageBean getConversationByVideo(String username, String filePath, boolean isReSend, String msgId) {
        return messageBean;
    }


    /**
     * 获取会话列表
     */
    @Override
    public List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //如果聊天不集合属于聊天室
                    // if(conversation.getType() !=EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                    //}
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            //内部是timsort算法，有错误
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }


    /**
     * 获取聊天记录
     */
    @Override
    public List<EMMessage> getEMMessageList(String usernameOrGroupid) {
        try {
            EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
            //获取此会话的所有消息
            List<EMMessage> messages = conversation.getAllMessages();
            return messages;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * 获得未读消息总数
     *
     * @return
     */
    public int getMessageCount() {
        return EMChatManager.getInstance().getUnreadMsgsCount();
    }

    /**
     * 获取聊天记录
     */
    @Override
    public List<EMMessage> getEMMessageList(String usernameOrGroupid, String startMsgId, int pagesize) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        //sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        //如果是群聊，调用下面此方法
        //List<EMMessage> messages = conversation.loadMoreGroupMsgFromDB(startMsgId, pagesize);
        return messages;
    }

    /**
     * 获取未读消息数量
     *
     * @param usernameOrGroupid 用户名名和群id
     */
    @Override
    public int getUnreadMsgCount(String usernameOrGroupid) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        return conversation.getUnreadMsgCount();
    }

    /**
     * 获取消息总数
     *
     * @param usernameOrGroupid 用户名名和群id
     */
    @Override
    public int getMsgCount(String usernameOrGroupid) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        return conversation.getMsgCount();
    }

    /**
     * 设置某条消息为已读
     */
    @Override
    public void getMarkAsRead(String usernameOrGroupid, EMMessage message, boolean markAsRead) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        //markAsRead为true，则标记msgid的消息未已读
        conversation.getMessage(message.getMsgId(), markAsRead);
    }

}
