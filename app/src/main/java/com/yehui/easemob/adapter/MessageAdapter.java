package com.yehui.easemob.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yehui.easemob.R;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.model.EaseChatRowVoicePlayClickListener;
import com.yehui.easemob.utils.BiaoqingUtil;
import com.yehui.easemob.utils.BitmapUtil;
import com.yehui.easemob.utils.DateUtil;
import com.yehui.utils.activity.PhotoViewActivity;
import com.yehui.utils.adapter.base.BaseAdapter;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.contacts.SettingContact;
import com.yehui.utils.utils.DisplayUtil;
import com.yehui.utils.utils.files.FileOperationUtil;
import com.yehui.utils.view.CircularImageView;

import java.util.ArrayList;
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
    private RecyclerView recyclerView;
    private boolean isOutTime = false;
    private String outTimeStr;
    private int pageSize = 20;
    private ImageLoader imageLoader;
    private int reqWidth = 400, reqHeight = 500;

    public MessageAdapter(Activity context, List<MessageBean> messageList, String friendName, LinearLayout speaker_ly, TextView speaker_text, RecyclerView recyclerView) {
        super(messageList);
        this.context = context;
        this.friendName = friendName;//当前聊天的好友id
        this.speaker_ly = speaker_ly;//切换扬声器和听筒的布局
        this.speaker_text = speaker_text;//切换扬声器和听筒的文字提示
        this.recyclerView = recyclerView;//当前使用的recyclerview
        imageLoader = ImageLoader.getInstance();
        data.add(0, getLoadMoreItem());

    }

    /**
     * 替换消息
     */
    public MessageAdapter(List<MessageBean> messageList) {
        super(messageList);
    }

    /**
     * 加载更多item的适配器
     *
     * @return
     */
    private MessageBean getLoadMoreItem() {
        MessageBean messageBean = new MessageBean();
        messageBean.setContent("点击加载更多");
        messageBean.setBackStatus(MessageContant.loadMoreData);
        return messageBean;
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
     *
     * @param messageList
     */
    public void addData(List<MessageBean> messageList) {
        data.add(messageList);
    }

    public void getLastHour() {
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
        if (position == 0 && messageBean.getBackStatus() == MessageContant.loadMoreData) {
            MessageMoreVIewHolder messageMoreVIewHolder = (MessageMoreVIewHolder) holder;
            if (!isMessageRecord()) {
                messageMoreVIewHolder.load_more_text.setVisibility(View.GONE);
                messageMoreVIewHolder.msg_progress_bar.setVisibility(View.GONE);
            } else {
                messageMoreVIewHolder.load_more_text.setText(messageBean.getContent());
                messageMoreVIewHolder.load_more_text.setOnClickListener(new OnLoadMoreMessageClick(messageMoreVIewHolder.msg_progress_bar, messageMoreVIewHolder.load_more_text));
                messageMoreVIewHolder.msg_progress_bar.setVisibility(View.INVISIBLE);
            }
            return;
        }
        //如果刚刚发送的这条消息和上条消息时间间隔超过3min则显示时间
        if (position >= 2) {
            if (emMessage.getMsgTime() - ((MessageBean) data.get(position - 1)).getEmMessage().getMsgTime() >= 180 * 1000) {
                isOutTime = true;
                outTimeStr = DateUtil.stampByTime(emMessage.getMsgTime(), ((MessageBean) data.get(position - 1)).getEmMessage().getMsgTime());
            } else {
                isOutTime = false;
            }
        }
        switch (emMessage.getType()) {
            case TXT://文本消息，带表情
                TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
                if (emMessage.direct == EMMessage.Direct.SEND) {//判断这条消息是否是发送消息
                    SendTextViewHolder sendTextViewHolder = (SendTextViewHolder) holder;
                    if (isOutTime) {
                        sendTextViewHolder.msg_time_ly.setVisibility(View.VISIBLE);
                        sendTextViewHolder.msg_time_text.setText(outTimeStr);
                    } else {
                        sendTextViewHolder.msg_time_ly.setVisibility(View.GONE);
                    }
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
                        sendTextViewHolder.msg_status_img.setOnClickListener(new OnReSendByTextClick(textMessageBody.getMessage(), messageBean.getEmMessage()));
                    }
                } else {
                    ReceiveTextViewHolder receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                    if (isOutTime) {
                        receiveTextViewHolder.msg_time_ly.setVisibility(View.VISIBLE);
                        receiveTextViewHolder.msg_time_text.setText(outTimeStr);
                    } else {
                        receiveTextViewHolder.msg_time_ly.setVisibility(View.GONE);
                    }
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
                    if (isOutTime) {
                        sendVoiceViewHolder.msg_time_ly.setVisibility(View.VISIBLE);
                        sendVoiceViewHolder.msg_time_text.setText(outTimeStr);
                    } else {
                        sendVoiceViewHolder.msg_time_ly.setVisibility(View.GONE);
                    }
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
                    if (isOutTime) {
                        receiveVoiceViewHolder.msg_time_ly.setVisibility(View.VISIBLE);
                        receiveVoiceViewHolder.msg_time_text.setText(outTimeStr);
                    } else {
                        receiveVoiceViewHolder.msg_time_ly.setVisibility(View.GONE);
                    }
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
                        receiveVoiceViewHolder.msg_voice_is_open.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case IMAGE://图片
                ImageMessageBody imageMessageBody = (ImageMessageBody) emMessage.getBody();
                //imageMessageBody.isSendOriginalImage();//是否是发送的原图
                if (emMessage.direct == EMMessage.Direct.SEND) {
                    SendImageViewHolder sendImageViewHolder = (SendImageViewHolder) holder;
                    //String url = "http://e.hiphotos.baidu.com/album/w%3D2048/sign=f2edabf44bed2e73fce9812cb339a08b/58ee3d6d55fbb2fb87811c624e4a20a44623dc1c.jpg";
                    String url = imageMessageBody.getLocalUrl();
                    if (FileOperationUtil.isHaveFile(url)) {
                        sendImageViewHolder.set_msg_img.setImageBitmap(BitmapUtil.decodeSampledBitmapFromFile(url, reqWidth, reqHeight));
                    } else {
                        sendImageViewHolder.set_msg_img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_loadings));
                    }
                    //imageLoader.displayImage(url, sendImageViewHolder.set_msg_img, EasemobAppliaction.defaultOptions);
                    //imageLoader.displayImage("file:///" + imageMessageBody.getLocalUrl(), sendImageViewHolder.set_msg_img, EasemobAppliaction.defaultOptions);
                    sendImageViewHolder.set_msg_img_fy.setOnClickListener(new OnShowOhotoViewClick(url));
                    if (messageBean.getBackStatus() == 0) {
                        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) sendImageViewHolder.set_msg_dialog.getLayoutParams(); //取控件textView当前的布局参数
                        linearParams.height = linearParams.height - messageBean.getGetMsgErrorInt() / 100 * linearParams.height;// 控件的高强制设
                        sendImageViewHolder.set_msg_dialog.setVisibility(View.VISIBLE);
                        sendImageViewHolder.msg_load_text.setVisibility(View.VISIBLE);
                        sendImageViewHolder.set_msg_dialog.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
                        sendImageViewHolder.msg_load_text.setText((messageBean.getGetMsgErrorInt() + "%"));
                    } else if (messageBean.getBackStatus() == 1) {
                        sendImageViewHolder.set_msg_dialog.setVisibility(View.GONE);
                        sendImageViewHolder.msg_load_text.setVisibility(View.GONE);

                    } else if (messageBean.getBackStatus() == -1) {
                        sendImageViewHolder.set_msg_dialog.setVisibility(View.GONE);
                        sendImageViewHolder.msg_load_text.setVisibility(View.GONE);
                        sendImageViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                    }

                } else {
                    ReceiveImageViewHolder receiveImageViewHolder = (ReceiveImageViewHolder) holder;
                    receiveImageViewHolder.get_msg_dialog.setVisibility(View.VISIBLE);
                    imageLoader.displayImage(imageMessageBody.getThumbnailUrl(), receiveImageViewHolder.get_msg_img, EasemobAppliaction.defaultOptions);
                    receiveImageViewHolder.get_msg_dialog.setVisibility(View.GONE);
                    receiveImageViewHolder.msg_load_text.setVisibility(View.GONE);
                    receiveImageViewHolder.get_msg_img_fy.setOnClickListener(new OnShowOhotoViewClick(imageMessageBody.getThumbnailUrl()));
                }
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_msg_img, parent, false);
                viewHolder = new SendImageViewHolder(convertView);
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receive_msg_img, parent, false);
                viewHolder = new ReceiveImageViewHolder(convertView);
                break;
            case MessageContant.receiveMsgByLocation:
                break;
            case MessageContant.receiveMsgByFile:
                break;
            case MessageContant.loadMoreData:
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_msg, parent, false);
                viewHolder = new MessageMoreVIewHolder(convertView);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemType(int position) {
        MessageBean messageBean = (MessageBean) data.get(position);
        if (position == 0) {
            if (messageBean.getBackStatus() == MessageContant.loadMoreData) {
                return MessageContant.loadMoreData;
            }
        }
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
                if (emMessage.direct == EMMessage.Direct.SEND)
                    return MessageContant.sendMsgByImage;
                else
                    return MessageContant.receiveMsgByImage;
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
     * 监听下载进度
     */
    class OnChangeLoadUrlListener {
        private TextView textView;

        private OnChangeLoadUrlListener(TextView textView) {

        }

    }

    /**
     * 点击查看大图
     */
    class OnShowOhotoViewClick implements View.OnClickListener {
        private String imageUrl;

        private OnShowOhotoViewClick(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PhotoViewActivity.class);
            Bundle bundle = new Bundle();
            ArrayList<String> urls = new ArrayList<>();
            urls.add(imageUrl);
            bundle.putStringArrayList(SettingContact.PHOTO_VIEW_LIST, urls);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
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
    class OnReSendByTextClick implements View.OnClickListener {
        private String content;
        private EMMessage message;

        private OnReSendByTextClick(String content, EMMessage message) {
            this.content = content;
            this.message = message;
        }

        @Override
        public void onClick(View v) {
            SendMessageHelper.getInstance().sendConversationByText(friendName, content, true, message.getMsgId());
        }
    }

    /**
     * 点击加载更多
     */
    class OnLoadMoreMessageClick implements View.OnClickListener {
        private ProgressBar msg_progress_bar;
        private TextView load_more_text;

        private OnLoadMoreMessageClick(ProgressBar msg_progress_bar, TextView load_more_text) {
            this.msg_progress_bar = msg_progress_bar;
            this.load_more_text = load_more_text;
        }

        @Override
        public void onClick(View v) {
            if (!isMessageRecord()) {
                msg_progress_bar.setVisibility(View.GONE);
                load_more_text.setVisibility(View.GONE);
            } else {
                msg_progress_bar.setVisibility(View.INVISIBLE);
                addMessageRecord();
                msg_progress_bar.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 判断是否还有更多的聊天记录
     */
    private boolean isMessageRecord() {
        if (data == null || data.size() <= 1) return false;
        String msgId = ((MessageBean) data.get(1)).getEmMessage().getMsgId();//获得当前显示的最后一条记录id
        List<EMMessage> list = SendMessageHelper.getInstance().getEMMessageList(friendName, msgId, pageSize);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加聊天记录
     */
    private void addMessageRecord() {
        String msgId = ((MessageBean) data.get(1)).getEmMessage().getMsgId();//获得当前显示的最后一条记录id
        List<EMMessage> list = SendMessageHelper.getInstance().getEMMessageList(friendName, msgId, pageSize);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                EMMessage emMessage = list.get(i);
                MessageBean messageBean = new MessageBean();
                messageBean.setBackStatus(1);
                messageBean.setEmMessage(emMessage);
                messageBean.setUserName(emMessage.getUserName());
                data.add(i + 1, messageBean);
            }
            recyclerView.scrollToPosition(list.size() + 1);
            notifyDataSetChanged();
        }
    }

    /****************************************viewholder******************************************************/

    /**
     * 显示加载更多
     */
    private class MessageMoreVIewHolder extends BaseViewHolder {
        private TextView load_more_text;
        private ProgressBar msg_progress_bar;

        public MessageMoreVIewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            load_more_text = (TextView) itemView.findViewById(R.id.load_more_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
        }
    }


    /**
     * 接收文本类消息
     */
    private class ReceiveTextViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text;

        public ReceiveTextViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void initItemView(View itemView) {
            get_msg_image = (CircularImageView) itemView.findViewById(R.id.get_msg_image);
            get_msg_text = (TextView) itemView.findViewById(R.id.get_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 发送文本消息
     */
    private class SendTextViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text;

        public SendTextViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void initItemView(View itemView) {
            set_msg_image = (CircularImageView) itemView.findViewById(R.id.set_msg_image);
            set_msg_text = (TextView) itemView.findViewById(R.id.set_msg_text);
            msg_progress_bar = (ProgressBar) itemView.findViewById(R.id.msg_progress_bar);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 接收语音消息
     */
    private class ReceiveVoiceViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open, get_msg_img;
        private RelativeLayout get_msg_ly;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text;

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
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }

    /**
     * 发送语音消息
     */
    private class SendVoiceViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open, set_msg_img;
        private RelativeLayout set_msg_ly;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text;

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
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            msg_progress_bar.setVisibility(View.GONE);
            msg_status_img.setVisibility(View.GONE);
        }
    }


    /**
     * 发送图片消息
     */
    private class SendImageViewHolder extends BaseViewHolder {

        private CircularImageView set_msg_image;
        private ImageView msg_status_img, set_msg_img, set_msg_dialog;
        private FrameLayout set_msg_img_fy;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text, msg_load_text;

        public SendImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            set_msg_image = (CircularImageView) itemView.findViewById(R.id.set_msg_image);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            set_msg_dialog = (ImageView) itemView.findViewById(R.id.set_msg_dialog);
            set_msg_img = (ImageView) itemView.findViewById(R.id.set_msg_img);
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            set_msg_img_fy = (FrameLayout) itemView.findViewById(R.id.set_msg_img_fy);
            msg_load_text = (TextView) itemView.findViewById(R.id.msg_load_text);
            msg_status_img.setVisibility(View.GONE);
            msg_load_text.setVisibility(View.GONE);
        }
    }

    /**
     * 接收图片消息
     */
    private class ReceiveImageViewHolder extends BaseViewHolder {

        private CircularImageView get_msg_image;
        private ImageView msg_status_img, get_msg_img, get_msg_dialog;
        private FrameLayout get_msg_img_fy;
        private LinearLayout msg_time_ly;
        private TextView msg_time_text, msg_load_text;

        public ReceiveImageViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            get_msg_image = (CircularImageView) itemView.findViewById(R.id.get_msg_image);
            msg_status_img = (ImageView) itemView.findViewById(R.id.msg_status_img);
            get_msg_dialog = (ImageView) itemView.findViewById(R.id.get_msg_dialog);
            get_msg_img = (ImageView) itemView.findViewById(R.id.get_msg_img);
            msg_time_ly = (LinearLayout) itemView.findViewById(R.id.msg_time_ly);
            msg_time_text = (TextView) itemView.findViewById(R.id.msg_time_text);
            get_msg_img_fy = (FrameLayout) itemView.findViewById(R.id.get_msg_img_fy);
            msg_load_text = (TextView) itemView.findViewById(R.id.msg_load_text);
            msg_status_img.setVisibility(View.GONE);
            msg_load_text.setVisibility(View.GONE);
        }
    }
}
