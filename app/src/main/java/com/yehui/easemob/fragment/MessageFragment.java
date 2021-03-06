package com.yehui.easemob.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.HomeActivity;
import com.yehui.easemob.activity.MessageActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.fragment.base.EasemobListFragment;
import com.yehui.easemob.helper.ReceiveMessageHelper;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.utils.BiaoqingUtil;
import com.yehui.easemob.utils.DateUtil;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.view.CircularImageView;
import com.yehui.utils.view.dialog.PromptDialog;
import com.yehui.utils.view.titleview.MyTitleView;

import java.util.List;

/**
 * Created by Luhao
 * on 2016/2/20.
 * 消息列表页面
 */
public class MessageFragment extends EasemobListFragment {

    private MyTitleView titleView;
    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;
    private PromptDialog promptDialog;//提示框

    @Override
    protected void initView(final View parentView) {
        super.initView(parentView);
        titleView = (MyTitleView) parentView.findViewById(R.id.my_title_view);
        titleView.setTitleText("消息");
        titleView.setTitleMode(MyTitleView.TitleMode.NO_BACK_IMAGE);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清楚所有未读消息
                promptDialog.showPromptDialog("确定清除所有未读消息？", new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        ReceiveMessageHelper.getInstance().markAllConversationsAsRead();
                        reLoad();
                        if (((HomeActivity) parentActivity).message_number_text != null) {
                            ((HomeActivity) parentActivity).message_number_text.setText("0");
                            ((HomeActivity) parentActivity).message_number_text.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
        promptDialog = new PromptDialog(parentActivity);
    }

    @Override
    protected void initData() {
        super.initData();
        loadingView();
        loadData();
        userInfoDao = new UserInfoDao(parentActivity);
        userInfoBean = (UserInfoBean) userInfoDao.queryByWhere(MapContant.USER_NAME, EasemobAppliaction.user.getUserName()).get(0);
    }

    @Override
    public void getNewMessage(MessageBean messageBean) {
        clearAll();
        addAll(SendMessageHelper.getInstance().loadConversationList());
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
        notifyDataChange();
        showMessageNumberIcon();
    }

    @Override
    public void getNewCMDMessage(MessageBean messageBean) {
        //对方撤回的一条消息
        String a = ((CmdMessageBody) messageBean.getEmMessage().getBody()).action;
        if (a.equals(MessageContant.sendRevokeMessage)) {
            try {
                String receiveStr = messageBean.getEmMessage().getStringAttribute(MessageContant.sendRevokeMessageById);
                String thisStr;
                for (int i = 0; i < data.size(); i++) {
                    //先搜索最后一条消息，若不是撤回的最后一条消息则在进入下个循环
                    thisStr = ((EMConversation) (data.get(i))).getLastMessage().getMsgId();
                    if (thisStr.equals(receiveStr)) {
                        if (messageBean.getContent() == null)
                            messageBean.setContent(MessageContant.revokeStr);
                        EMConversation emConversation = SendMessageHelper.getInstance().receiveRevokeMessage(messageBean);
                        data.set(i, emConversation);
                        mAdapter.notifyDataSetChanged();
                        showMessageNumberIcon();
                        return;
                    }
                }
                for (int i = 0; i < data.size(); i++) {
                    List<EMMessage> list = ((EMConversation) (data.get(i))).getAllMessages();
                    for (int j = 0; j < list.size(); j++) {
                        thisStr = list.get(j).getMsgId();
                        if (thisStr.equals(receiveStr)) {
                            if (messageBean.getContent() == null)
                                messageBean.setContent(MessageContant.revokeStr);
                            EMConversation emConversation = SendMessageHelper.getInstance().receiveRevokeMessage(messageBean);
                            data.set(i, emConversation);
                            mAdapter.notifyDataSetChanged();
                            showMessageNumberIcon();
                            return;
                        }
                    }
                }
            } catch (EaseMobException e) {
                return;
            }
        }
    }

    private void loadData() {
        clearAll();
        addAll(SendMessageHelper.getInstance().loadConversationList());
        notifyDataChange();
        loadingSuccess();
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
        showMessageNumberIcon();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void refresh() {
        super.refresh();
        loadData();
        refreshSuccess();
    }

    @Override
    protected void reLoad() {
        super.reLoad();
        loadingView();
        loadData();
    }

    @Override
    protected int getItemLayoutResId(int type) {
        return R.layout.item_message;
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        EMConversation emConversation = (EMConversation) data.get(position);
        EMMessage emMessage = emConversation.getLastMessage();
        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        imageLoader.displayImage("图片资源路径，暂无", messageViewHolder.user_icon_img, EasemobAppliaction.roundOptions);
        messageViewHolder.user_name_text.setText(emMessage.getUserName());
        messageViewHolder.message_time_text.setText(DateUtil.stampToTime(emMessage.getMsgTime()));
        if (emConversation.getUnreadMsgCount() == 0)
            messageViewHolder.message_number_text.setVisibility(View.GONE);
        else if (emConversation.getUnreadMsgCount() >= 100) {
            messageViewHolder.message_number_text.setText("99+");
            messageViewHolder.message_number_text.setVisibility(View.VISIBLE);
        } else {
            messageViewHolder.message_number_text.setText(emConversation.getUnreadMsgCount() + "");
            messageViewHolder.message_number_text.setVisibility(View.VISIBLE);
        }
        getMsgType(emConversation, messageViewHolder.user_message_text);
    }

    private void getMsgType(EMConversation emConversation, TextView textView) {
        EMMessage emMessage = emConversation.getLastMessage();
        if (emMessage.getType() == EMMessage.Type.TXT) {
            TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
            if (textMessageBody.getMessage() == null) return;
            textView.setText(BiaoqingUtil.getInstance().showBiaoqing(parentActivity, textMessageBody.getMessage()));
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {
            textView.setText(emConversation.getUnreadMsgCount() == 0 ? "[语音消息]" : emConversation.getUnreadMsgCount() + "条语音消息");
        } else if (emMessage.getType() == EMMessage.Type.IMAGE) {
            textView.setText(emConversation.getUnreadMsgCount() == 0 ? "[图片]" : emConversation.getUnreadMsgCount() + "张图片");
        } else if (emMessage.getType() == EMMessage.Type.FILE) {
            textView.setText(emConversation.getUnreadMsgCount() == 0 ? "[文件]" : emConversation.getUnreadMsgCount() + "个文件");
        } else if (emMessage.getType() == EMMessage.Type.VIDEO) {
            textView.setText(emConversation.getUnreadMsgCount() == 0 ? "[视频]" : emConversation.getUnreadMsgCount() + "个视频");
        } else if (emMessage.getType() == EMMessage.Type.LOCATION) {
            textView.setText("地理位置：我在这里");
        } else if (emMessage.getType() == EMMessage.Type.CMD) {

        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new MessageViewHolder(itemView);
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {
        //指定的QQ号只需要修改uin后的值即可。
        //String url = "mqqwpa://im/chat?chat_type=wpa&uin=928186846";
        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

        EMConversation emConversation = (EMConversation) data.get(position);
        EMMessage emMessage = emConversation.getLastMessage();
        Bundle bundle = new Bundle();
        bundle.putString(MapContant.MESSAGE_USER_NAME, emMessage.getUserName());
        startActivity(MessageActivity.class, bundle);
        //注销消息监听
        EMChatManager.getInstance().unregisterEventListener((HomeActivity) parentActivity);
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {
        //showShortToast("长按第" + position + "行");
    }

    @Override
    protected int setFragmentLayoutId() {
        return R.layout.fragment_message;
    }

    private class MessageViewHolder extends BaseViewHolder {

        private CircularImageView user_icon_img;
        private TextView user_name_text, user_message_text, message_time_text, message_number_text;

        public MessageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            user_icon_img = (CircularImageView) itemView.findViewById(R.id.user_icon_img);
            user_name_text = (TextView) itemView.findViewById(R.id.user_name_text);
            user_message_text = (TextView) itemView.findViewById(R.id.user_message_text);
            message_time_text = (TextView) itemView.findViewById(R.id.message_time_text);
            message_number_text = (TextView) itemView.findViewById(R.id.message_number_text);
        }
    }
}
