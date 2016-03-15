package com.yehui.easemob.activity;


import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobListActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.BiaoqingMap;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.utils.LogUtil;
import com.yehui.utils.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luhao on 2016/3/8.
 */
public class MessageActivity extends EasemobListActivity implements View.OnClickListener, TextWatcher, View.OnLayoutChangeListener, EMEventListener {

    private RelativeLayout msg_root_ly;
    private TextView start_voice_text;
    private ImageView
            voice_msg_img,
            text_msg_img,
            biaoqing_msg_img,
            often_msg_img,
            gengduo_msg_img;
    private LinearLayout function_layout, biaoqing_layout;
    private EditText text_msg_edit;
    private Button fasong_msg_btn;
    private String friendName;
    private int pageSize = 20;
    private List<MessageBean> msgList;
    private ImageView
            ee_1,
            ee_2,
            ee_3,
            ee_4,
            ee_5,
            ee_del;

    @Override
    protected void initView() {
        super.initView();
        msg_root_ly = (RelativeLayout) findViewById(R.id.msg_root_ly);
        start_voice_text = (TextView) findViewById(R.id.start_voice_text);
        voice_msg_img = (ImageView) findViewById(R.id.voice_msg_img);
        text_msg_img = (ImageView) findViewById(R.id.text_msg_img);
        biaoqing_msg_img = (ImageView) findViewById(R.id.biaoqing_msg_img);
        gengduo_msg_img = (ImageView) findViewById(R.id.gengduo_msg_img);
        often_msg_img = (ImageView) findViewById(R.id.often_msg_img);
        function_layout = (LinearLayout) findViewById(R.id.function_layout);
        biaoqing_layout = (LinearLayout) findViewById(R.id.biaoqing_layout);
        text_msg_edit = (EditText) findViewById(R.id.text_msg_edit);
        fasong_msg_btn = (Button) findViewById(R.id.fasong_msg_btn);
        ee_1 = (ImageView) biaoqing_layout.findViewById(R.id.ee_1);
        ee_2 = (ImageView) biaoqing_layout.findViewById(R.id.ee_2);
        ee_3 = (ImageView) biaoqing_layout.findViewById(R.id.ee_3);
        ee_4 = (ImageView) biaoqing_layout.findViewById(R.id.ee_4);
        ee_5 = (ImageView) biaoqing_layout.findViewById(R.id.ee_5);
        ee_del = (ImageView) biaoqing_layout.findViewById(R.id.ee_del);

        ee_1.setOnClickListener(this);
        ee_2.setOnClickListener(this);
        ee_3.setOnClickListener(this);
        ee_4.setOnClickListener(this);
        ee_5.setOnClickListener(this);
        ee_del.setOnClickListener(this);

        fasong_msg_btn.setOnClickListener(this);
        voice_msg_img.setOnClickListener(this);
        often_msg_img.setOnClickListener(this);
        text_msg_img.setOnClickListener(this);
        biaoqing_msg_img.setOnClickListener(this);
        start_voice_text.setOnClickListener(this);
        gengduo_msg_img.setOnClickListener(this);
        text_msg_edit.setOnClickListener(this);

        text_msg_edit.addTextChangedListener(this);

        //添加layout大小发生改变监听器
        msg_root_ly.addOnLayoutChangeListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        text_msg_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    showSoftInputFromWindow(text_msg_edit);
                } else {
                    // 此处为失去焦点时的处理内容
                    hideSoftInputFromWindow(text_msg_edit);
                }
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
            }
        });
        setIsLoadMore(false);//禁止加载更多
        setIsRefresh(false);//禁止下拉刷新
        loadingView();
        friendName = getString(MapContant.MESSAGE_USER_NAME, null);
        List<EMMessage> listAll = SendMessageHelper.getInstance().getEMMessageList(friendName);
        if (listAll != null && listAll.size() > 0) {
            List<EMMessage> listMsg = SendMessageHelper.getInstance().getEMMessageList(friendName, listAll.get(listAll.size() - 1).getMsgId(), pageSize);
            msgList = new ArrayList<>();
            for (EMMessage emMessage : listMsg) {
                MessageBean messageBean = new MessageBean();
                messageBean.setBackStatus(1);
                messageBean.setEmMessage(emMessage);
                messageBean.setUserName(emMessage.getUserName());
                msgList.add(messageBean);
            }
            addAll(msgList);
            notifyDataChange();
            //让控件显示最后一行数据
            recyclerView.smoothScrollToPosition(data.size() - 1);
        }
        loadingClose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册消息监听
        //EMChatManager.getInstance().registerEventListener(this,
        //new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
        EMChatManager.getInstance().registerEventListener(this);
        if (data != null || data.size() > 0)
            notifyDataChange();
    }

    @Override
    protected void onStop() {
        EMChatManager.getInstance().unregisterEventListener(this);//注销消息监听
        super.onStop();
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
            case EventNewMessage://接收新消息event注册
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNew);
                getMessageByType(messageBean);
                break;
            case EventNewCMDMessage://接收透传event注册
                LogUtil.d("收到透传消息");
                messageBean.setEmMessage(message);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByNewCMDM);
                // 获取消息body
                CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action;// 获取自定义action
                break;
            case EventDeliveryAck://已发送回执event注册
                message.setDelivered(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByDeliveryAck);
                break;
            case EventReadAck://已读回执event注册
                message.setAcked(true);
                messageBean.setGetMsgCode(MessageContant.receiveMsgByReadAck);
                break;
            default:
                messageBean.setEmMessage(message);
                break;
        }

    }

    /**
     * 图文混排
     * 该方法获得的是资源原始大小
     */
    private void addImageByText(EditText editText, int drawableId, int imgId) {
        SpannableString spannableString = new SpannableString("[ee_" + imgId + "]");
        //获取Drawable资源
        Drawable drawable = getResourceDrawable(drawableId);
        //这句话必须，不然图片不显示
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //创建ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //用ImageSpan替换文本
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable e = editText.getText();
        int st = editText.getSelectionStart();
        int en = editText.getSelectionEnd();
        e.replace(st, en, spannableString);
        //editText.setMovementMethod(LinkMovementMethod.getInstance()); //实现文本的滚动
    }

    /**
     * 该方法直接获得显示图片的大小
     *
     * @param editText
     * @param drawable
     */
    private void addImageByText(EditText editText, Drawable drawable) {
        SpannableString spannableString = new SpannableString("[ee_biaoqing]");
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);//插入图
        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable e = editText.getText();
        int st = editText.getSelectionStart();
        int en = editText.getSelectionEnd();
        e.replace(st, en, spannableString);
    }

    /**
     * 删除表情
     */
    private void deleteImageByText() {
        int selectionStart = text_msg_edit.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = text_msg_edit.getText().toString();
            if (!TextUtils.isEmpty(body)) {
                String tempStr = body.substring(0, selectionStart);
                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                if (i != -1) {
                    CharSequence cs = tempStr.subSequence(i, selectionStart);
                    if (cs.equals("[ee_")) {// 判断是否是一个表情
                        text_msg_edit.getEditableText().delete(i, selectionStart);
                        return;
                    }
                }
                text_msg_edit.getEditableText().delete(tempStr.length() - 1,
                        selectionStart);
            }
        }
    }

    @Override
    protected int getItemType(int position) {
        MessageBean messageBean = (MessageBean) data.get(position);
        EMMessage emMessage = messageBean.getEmMessage();
        if (emMessage.getType() == EMMessage.Type.TXT ||
                emMessage.getType() == EMMessage.Type.IMAGE ||
                emMessage.getType() == EMMessage.Type.LOCATION ||
                emMessage.getType() == EMMessage.Type.FILE) {
            if (emMessage.direct == EMMessage.Direct.SEND) //判断这条消息是否是发送消息
                return MessageContant.sendMsgByText;
            else
                return MessageContant.receiveMsgByText;
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {
            if (emMessage.direct == EMMessage.Direct.SEND)
                return MessageContant.sendMsgByVoice;
            else
                return MessageContant.receiveMsgByVoice;
        }
        return MessageContant.sendMsgByText;
    }

    /**
     * 添加聊天记录
     */
    private void addMessageRecord() {
        if (SendMessageHelper.getInstance().getEMMessageList(friendName) != null && SendMessageHelper.getInstance().getEMMessageList(friendName).size() > 0) {
            if (SendMessageHelper.getInstance().getEMMessageList(friendName).size() <= data.size())
                return;
            List<EMMessage> list = SendMessageHelper.getInstance().getEMMessageList(friendName, SendMessageHelper.getInstance().getEMMessageList(friendName).get(SendMessageHelper.getInstance().getEMMessageList(friendName).size() - data.size()).getMsgId(), pageSize);
            msgList = new ArrayList<>();
            for (EMMessage emMessage : list) {
                MessageBean messageBean = new MessageBean();
                messageBean.setBackStatus(1);
                messageBean.setEmMessage(emMessage);
                messageBean.setUserName(emMessage.getUserName());
                msgList.add(messageBean);
            }
            addAll(msgList);
        }
    }

    /**
     * 发送消息回调状态
     *
     * @param getMessageBean
     */
    private String msgId = "";

    @Override
    protected void getMessageStatus(MessageBean messageBean) {
        switch (messageBean.getGetMsgCode()) {
            case MessageContant.sendMsgByText://发送文本消息后状态回调
                //改变item的视图状态
                //如果回调的消息不属于任何一个消息id，说明它是一条新的消息
                if (!messageBean.getEmMessage().getMsgId().equals(msgId)) {
                    msgId = messageBean.getEmMessage().getMsgId();
                    if (data == null || data.size() == 0) {
                        List<MessageBean> msgList = new ArrayList<>();
                        msgList.add(messageBean);
                        addAll(msgList);
                        notifyDataChange();
                    } else {
                        addOne(messageBean, data.size());
                        text_msg_edit.setText("");
                        recyclerView.smoothScrollToPosition(data.size() - 1);
                    }
                } else {
                    for (int i = data.size(); i > 0; i--) {
                        MessageBean msg = (MessageBean) data.get(i);
                        if (msg.getEmMessage().getMsgId().equals(msgId)) {
                            data.add(i, msg);
                            mAdapter.notifyItemInserted(i);//精确改变
                            break;
                        }
                    }

                }
                break;
            case MessageContant.sendMsgByVoice://语音

                break;
            case MessageContant.sendMsgByImage://图片

                break;
            case MessageContant.sendMsgByLocation://地理位置

                break;
            case MessageContant.sendMsgByFile://文件

                break;
            case MessageContant.receiveMsgByAll://接收所有消息
                break;
            case MessageContant.receiveMsgByNew://接收新消息
                break;
            case MessageContant.receiveMsgByDeliveryAck://已发送
                break;
            case MessageContant.receiveMsgByOffline://接收离线消息
                break;
            case MessageContant.receiveMsgByNewCMDM://透传消息
                break;
            case MessageContant.receiveMsgByReadAck://已读
                break;
            case MessageContant.receiveMsgByListChanged://会话列表改变
                break;
        }
    }

    /**
     * 新消息类型解析
     *
     * @param messageBean
     */
    private void getMessageByType(MessageBean messageBean) {
        EMMessage emMessage = messageBean.getEmMessage();
        if (emMessage.getType() == EMMessage.Type.TXT) {//文本消息
            addOne(messageBean, data.size());
            recyclerView.smoothScrollToPosition(data.size());
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {//语音消息

        } else if (emMessage.getType() == EMMessage.Type.LOCATION) {//地理位置

        } else if (emMessage.getType() == EMMessage.Type.IMAGE) {//图片

        } else if (emMessage.getType() == EMMessage.Type.FILE) {//文件

        } else if (emMessage.getType() == EMMessage.Type.VIDEO) {//视频

        } else if (emMessage.getType() == EMMessage.Type.CMD) {//视频

        }
    }

    /**
     * 根据每一行item的数据不同返回不同的视图
     *
     * @param resId
     * @return
     */
    @Override
    protected int getItemLayoutById(int resId) {
        int viewId = 0;
        switch (resId) {
            case MessageContant.sendMsgByText:
            case MessageContant.sendMsgByImage://图片
            case MessageContant.sendMsgByLocation://地理位置
            case MessageContant.sendMsgByFile://文件
                viewId = R.layout.item_set_msg_text;
                break;
            case MessageContant.sendMsgByVoice://语音
                viewId = R.layout.item_set_msg_voice;
                break;
            case MessageContant.receiveMsgByText:
            case MessageContant.receiveMsgByImage:
            case MessageContant.receiveMsgByLocation:
            case MessageContant.receiveMsgByFile:
                viewId = R.layout.item_get_msg_text;
                break;
            case MessageContant.receiveMsgByVoice:
                viewId = R.layout.item_get_msg_voice;
                break;
        }
        return viewId;
    }

    private ReceiveVoiceViewHolder receiveVoiceViewHolder;
    private SendVoiceViewHolder sendVoiceViewHolder;
    private ReceiveTextViewHolder receiveTextViewHolder;
    private SendTextViewHolder sendTextViewHolder;

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        final MessageBean messageBean = (MessageBean) data.get(position);
        EMMessage emMessage = messageBean.getEmMessage();
        if (emMessage.getType() == EMMessage.Type.VOICE) {//语音消息
            if (emMessage.direct == EMMessage.Direct.SEND) {
                receiveVoiceViewHolder = (ReceiveVoiceViewHolder) holder;
            } else {
                sendVoiceViewHolder = (SendVoiceViewHolder) holder;
            }
        } else {
            if (emMessage.getType() == EMMessage.Type.TXT) {//文本消息
                final TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
                if (emMessage.direct == EMMessage.Direct.SEND) {//判断这条消息是否是发送消息
                    sendTextViewHolder = (SendTextViewHolder) holder;
                    sendTextViewHolder.set_msg_text.setText(textMessageBody.getMessage());
                    if (messageBean.getBackStatus() == 0) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.VISIBLE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (messageBean.getBackStatus() == 1) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (messageBean.getBackStatus() == -1) {
                        sendTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        sendTextViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                        sendTextViewHolder.msg_status_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SendMessageHelper.getInstance().getConversationByText(friendName, textMessageBody.getMessage());
                            }
                        });
                    }
                    sendTextViewHolder.set_msg_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //用户详情
                            showShortToast("用户详情" + messageBean.getEmMessage().getUserName());
                        }
                    });
                    //text_msg_edit.setText("");
                } else {
                    receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                    receiveTextViewHolder.get_msg_text.setText(textMessageBody.getMessage());
                    receiveTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                    receiveTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    receiveTextViewHolder.get_msg_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //用户详情
                            showShortToast("用户详情" + messageBean.getEmMessage().getUserName());
                        }
                    });
                }
            } else if (emMessage.getType() == EMMessage.Type.LOCATION) {//地理位置
                if (emMessage.getUserName().equals(friendName)) {
                    receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                } else {
                    sendTextViewHolder = (SendTextViewHolder) holder;
                }
            } else if (emMessage.getType() == EMMessage.Type.IMAGE) {//图片
                if (emMessage.getUserName().equals(friendName)) {
                    receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                } else {
                    sendTextViewHolder = (SendTextViewHolder) holder;
                }
            } else if (emMessage.getType() == EMMessage.Type.FILE) {//文件
                if (emMessage.getUserName().equals(friendName)) {
                    receiveTextViewHolder = (ReceiveTextViewHolder) holder;
                } else {
                    sendTextViewHolder = (SendTextViewHolder) holder;
                }
            }
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        if (type == MessageContant.sendMsgByText || type ==
                MessageContant.sendMsgByImage || type ==
                MessageContant.sendMsgByLocation || type ==
                MessageContant.sendMsgByFile) {
            return new SendTextViewHolder(itemView);
        } else if (type == MessageContant.receiveMsgByText || type ==
                MessageContant.receiveMsgByImage || type ==
                MessageContant.receiveMsgByLocation || type ==
                MessageContant.receiveMsgByFile) {
            return new ReceiveTextViewHolder(itemView);
        } else if (type == MessageContant.sendMsgByVoice) {
            return new SendVoiceViewHolder(itemView);
        } else if (type == MessageContant.receiveMsgByVoice) {
            return new ReceiveVoiceViewHolder(itemView);
        }
        return null;
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {
        return;
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected String setTitleText() {
        return EasemobAppliaction.user.getUserName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fasong_msg_btn://发送消息
                SendMessageHelper.getInstance().getConversationByText(friendName, text_msg_edit.getText().toString());
                break;
            case R.id.voice_msg_img://切换语音消息
                hideSoftInputFromWindow(text_msg_edit);
                start_voice_text.setVisibility(View.VISIBLE);
                text_msg_img.setVisibility(View.VISIBLE);
                biaoqing_msg_img.setVisibility(View.GONE);
                text_msg_edit.setVisibility(View.GONE);
                voice_msg_img.setVisibility(View.GONE);
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.often_msg_img://常用语
                showLongToast("暂未开放该功能");
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.text_msg_img://切换文本消息
                start_voice_text.setVisibility(View.GONE);
                text_msg_img.setVisibility(View.GONE);
                biaoqing_msg_img.setVisibility(View.VISIBLE);
                text_msg_edit.setVisibility(View.VISIBLE);
                voice_msg_img.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(text_msg_edit);
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.biaoqing_msg_img://打开表情
                hideSoftInputFromWindow(text_msg_edit);
                biaoqing_layout.setVisibility(View.VISIBLE);
                function_layout.setVisibility(View.GONE);
                break;
            case R.id.start_voice_text://按住说话
                //语音
                break;
            case R.id.gengduo_msg_img://打开更多功能
                hideSoftInputFromWindow(text_msg_edit);
                function_layout.setVisibility(View.VISIBLE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.text_msg_edit:
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.ee_1:
                addImageByText(text_msg_edit, ee_1.getDrawable());
            case R.id.ee_2:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_2), 2);
                break;
            case R.id.ee_3:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_3), 3);
                break;
            case R.id.ee_4:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_4), 4);
                break;
            case R.id.ee_5:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_5), 5);
                break;
            case R.id.ee_del:// 删除表情，当时表情时删除“[fac”的长度，是文字时删除一个长度
                deleteImageByText();
                break;
        }
    }

    /**
     * 显示发送按钮
     */
    private void showSendBtn() {
        if (text_msg_edit.length() <= 0) {
            fasong_msg_btn.setVisibility(View.GONE);
            gengduo_msg_img.setVisibility(View.VISIBLE);
        } else {
            fasong_msg_btn.setVisibility(View.VISIBLE);
            gengduo_msg_img.setVisibility(View.GONE);
        }
    }

    /**
     * 在文本改变之前
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        showSendBtn();
    }

    /**
     * 文本改变了
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 文本改变之后
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        showSendBtn();
    }

    /**
     * 布局状态改变监听
     *
     * @param v
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param oldLeft
     * @param oldTop
     * @param oldRight
     * @param oldBottom
     */
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值

//      System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
//      System.out.println(left + " " + top +" " + right + " " + bottom);

        keyHeight = getWindowHeight() / 3;
        if (data == null || data.size() == 0) return;
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            LogUtil.i("监听到软键盘弹起");
            recyclerView.smoothScrollToPosition(data.size() - 1);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            LogUtil.i("监听到软件盘关闭");
            recyclerView.smoothScrollToPosition(data.size() - 1);
        }
    }


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
        private ImageView msg_status_img, msg_voice_is_open;

        public ReceiveVoiceViewHolder(View itemView) {
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
     * 好友发来的语音类消息
     */
    private class SendVoiceViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text, msg_voice_length;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open;

        public SendVoiceViewHolder(View itemView) {
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
