package com.yehui.easemob.helper;

import android.support.v4.util.Pair;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.yehui.easemob.bean.GetMessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.interfaces.GetMessageInterfaces;

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
public class GetMessageHelper implements GetMessageInterfaces {

    private GetMessageHelper() {
        getMessageBean = new GetMessageBean();
        eventBus = EventBus.getDefault();
    }

    private static volatile GetMessageHelper instance = null;

    public synchronized static GetMessageHelper getInstance() {
        if (instance == null) {
            synchronized (GetMessageHelper.class) {
                if (instance == null) {
                    instance = new GetMessageHelper();
                }
            }
        }
        return instance;
    }

    private EventBus eventBus;
    private GetMessageBean getMessageBean;

    /**
     * 初始化信息
     */
    private void initSendMsg() {
        getMessageBean = new GetMessageBean();
    }

    /**
     * 发送文本消息及表情
     *
     * @param content  发送的内容
     * @param username 接收人
     */
    @Override
    public void getConversationByText(String username, String content) {
        //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        //创建一条文本消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);

        //设置消息body
        TextMessageBody txtBody = new TextMessageBody(content);
        message.addBody(txtBody);
        //设置接收人
        message.setReceipt(username);
        //把消息加入到此会话对象中
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        getMessageBean.setGetMsgCode(MessageContant.getMsgByText);
        getMessageBean.setUserName(username);
        getMessageBean.setContent(content);
        getMessageBean.setEmMessage(message);
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                getMessageBean.setBackStatus(1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onError(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(-1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(0);
                eventBus.post(getMessageBean);
            }
        });
    }


    /**
     * 发送语音消息
     *
     * @param username 接收人
     * @param filePath 语音文件路径
     * @param length   文件长度，大小
     */
    @Override
    public void getConversationByVoice(String username, String filePath, int length) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        VoiceMessageBody body = new VoiceMessageBody(new File(filePath), length);
        message.addBody(body);
        message.setReceipt(username);
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        getMessageBean.setGetMsgCode(MessageContant.getMsgByVoice);
        getMessageBean.setUserName(username);
        getMessageBean.setContent(filePath);
        getMessageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                getMessageBean.setBackStatus(1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onError(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(-1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(0);
                eventBus.post(getMessageBean);
            }
        });
    }

    /**
     * 发送图片消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     */
    @Override
    public void getConversationByImage(String username, String filePath) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);

        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        message.setReceipt(username);
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        getMessageBean.setGetMsgCode(MessageContant.getMsgByImage);
        getMessageBean.setUserName(username);
        getMessageBean.setContent(filePath);
        getMessageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                getMessageBean.setBackStatus(1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onError(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(-1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(0);
                eventBus.post(getMessageBean);
            }
        });
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
    public void getConversationByLocation(String username, String locationAddress, double latitude, double longitude) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(username);
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
        //如果是群聊，设置chattype,默认是单聊
        //message.setChatType(EMMessage.ChatType.GroupChat);
        LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(username);
        conversation.addMessage(message);
        //初始化发送信息
        initSendMsg();
        getMessageBean.setGetMsgCode(MessageContant.getMsgByLocation);
        getMessageBean.setUserName(username);
        getMessageBean.setContent(locationAddress);
        getMessageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                getMessageBean.setBackStatus(1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onError(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(-1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(0);
                eventBus.post(getMessageBean);
            }
        });
    }

    /**
     * 发送图片消息
     *
     * @param username 接收人
     * @param filePath 图片文件路径
     */
    @Override
    public void getConversationByFile(String username, String filePath) {
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
        getMessageBean.setGetMsgCode(MessageContant.getMsgByFile);
        getMessageBean.setUserName(username);
        getMessageBean.setContent(filePath);
        getMessageBean.setEmMessage(message);
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                getMessageBean.setBackStatus(1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onError(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(-1);
                eventBus.post(getMessageBean);
            }

            @Override
            public void onProgress(int i, String s) {
                getMessageBean.setGetMsgErrorInt(i);
                getMessageBean.setGetMsgErrorStr(s);
                getMessageBean.setBackStatus(0);
                eventBus.post(getMessageBean);
            }
        });
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
     * @param usernameOrGroupid 用户名名和群id
     */
    @Override
    public int getUnreadMsgCount(String usernameOrGroupid) {
        EMConversation conversation = EMChatManager.getInstance().getConversation(usernameOrGroupid);
        return conversation.getUnreadMsgCount();
    }

    /**
     * 获取消息总数
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
