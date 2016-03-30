package com.yehui.easemob.activity.base;

import android.os.Bundle;
import android.view.View;

import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.activity.HomeActivity;
import com.yehui.easemob.activity.LoginActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.activity.base.BaseActivity;
import com.yehui.utils.utils.LogUtil;
import com.yehui.utils.view.dialog.LoadingDialog;
import com.yehui.utils.view.dialog.PromptDialog;

import java.util.List;

/**
 * Created by Luhao on 2016/2/28.
 */
public abstract class EasemobActivity extends BaseActivity implements EMEventListener {

    protected PromptDialog promptDialog;
    protected LoadingDialog loadingDialog;

    @Override
    protected void onResume() {
        //注册消息监听
        //EMChatManager.getInstance().registerEventListener(this,
        //new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
        EMChatManager.getInstance().registerEventListener(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);//注销消息监听
        super.onStop();
    }


    @Override
    protected void initData() {
        long a = System.currentTimeMillis();
        promptDialog = new PromptDialog(EasemobActivity.this);
        loadingDialog = new LoadingDialog(EasemobActivity.this);
        LogUtil.e(System.currentTimeMillis() - a + "聊天基类");
    }


    /**
     * 好友体系状态回调
     *
     * @param friendBean
     */
    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    /**
     * 判断消息是否发送成功
     *
     * @param messageBean
     */
    public void onEventMainThread(MessageBean messageBean) {
        getMessageStatus(messageBean);

    }

    protected void getMessageStatus(MessageBean messageBean) {
    }

    /**
     * 服务器状态回调
     *
     * @param serverBean
     */
    public void onEventMainThread(ServerBean serverBean) {
        getServerStatus(serverBean);
    }

    /**
     * 好友体系状态码回调
     */
    protected synchronized void getFriendStatus(final FriendBean friendBean) {
        switch (friendBean.getStatusCode()) {
            case EasemobContant.acceptFriendRequest://同意好友成功
            case EasemobContant.onContactAgreed://好友请求被同意
            case EasemobContant.onContactRefused://好友请求被拒绝
            case EasemobContant.onContactDeleted://好友将本人删除
            case EasemobContant.onContactAdded://好友添加了本人
                promptDialog.showPromptDialog(friendBean.getStatusMsg(), new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case EasemobContant.onContactInvited://收到好友邀请
                promptDialog.showPromptDialog(friendBean.getStatusMsg(), new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        FriendStatushelper.getInstance().acceptFriendRequests(friendBean.getFriendName());
                    }

                    @Override
                    public void onCancel() {
                        FriendStatushelper.getInstance().refuseFriendRequests(friendBean.getFriendName());
                    }
                });
                break;
        }
    }

    /**
     * 服务器状态回调
     */
    private void getServerStatus(ServerBean serverBean) {
        switch (serverBean.getStatusCode()) {
            case EMError.CONNECTION_CONFLICT://账号在其他设备登陆
                promptDialog.showPromptDialog(serverBean.getStatusMsg(), new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        loadingDialog.showLoadingDialog("正在登录，请稍后……");
                        ServerStatusHelper.getInstance().loginByTesting(EasemobAppliaction.user.getUserName(), EasemobAppliaction.user.getUserPwd());
                    }

                    @Override
                    public void onCancel() {
                        ServerStatusHelper.getInstance().outlogin(EasemobActivity.this);
                    }
                });
                break;
            case EasemobContant.loginTesting://单点登录状态
                loadingDialog.dismissLoadingDialog();
                if (serverBean.isOperation()) {
                    finishAll();
                    startActivity(HomeActivity.class);
                } else {
                    ServerStatusHelper.getInstance().outlogin(EasemobActivity.this);
                }
                break;
            case EMError.USER_REMOVED://账号被移除
                promptDialog.showPromptDialog(serverBean.getStatusMsg(), new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        ServerStatusHelper.getInstance().outlogin(EasemobActivity.this);
                    }

                    @Override
                    public void onCancel() {
                        ServerStatusHelper.getInstance().outlogin(EasemobActivity.this);
                    }
                });

                break;
            case EMError.UNABLE_CONNECT_TO_SERVER://链接不到服务器
            case EMError.NONETWORK_ERROR://网络不可用
                showLongToast(serverBean.getStatusMsg());
                //loadingEmpty(serverBean.getStatusMsg());
                break;
            case EasemobContant.loginOut:
                if (serverBean.isOperation()) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(MapContant.IS_OUT_LOGIN, true);
                    startActivity(LoginActivity.class, bundle);
                    serverBean.getActivity().finish();
                } else {
                    showLongToast(serverBean.getStatusMsg());
                }
                break;
//            default:
//                showLongToast(serverBean.getStatusMsg());
//                break;
        }
    }

    /**
     * 接收新消息
     *
     * @param messageBean
     */
    public void getNewMessage(MessageBean messageBean) {

    }

    /**
     * 接收透传消息
     *
     * @param messageBean
     */
    public void getNewCMDMessage(MessageBean messageBean) {

    }

    /**
     * 接收消息回调
     *
     * @param event
     */
    @Override
    public void onEvent(EMNotifierEvent event) {
        EMMessage message;
        MessageBean messageBean;
        if (event.getData() instanceof EMMessage) {
            message = (EMMessage) event.getData();
            messageBean = new MessageBean();
            messageBean.setEmMessage(message);
            LogUtil.d("receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
        } else if (event.getData() instanceof List) {
            LogUtil.d("received offline messages");
            messageBean = new MessageBean();
            List<EMMessage> messages = (List<EMMessage>) event.getData();
            messageBean.setGetMsgCode(MessageContant.receiveMsgByOffline);
            messageBean.setMessageList(messages);
            return;
        } else return;
        switch (event.getEvent()) {
            default:
                messageBean.setEmMessage(message);
                break;
            case EventNewMessage://接收新消息event注册
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNew);
                getNewMessage(messageBean);
                break;
            case EventNewCMDMessage://接收透传event注册
                LogUtil.d("收到透传消息");
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNewCMDM);
                // 获取消息body
                //CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                //final String action = cmdMsgBody.action;// 获取自定义action
                getNewCMDMessage(messageBean);
                break;
            case EventDeliveryAck://已发送回执event注册
                message.setDelivered(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByDeliveryAck);
                break;
            case EventReadAck://已读回执event注册
                message.setAcked(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByReadAck);
                break;
        }
    }

    /**
     * 显示未读消息的角标
     */
    public void showMessageCount() {
        int j = SendMessageHelper.getInstance().getMessageCount();
        if (HomeActivity.message_number_text != null) {
            if (j <= 0)
                HomeActivity.message_number_text.setVisibility(View.GONE);
            else if (j > 99) {
                HomeActivity.message_number_text.setText("99+");
                HomeActivity.message_number_text.setVisibility(View.VISIBLE);
            } else {
                HomeActivity.message_number_text.setVisibility(View.VISIBLE);
                HomeActivity.message_number_text.setText("" + j);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (promptDialog != null) {
//            promptDialog.dismissPromptDialog();
//        }
    }
}
