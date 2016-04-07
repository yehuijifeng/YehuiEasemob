package com.yehui.easemob.fragment.base;

import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.utils.fragment.base.BaseListFragment;

/**
 * Created by yehuijifeng
 * on 2016/1/4.
 * 列表fragment的代理类
 */
public abstract class EasemobListFragment extends BaseListFragment{

//    @Override
//    public void onResume() {
//        //注册消息监听
//        EMChatManager.getInstance().registerEventListener(this);
//        super.onResume();
//    }
//
//    @Override
//    public void onStop() {
//        EMChatManager.getInstance().unregisterEventListener(this);//注销消息监听
//        super.onStop();
//    }

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    public void onEventMainThread(MessageBean messageBean) {
        getNewMessage(messageBean);
    }

    protected void showMessageNumberIcon() {
        ((EasemobActivity) parentActivity).showMessageCount();
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }

    /**
     * 接收新消息
     *
     * @param messageBean
     */
    public void getNewMessage(MessageBean messageBean) {
        showMessageNumberIcon();
    }

    /**
     * 接收透传消息
     *
     * @param messageBean
     */
    public void getNewCMDMessage(MessageBean messageBean) {

    }

//    /**
//     * 接收消息回调
//     *
//     * @param event
//     */
//    @Override
//    public void onEvent(EMNotifierEvent event) {
//        EMMessage message;
//        MessageBean messageBean;
//        if (event.getData() instanceof EMMessage) {
//            message = (EMMessage) event.getData();
//            messageBean = new MessageBean();
//            messageBean.setEmMessage(message);
//            LogUtil.d("receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
//        } else if (event.getData() instanceof List) {
//            LogUtil.d("received offline messages");
//            messageBean = new MessageBean();
//            List<EMMessage> messages = (List<EMMessage>) event.getData();
//            messageBean.setGetMsgCode(MessageContant.receiveMsgByOffline);
//            messageBean.setMessageList(messages);
//            return;
//        } else return;
//        switch (event.getEvent()) {
//            default:
//                messageBean.setEmMessage(message);
//                break;
//            case EventNewMessage://接收新消息event注册
//                messageBean.setGetMsgCode(MessageContant.receiveMsgByNew);
//                eventBus.post(messageBean);
//
//                break;
//            case EventNewCMDMessage://接收透传event注册
//                LogUtil.d("收到透传消息");
//                messageBean.setGetMsgCode(MessageContant.receiveMsgByNewCMDM);
//                // 获取消息body
//                //CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
//                //final String action = cmdMsgBody.action;// 获取自定义action
//                getNewCMDMessage(messageBean);
//                break;
//            case EventDeliveryAck://已发送回执event注册
//                message.setDelivered(true);
//                messageBean.setGetMsgCode(MessageContant.receiveMsgByDeliveryAck);
//                break;
//            case EventReadAck://已读回执event注册
//                message.setAcked(true);
//                messageBean.setGetMsgCode(MessageContant.receiveMsgByReadAck);
//                break;
//
//        }
//
//    }

}
