package com.yehui.easemob.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.MessageActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.fragment.base.EasemobListFragment;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.view.CircularImageView;
import com.yehui.utils.view.titleview.MyTitleView;

/**
 * Created by Luhao
 * on 2016/2/20.
 * 消息列表页面
 */
public class MessageFragment extends EasemobListFragment {

    private MyTitleView titleView;
    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        titleView = (MyTitleView) parentView.findViewById(R.id.my_title_view);
        titleView.setTitleText("消息");
        titleView.setTitleMode(MyTitleView.TitleMode.NO_BACK_IMAGE);
    }

    @Override
    protected void initData() {
        super.initData();
        clearAll();
        loadingView();
        addAll(SendMessageHelper.getInstance().loadConversationList());
        loadingSuccess();
        notifyDataChange();
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
        userInfoDao = new UserInfoDao(parentActivity);
        userInfoBean = (UserInfoBean) userInfoDao.queryByWhere(MapContant.USER_NAME, EasemobAppliaction.user.getUserName()).get(0);

    }

    @Override
    protected void refresh() {
        super.refresh();
        //loadingView();
        clearAll();
        addAll(SendMessageHelper.getInstance().loadConversationList());
        refreshSuccess();
        notifyDataChange();
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
    }

    @Override
    protected void reLoad() {
        super.reLoad();
        loadingView();
        clearAll();
        addAll(SendMessageHelper.getInstance().loadConversationList());
        refreshSuccess();
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
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
        imageLoader.displayImage("", messageViewHolder.user_icon_img, EasemobAppliaction.roundOptions);
        messageViewHolder.user_name_text.setText(emMessage.getUserName());
        messageViewHolder.message_number_text.setText(emConversation.getMsgCount() + "");
        messageViewHolder.message_time_text.setText(emMessage.getMsgTime() + "");
        getMsgType(emMessage, messageViewHolder.user_message_text);
    }

    private void getMsgType(EMMessage emMessage, TextView textView) {
        if (emMessage.getType() == EMMessage.Type.TXT) {
            TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
            textView.setText(textMessageBody.getMessage());
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {
            textView.setText("语音消息");
        } else if (emMessage.getType() == EMMessage.Type.IMAGE) {
            textView.setText("图片");
        } else if (emMessage.getType() == EMMessage.Type.FILE) {
            textView.setText("文件");
        } else if (emMessage.getType() == EMMessage.Type.VIDEO) {
            textView.setText("视频");
        } else if (emMessage.getType() == EMMessage.Type.LOCATION) {
            textView.setText("地理位置：我在这里");
        } else if (emMessage.getType() == EMMessage.Type.CMD) {
            textView.setText("未知类型消息");
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
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {
        showShortToast("长按第" + position + "行");
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
