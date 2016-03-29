package com.yehui.easemob.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.model.EaseChatRowVoicePlayClickListener;
import com.yehui.easemob.utils.BiaoqingUtil;
import com.yehui.utils.adapter.base.BaseAdapter;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.utils.DisplayUtil;
import com.yehui.utils.view.CircularImageView;

import java.util.List;

/**
 * Created by Luhao
 * on 2016/3/29.
 * 消息类型适配器
 */
public class MessageAdapter extends BaseAdapter {
    private Activity context;
    private int windowWidth, minWidth;
    private String friendName;
    private DisplayMetrics outMetrics = new DisplayMetrics();
    private EaseChatRowVoicePlayClickListener easeChatRowVoicePlayClickListener;
    private LinearLayout speaker_ly;
    private TextView speaker_text;

    public MessageAdapter(Activity context, List<MessageBean> messageList, String friendName, LinearLayout speaker_ly, TextView speaker_text) {
        super(messageList);
        this.context = context;
        this.friendName = friendName;
        this.speaker_ly = speaker_ly;
        this.speaker_text = speaker_text;
    }

    /**
     * 替换消息
     */
    public MessageAdapter(List<MessageBean> messageList) {
        super(messageList);
    }

    /**
     * 停止语音
     */
    public void stopVoicePlay() {
        if (easeChatRowVoicePlayClickListener != null)
            easeChatRowVoicePlayClickListener.stopPlayVoice();
    }

    /**
     * 向适配器中添加数据
     * @param messageList
     */
    public void addData(List<MessageBean> messageList) {
        data.addAll(messageList);
    }

