package com.yehui.easemob.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.R;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.fragment.base.EasemobListFragment;
import com.yehui.easemob.helper.GetMessageHelper;
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
        loadingView();
        addAll(GetMessageHelper.getInstance().loadConversationList());
        loadingSuccess();
        notifyDataChange();
        if (data == null || data.size() == 0)
            loadingEmpty("暂无消息");
    }

    @Override
    protected void refresh() {
        super.refresh();
        //loadingView();
        clearAll();
        addAll(GetMessageHelper.getInstance().loadConversationList());
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
        addAll(GetMessageHelper.getInstance().loadConversationList());
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
        messageViewHolder.message_number_text.setText(emMessage.getFrom());
        messageViewHolder.message_number_text.setText(emConversation.getMsgCount() + "");
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new MessageViewHolder(itemView);
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {
        showShortToast("第"+position+"行");
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {
        showShortToast("长按第"+position+"行");
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
