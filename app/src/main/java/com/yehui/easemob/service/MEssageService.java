package com.yehui.easemob.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.HomeActivity;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.utils.MusicUtil;
import com.yehui.easemob.utils.VibratorUtil;
import com.yehui.utils.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luhao
 * on 2016/4/6.
 * 离线消息通知服务
 */
public class MessageService extends Service implements EMEventListener {

    private Notification notification;//通知栏
    private NotificationManager notificationManager;//通知栏管理
    private int messageNumber = 0, friendNumber = 0;
    private List<EMMessage> emmessages;
    private Message message;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MusicUtil.getInstance().playMusic(MessageService.this);
                VibratorUtil.vibrate(MessageService.this, 1000);//新消息震动1s
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //声明任务栏通知消息服务
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        EMChatManager.getInstance().registerEventListener(this);//注册消息监听
        emmessages = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMChatManager.getInstance().unregisterEventListener(this);//注销消息监听
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;//kill后重启service
    }

    private void getNotification(MessageBean messageBean) {

        String titleStr = getResources().getString(R.string.app_name), contentStr = "";
        if (messageBean.getEmMessage() == null) {
            if (messageBean.getMessageList() != null) {
                messageNumber += messageBean.getMessageList().size();
                String[] friendName = new String[messageBean.getMessageList().size()];
                for (int i = 0; i < messageBean.getMessageList().size(); i++) {
                    for (int e = 0; e < friendName.length; e++) {
                        if (!messageBean.getMessageList().get(i).getFrom().equals(friendName[e])) {
                            friendNumber += 1;
                            friendName[e] = messageBean.getMessageList().get(i).getFrom();
                        }
                    }
                }
                contentStr = friendNumber + "个联系人给你发来了" + messageNumber + "条消息";
            }
        } else {

            emmessages.add(messageBean.getEmMessage());
            messageNumber = emmessages.size();
            for (int i = 0; i < emmessages.size(); i++) {
                if (i > 0) {
                    if (!emmessages.get(i - 1).getFrom().equals(emmessages.get(i - 1).getFrom())) {
                        friendNumber++;
                    }
                }
            }
            if (friendNumber == 1 && messageNumber > 1) {
                contentStr = messageBean.getEmMessage().getFrom() + " 给你发来了" + messageNumber + "条消息";
            } else if (friendNumber > 1 && messageNumber > 1) {
                contentStr = friendNumber + "个联系人给你发来了" + messageNumber + "条消息";
            } else {
                contentStr = messageBean.getEmMessage().getFrom() + " : " + messageBean.getContent();
            }
        }
        PendingIntent pendingIntent = PendingIntent
                .getActivity(MessageService.this, 0, new Intent(MessageService.this, HomeActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        notification = new Notification
                .Builder(MessageService.this)
                .setSmallIcon(R.mipmap.icon)
                .setTicker(contentStr)
                .setContentTitle(titleStr)
                .setContentText(contentStr)
                .setContentIntent(pendingIntent)
                .setNumber(messageNumber)
                .getNotification();
        //.build(); // 需要注意build()是在API 16及之后增加的，API 11可以使用getNotification()来替代
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;// 不能够自动清除
        notificationManager.notify(1, notification);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
        message.what = 1;
        handler.sendMessage(message);

    }


    /**
     * 接收消息回调
     *
     * @param event
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        EMMessage emMessage;
        MessageBean messageBean;
        message = new Message();
        if (event.getData() instanceof EMMessage) {
            emMessage = (EMMessage) event.getData();
            messageBean = new MessageBean();
            messageBean.setEmMessage(emMessage);
            LogUtil.d("receive the event : " + event.getEvent() + ",id : " + emMessage.getMsgId());
        } else if (event.getData() instanceof List) {
            LogUtil.d("received offline messages");
            messageBean = new MessageBean();
            List<EMMessage> messages = (List<EMMessage>) event.getData();
            messageBean.setGetMsgCode(MessageContant.receiveMsgByOffline);
            messageBean.setMessageList(messages);
            return;
        } else return;
        switch (event.getEvent()) {
            case EventNewMessage://接收新消息event注册
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNew);
                if (messageBean.getEmMessage() != null) {
                    switch (messageBean.getEmMessage().getType()) {
                        case TXT:
                            TextMessageBody textMessageBody = (TextMessageBody) messageBean.getEmMessage().getBody();
                            messageBean.setContent(textMessageBody.getMessage());
                            break;
                        case VOICE://语音消息
                            messageBean.setContent("[语音消息]");
                            break;
                        case IMAGE://图片
                            messageBean.setContent("[图片]");
                            break;
                        case LOCATION://地理位置
                            messageBean.setContent("[地理位置：我在这里]");
                            break;
                        case FILE://文件
                            messageBean.setContent("[文件]");
                            break;
                        case CMD://未知类型
                            messageBean.setContent("[消息]");
                            break;
                    }

                }
                getNotification(messageBean);
                break;
            case EventNewCMDMessage://接收透传event注册
                LogUtil.d("收到透传消息");
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNewCMDM);
                getNotification(messageBean);
                // 获取消息body
                //CmdMessageBody cmdMsgBody = (CmdMessageBody) emMessage.getBody();
                //final String action = cmdMsgBody.action;// 获取自定义action
                break;
            case EventDeliveryAck://已发送回执event注册
                emMessage.setDelivered(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByDeliveryAck);
                break;
            case EventReadAck://已读回执event注册
                emMessage.setAcked(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByReadAck);
                break;
            default:
                break;
        }
    }
}
