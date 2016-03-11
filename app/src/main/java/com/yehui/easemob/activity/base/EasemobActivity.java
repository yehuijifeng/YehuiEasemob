package com.yehui.easemob.activity.base;

import android.os.Bundle;

import com.easemob.EMError;
import com.yehui.easemob.activity.HomeActivity;
import com.yehui.easemob.activity.LoginActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.bean.GetMessageBean;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.activity.base.BaseActivity;
import com.yehui.utils.view.dialog.LoadingDialog;
import com.yehui.utils.view.dialog.PromptDialog;

/**
 * Created by Luhao on 2016/2/28.
 */
public abstract class EasemobActivity extends BaseActivity {

    private PromptDialog promptDialog;
    private LoadingDialog loadingDialog;

    @Override
    protected void initData() {
        promptDialog = new PromptDialog(this);
        loadingDialog = new LoadingDialog(this);

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
     * @param getMessageBean
     */
    public void onEventMainThread(GetMessageBean getMessageBean) {
        getMessageStatus(getMessageBean);

    }

    protected void getMessageStatus(GetMessageBean getMessageBean) {
        switch (getMessageBean.getGetMsgCode()) {
            case MessageContant.getMsgByText:

                break;
            case MessageContant.getMsgByVoice://语音
                break;
            case MessageContant.getMsgByImage://图片
                break;
            case MessageContant.getMsgByLocation://地理位置
                break;
            case MessageContant.getMsgByFile://文件
                break;
        }
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
                loadingEmpty(serverBean.getStatusMsg());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (promptDialog != null) {
//            promptDialog.dismissPromptDialog();
//        }
    }
}
