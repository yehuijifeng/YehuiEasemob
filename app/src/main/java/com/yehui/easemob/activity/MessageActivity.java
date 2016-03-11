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
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobListActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.GetMessageBean;
import com.yehui.easemob.contants.BiaoqingMap;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.helper.GetMessageHelper;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luhao on 2016/3/8.
 */
public class MessageActivity extends EasemobListActivity implements View.OnClickListener, TextWatcher {

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
    private List<GetMessageBean> msgList;
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
        setIsLoadMore(false);
        loadingView();
        friendName = getString(MapContant.MESSAGE_USER_NAME, null);
        List<EMMessage> listAll = GetMessageHelper.getInstance().getEMMessageList(friendName);
        if (listAll != null && listAll.size() > 0) {
            List<EMMessage> listMsg = GetMessageHelper.getInstance().getEMMessageList(friendName, listAll.get(listAll.size() - 1).getMsgId(), pageSize);
            msgList = new ArrayList<>();
            for (EMMessage emMessage : listMsg) {
                GetMessageBean getMessageBean = new GetMessageBean();
                getMessageBean.setBackStatus(1);
                getMessageBean.setEmMessage(emMessage);
                getMessageBean.setUserName(emMessage.getUserName());
                msgList.add(getMessageBean);
            }
            addAll(msgList);
            notifyDataChange();
        }
        loadingClose();

    }

    /**
     * 图文混排
     * 该方法获得的是资源原始大小
     */

    private void addImageByText(EditText editText, int drawableId) {
        SpannableString spannableString = new SpannableString("[ee_biaoqing]");
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
                    if (cs.equals("[ee_biaoqing]")) {// 判断是否是一个表情
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
        GetMessageBean getMessageBean = (GetMessageBean) data.get(position);
        EMMessage emMessage = getMessageBean.getEmMessage();
        if (emMessage.getType() == EMMessage.Type.TXT ||
                emMessage.getType() == EMMessage.Type.IMAGE ||
                emMessage.getType() == EMMessage.Type.LOCATION ||
                emMessage.getType() == EMMessage.Type.FILE) {
            if (emMessage.direct == EMMessage.Direct.SEND) //判断这条消息是否是发送消息
                return MessageContant.getMsgByText;
            else
                return MessageContant.setMsgByText;
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {
            if (emMessage.direct == EMMessage.Direct.SEND)
                return MessageContant.getMsgByVoice;
            else
                return MessageContant.setMsgByVoice;
        }
        return MessageContant.getMsgByText;
    }

    /**
     * 添加聊天记录
     */
    private void addMessageRecord() {
        if (GetMessageHelper.getInstance().getEMMessageList(friendName) != null && GetMessageHelper.getInstance().getEMMessageList(friendName).size() > 0) {
            if (GetMessageHelper.getInstance().getEMMessageList(friendName).size() <= data.size())
                return;
            List<EMMessage> list = GetMessageHelper.getInstance().getEMMessageList(friendName, GetMessageHelper.getInstance().getEMMessageList(friendName).get(GetMessageHelper.getInstance().getEMMessageList(friendName).size() - data.size()).getMsgId(), pageSize);
            msgList = new ArrayList<>();
            for (EMMessage emMessage : list) {
                GetMessageBean getMessageBean = new GetMessageBean();
                getMessageBean.setBackStatus(1);
                getMessageBean.setEmMessage(emMessage);
                getMessageBean.setUserName(emMessage.getUserName());
                msgList.add(getMessageBean);
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
    protected void getMessageStatus(GetMessageBean getMessageBean) {
        switch (getMessageBean.getGetMsgCode()) {
            case MessageContant.getMsgByText://发送文本消息后状态回调
                //改变item的视图状态
                //如果回调的消息不属于任何一个消息id，说明它是一条新的消息
                if (!getMessageBean.getEmMessage().getMsgId().equals(msgId)) {

                    msgId = getMessageBean.getEmMessage().getMsgId();
                    if (data == null || data.size() == 0) {
                        List<GetMessageBean> msgList = new ArrayList<>();
                        msgList.add(getMessageBean);
                        addAll(msgList);
                        notifyDataChange();
                    } else
                        addOne(getMessageBean.getEmMessage(), data.size());

                } else {
                    for (int i = data.size(); i > 0; i--) {
                        GetMessageBean msg = (GetMessageBean) data.get(i);
                        if (msg.getEmMessage().getMsgId().equals(msgId)) {
                            data.add(i, msg);
                            mAdapter.notifyItemInserted(i);//精确改变
                            break;
                        }
                    }

                }
                break;
            case MessageContant.getMsgByVoice://语音

                break;
            case MessageContant.getMsgByImage://图片

                break;
            case MessageContant.getMsgByLocation://地理位置

                break;
            case MessageContant.getMsgByFile://文件

                break;
            case MessageContant.setMsgByAll://接收所有消息
                break;
            case MessageContant.setMsgByNew://接收新消息
                EMMessage emMessage = getMessageBean.getEmMessage();
                getMessageByType(emMessage);
                break;
            case MessageContant.setMsgByDeliveryAck://已发送
                break;
            case MessageContant.setMsgByOffline://接收离线消息
                break;
            case MessageContant.setMsgByNewCMDM://透传消息
                break;
            case MessageContant.setMsgByReadAck://已读
                break;
            case MessageContant.setMsgByListChanged://会话列表改变
                break;
        }
    }

    /**
     * 新消息类型解析
     *
     * @param emMessage
     */
    private void getMessageByType(EMMessage emMessage) {
        if (emMessage.getType() == EMMessage.Type.TXT) {//文本消息
            TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
            addOne(textMessageBody.getMessage(), data.size());
            getTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
            getTextViewHolder.msg_status_img.setVisibility(View.GONE);
            getTextViewHolder.get_msg_text.setText(textMessageBody.getMessage());
        } else if (emMessage.getType() == EMMessage.Type.VOICE) {//语音消息

        } else if (emMessage.getType() == EMMessage.Type.LOCATION) {//地理位置

        } else if (emMessage.getType() == EMMessage.Type.IMAGE) {//图片

        } else if (emMessage.getType() == EMMessage.Type.FILE) {//文件

        } else if (emMessage.getType() == EMMessage.Type.VIDEO) {//视频

        } else if (emMessage.getType() == EMMessage.Type.CMD) {//视频

        }
    }

    @Override
    protected int getItemLayoutById(int resId) {
        int viewId = 0;
        switch (resId) {
            case MessageContant.getMsgByText:
            case MessageContant.getMsgByImage://图片
            case MessageContant.getMsgByLocation://地理位置
            case MessageContant.getMsgByFile://文件
                viewId = R.layout.item_set_msg_text;
                break;
            case MessageContant.getMsgByVoice://语音
                viewId = R.layout.item_set_msg_voice;
                break;
            case MessageContant.setMsgByText:
            case MessageContant.setMsgByImage:
            case MessageContant.setMsgByLocation:
            case MessageContant.setMsgByFile:
                viewId = R.layout.item_get_msg_text;
                break;
            case MessageContant.setMsgByVoice:
                viewId = R.layout.item_get_msg_voice;
                break;
        }
        return viewId;
    }

    private GetVoiceViewHolder getVoiceViewHolder;
    private SetVoiceViewHolder setVoiceViewHolder;
    private GetTextViewHolder getTextViewHolder;
    private SetTextViewHolder setTextViewHolder;

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        GetMessageBean getMessageBean = (GetMessageBean) data.get(position);
        EMMessage emMessage = getMessageBean.getEmMessage();

        if (emMessage.getType() == EMMessage.Type.VOICE) {//语音消息
            if (emMessage.direct == EMMessage.Direct.SEND) {
                getVoiceViewHolder = (GetVoiceViewHolder) holder;
            } else {
                setVoiceViewHolder = (SetVoiceViewHolder) holder;
            }
        } else {
            if (emMessage.getType() == EMMessage.Type.TXT) {//文本消息
                final TextMessageBody textMessageBody = (TextMessageBody) emMessage.getBody();
                if (emMessage.direct == EMMessage.Direct.SEND) {//判断这条消息是否是发送消息
                    setTextViewHolder = (SetTextViewHolder) holder;
                    setTextViewHolder.set_msg_text.setText(textMessageBody.getMessage());
                    if (getMessageBean.getBackStatus() == 0) {
                        setTextViewHolder.msg_progress_bar.setVisibility(View.VISIBLE);
                        setTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (getMessageBean.getBackStatus() == 1) {
                        setTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        setTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (getMessageBean.getBackStatus() == -1) {
                        setTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        setTextViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                        setTextViewHolder.set_msg_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetMessageHelper.getInstance().getConversationByText(friendName, textMessageBody.getMessage());
                            }
                        });
                    }
                } else {
                    getTextViewHolder = (GetTextViewHolder) holder;
                    getTextViewHolder.get_msg_text.setText(textMessageBody.getMessage());
                    if (getMessageBean.getBackStatus() == 0) {
                        getTextViewHolder.msg_progress_bar.setVisibility(View.VISIBLE);
                        getTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (getMessageBean.getBackStatus() == 1) {
                        getTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        getTextViewHolder.msg_status_img.setVisibility(View.GONE);
                    } else if (getMessageBean.getBackStatus() == -1) {
                        getTextViewHolder.msg_progress_bar.setVisibility(View.GONE);
                        getTextViewHolder.msg_status_img.setVisibility(View.VISIBLE);
                        getTextViewHolder.get_msg_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetMessageHelper.getInstance().getConversationByText(friendName, textMessageBody.getMessage());
                            }
                        });
                    }
                }
            } else if (emMessage.getType() == EMMessage.Type.LOCATION) {//地理位置
                if (emMessage.getUserName().equals(friendName)) {
                    getTextViewHolder = (GetTextViewHolder) holder;
                } else {
                    setTextViewHolder = (SetTextViewHolder) holder;
                }
            } else if (emMessage.getType() == EMMessage.Type.IMAGE) {//图片
                if (emMessage.getUserName().equals(friendName)) {
                    getTextViewHolder = (GetTextViewHolder) holder;
                } else {
                    setTextViewHolder = (SetTextViewHolder) holder;
                }
            } else if (emMessage.getType() == EMMessage.Type.FILE) {//文件
                if (emMessage.getUserName().equals(friendName)) {
                    getTextViewHolder = (GetTextViewHolder) holder;
                } else {
                    setTextViewHolder = (SetTextViewHolder) holder;
                }
            }
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        if (type == MessageContant.getMsgByText || type ==
                MessageContant.getMsgByImage || type ==
                MessageContant.getMsgByLocation || type ==
                MessageContant.getMsgByFile) {
            return new SetTextViewHolder(itemView);
        } else if (type == MessageContant.setMsgByText || type ==
                MessageContant.setMsgByImage || type ==
                MessageContant.setMsgByLocation || type ==
                MessageContant.setMsgByFile) {
            return new GetTextViewHolder(itemView);
        } else if (type == MessageContant.getMsgByVoice) {
            return new SetVoiceViewHolder(itemView);
        } else if (type == MessageContant.setMsgByVoice) {
            return new GetVoiceViewHolder(itemView);
        }
        return null;
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {

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
                GetMessageHelper.getInstance().getConversationByText(friendName, text_msg_edit.getText().toString());
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
                break;
            case R.id.ee_2:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_2));
                break;
            case R.id.ee_3:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_3));
                break;
            case R.id.ee_4:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_4));
                break;
            case R.id.ee_5:
                addImageByText(text_msg_edit, BiaoqingMap.getInstance().getBiaoqingMap().get(BiaoqingMap.getInstance().ee_5));
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
     * 好友发来的文本类消息
     */
    private class GetTextViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;

        public GetTextViewHolder(View itemView) {
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
    private class SetTextViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img;

        public SetTextViewHolder(View itemView) {
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
     * 发送给好友的语音类消息
     */
    private class GetVoiceViewHolder extends BaseViewHolder {
        private CircularImageView get_msg_image;
        private TextView get_msg_text, msg_voice_length;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open;

        public GetVoiceViewHolder(View itemView) {
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
    private class SetVoiceViewHolder extends BaseViewHolder {
        private CircularImageView set_msg_image;
        private TextView set_msg_text, msg_voice_length;
        private ProgressBar msg_progress_bar;
        private ImageView msg_status_img, msg_voice_is_open;

        public SetVoiceViewHolder(View itemView) {
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