    public void getLastHour(RecyclerView recyclerView) {
        if (data != null && data.size() > 0) {
            //让控件显示最后一行数据
            recyclerView.smoothScrollToPosition(data.size() - 1);
            notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindDataForItem(BaseViewHolder holder, int position) {
        MessageBean messageBean = (MessageBean) data.get(position);
        EMMessage emMessage = messageBean.getEmMessage();
        switch (emMessage.getType()) {
            case TXT://文本消息，带表情
                TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
                if (emMessage.direct == EMMessage.Direct.SEND) {//判断这条消息是否是发送消息
                    SendTextViewHolder sendTextViewHolder = (SendTextViewHolder) holder;
                    // 设置内容
                    sendTextViewHolder.set_msg_text.setText(BiaoqingUtil.getInstance().showBiaoqing(context, textMessageBody.getMessage()));
                    sendTextViewHolder.set_msg_image.setOnClickListener(new OnUserInfoClick(messageBean));
                    if (messageBean.getBackStatus() == 0) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.VISIBLE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (messageBean.getBackStatus() == 1) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (messageBean.getBackStatus() == -1) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                        sendTextViewHolder.msg_status_img.setOnClickListener(new OnReSendClick(textMessageBody.getMessage(), messageBean.getEmMessage()));
                    }
                } else {
                    ReceiveTextViewHolder receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                    receiveTextViewHolder.get_msg_text.setText(BiaoqingUtil.getInstance().showBiaoqing(context, textMessageBody.getMessage()));
                    receiveTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                    receiveTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    receiveTextViewHolder.get_msg_image.setOnClickListener(new OnUserInfoClick(messageBean));
                }
                break;
            case VOICE://语音消息
                VoiceMessageBody voiceMessageBody = (VoiceMessageBody) emMessage.getBody();
                int length = voiceMessageBody.getLength();
                if (emMessage.direct == EMMessage.Direct.SEND) {
                    SendVoiceViewHolder sendVoiceViewHolder = (SendVoiceViewHolder) holder;
                    sendVoiceViewHolder.set_msg_image.setOnClickListener(new OnUserInfoClick(messageBean));
                    if (minWidth == 0) minWidth = sendVoiceViewHolder.set_msg_text.getMinWidth();
                    sendVoiceViewHolder.set_msg_text.setWidth(getVoiceLengthByView(length));
                    sendVoiceViewHolder.set_msg_ly.setOnClickListener(new OnVoicePlayClick(emMessage, sendVoiceViewHolder.set_msg_img, null));
                    sendVoiceViewHolder.set_msg_text.setText(length + "");
                    sendVoiceViewHolder.msg_voice_is_open.setVisibility(View.GONE);
                    sendVoiceViewHolder.msg_status_img.setVisibility(View.GONE);
                    if (messageBean.getBackStatus() == 0) {
                        sendVoiceViewHolder.msg_progress_bar.setVisibility(View.VISIBLE);
                    } else if (messageBean.getBackStatus() == 1) {
                        sendVoiceViewHolder.msg_progress_bar.setVisibility(View.GONE);
                    } else if (messageBean.getBackStatus() == -1) {
                        sendVoiceViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                        sendVoiceViewHolder.msg_progress_bar.setVisibility(View.GONE);
                    }
                } else {
                    ReceiveVoiceViewHolder receiveVoiceViewHolder = (ReceiveVoiceViewHolder) holder;
                    receiveVoiceViewHolder.get_msg_image.setOnClickListener(new OnUserInfoClick(messageBean));
                    receiveVoiceViewHolder.get_msg_text.setText(length + "");
                    if (minWidth == 0) minWidth = receiveVoiceViewHolder.get_msg_text.getMinWidth();
                    receiveVoiceViewHolder.get_msg_text.setWidth(getVoiceLengthByView(length));
                    receiveVoiceViewHolder.get_msg_ly.setOnClickListener(new OnVoicePlayClick(emMessage, receiveVoiceViewHolder.get_msg_img, receiveVoiceViewHolder.msg_voice_is_open));
                    receiveVoiceViewHolder.msg_status_img.setVisibility(View.GONE);
                    receiveVoiceViewHolder.msg_progress_bar.setVisibility(View.GONE);
                    receiveVoiceViewHolder.msg_voice_is_open.setVisibility(View.VISIBLE);
                    if (emMessage.isListened()) {
                        receiveVoiceViewHolder.msg_voice_is_open.setVisibility(View.INVISIBLE);
                    } else {
                        receiveVoiceViewHolder.msg_voice_is_open.setVisibility(View.GONE);
                    }
                }
                break;
            case IMAGE://图片
                break;
            case LOCATION://地理位置
                break;
            case FILE://文件
                break;
            case CMD://未知类型
                break;
        }
    }

    /**
     * 属于头viewholder
     *
     * @param view
     * @return
     */
    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderItem(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        View convertView;
        switch (viewType) {
            case MessageContant.sendMsgByText:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_msg_text, parent, false);
                viewHolder = new SendTextViewHolder(convertView);
                break;
            case MessageContant.sendMsgByVoice:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_msg_voice, parent, false);
                viewHolder = new SendVoiceViewHolder(convertView);
                break;
            case MessageContant.sendMsgByImage:
                break;
            case MessageContant.sendMsgByLocation:
                break;
            case MessageContant.sendMsgByFile:
                break;
            case MessageContant.receiveMsgByText:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive_msg_text, parent, false);
                viewHolder = new ReceiveTextViewHolder(convertView);
                break;
            case MessageContant.receiveMsgByVoice:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive_msg_voice, parent, false);
                viewHolder = new ReceiveVoiceViewHolder(convertView);
                break;
            case MessageContant.receiveMsgByImage:
                break;
            case MessageContant.receiveMsgByLocation:
                break;
            case MessageContant.receiveMsgByFile:
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemType(int position) {
        MessageBean messageBean = (MessageBean) data.get(position);
        EMMessage emMessage = messageBean.getEmMessage();
        switch (emMessage.getType()) {
            case TXT:
                if (emMessage.direct == EMMessage.Direct.SEND) //判断这条消息是否是发送消息
                    return MessageContant.sendMsgByText;
                else
                    return MessageContant.receiveMsgByText;
            case VOICE:
                if (emMessage.direct == EMMessage.Direct.SEND)
                    return MessageContant.sendMsgByVoice;
                else
                    return MessageContant.receiveMsgByVoice;
            case IMAGE:
                break;
            case LOCATION:
                break;
            case FILE:
                break;
            case CMD:
                break;
        }
        return MessageContant.sendMsgByText;
    }

    /**
     * 根据语音的时长改变语音view的长度
     *
     * @param voiceLength
     * @return
     */
    private int getVoiceLengthByView(int voiceLength) {
        context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        if (windowWidth <= 0) windowWidth = DisplayUtil.px2dip(context, outMetrics.widthPixels);
        if (voiceLength >= 60) return outMetrics.widthPixels;
        float voicePercenst = voiceLength * 100 / 60;
        int thisWidth = DisplayUtil.dip2px(context, windowWidth * voicePercenst / 100);
        if (thisWidth < minWidth) thisWidth = minWidth;
        return thisWidth;
    }

    /**
     * 点击播放语音
     */
    class OnVoicePlayClick implements View.OnClickListener {
        private EMMessage message;
        private ImageView view;
        private ImageView iv_read_status;

        private OnVoicePlayClick(EMMessage message, ImageView view, ImageView iv_read_status) {
            this.message = message;
            this.view = view;
            this.iv_read_status = iv_read_status;
        }

        @Override
        public void onClick(View v) {
            easeChatRowVoicePlayClickListener = new EaseChatRowVoicePlayClickListener(message, view, iv_read_status, MessageAdapter.this, context, speaker_ly, speaker_text);
            easeChatRowVoicePlayClickListener.onClick(v);
        }
    }

    /**
     * 点击头像的操作
     */
    class OnUserInfoClick implements View.OnClickListener {
        private MessageBean messageBean;

        private OnUserInfoClick(MessageBean messageBean) {
            this.messageBean = messageBean;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "用户名：" + messageBean.getEmMessage().getFrom(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击消息重新发送
     */
    class OnReSendClick implements View.OnClickListener {
        private String content;
        private EMMessage message;

        private OnReSendClick(String content, EMMessage message) {
            this.content = content;
            this.message = message;
        }

        @Override
        public void onClick(View v) {
            SendMessageHelper.getInstance().sendConversationByText(friendName, content, true, message.getMsgId());
        }
    }


    /****************************************viewholder******************************************************/


    /**
     * 好友发来的文本类消息
     */
    private class ReceiveTextViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;

        public ReceiveTextViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void initItemView(View itemView) {
            get_msg_image = (CircularImageView) itemView.findViewById(R.id.get_msg_image);
            get_msg_text = (TextView) itemView.findViewById(R.id.get_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 用户发给好友的文本类消息
     */
    private class SendTextViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;

        public SendTextViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void initItemView(View itemView) {
            set_msg_image = (CircularImageView) itemView.findViewById(R.id.set_msg_image);
            set_msg_text = (TextView) itemView.findViewById(R.id.set_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 好友发送过来的语音类消息
     */
    private class ReceiveVoiceViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text, msg_voice_length;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open, get_msg_img;
        private RelativeLayout get_msg_ly;

        public ReceiveVoiceViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void initItemView(View itemView) {
            get_msg_image = (CircularImageView) itemView.findViewById(R.id.get_msg_image);
            get_msg_text = (TextView) itemView.findViewById(R.id.get_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_voice_is_open = (ImageView) itemView.findViewById(R.id.msg_voice_is_open);
            get_msg_ly = (RelativeLayout) itemView.findViewById(R.id.get_msg_ly);
            get_msg_img = (ImageView) itemView.findViewById(R.id.get_msg_img);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 发给好友的语音类消息
     */
    private class SendVoiceViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text, msg_voice_length;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open, set_msg_img;
        private RelativeLayout set_msg_ly;

        public SendVoiceViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            set_msg_image = (CircularImageView) itemView.findViewById(R.id.set_msg_image);
            set_msg_text = (TextView) itemView.findViewById(R.id.set_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_voice_is_open = (ImageView) itemView.findViewById(R.id.msg_voice_is_open);
            set_msg_ly = (RelativeLayout) itemView.findViewById(R.id.set_msg_ly);
            set_msg_img = (ImageView) itemView.findViewById(R.id.set_msg_img);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 显示时间
     */
    private class MessageTimeVIewHolder extends BaseViewHolder {
        private TextView msg_time_text, load_more_text;
        private ProgressBar msg_progress_bar;

        public MessageTimeVIewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            load_more_text = (TextView) itemView.findViewById(R.id.load_more_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
        }
    }
}
