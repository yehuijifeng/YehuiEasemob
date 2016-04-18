package com.yehui.easemob.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a.map.activity.LocationSourceActivity;
import com.a.map.contant.AMapContant;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobListActivity;
import com.yehui.easemob.adapter.MessageAdapter;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.easemob.function.BitmapCacheFunction;
import com.yehui.easemob.helper.ReceiveMessageHelper;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.service.MessageService;
import com.yehui.easemob.utils.BiaoqingUtil;
import com.yehui.easemob.utils.BitmapUtil;
import com.yehui.easemob.view.BiaoqingView;
import com.yehui.easemob.view.EditTexts;
import com.yehui.easemob.view.VoiceView;
import com.yehui.utils.activity.ImageCroppingActivity;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.PickLocalImageUtils;
import com.yehui.utils.utils.files.FileContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luhao
 * on 2016/3/8.
 * 消息队列
 */
public class MessageActivity extends EasemobListActivity implements View.OnClickListener, TextWatcher, View.OnLayoutChangeListener, View.OnTouchListener {

    private RelativeLayout msg_root_ly, start_voice_rl;
    private TextView start_voice_text, speaker_text;
    private ImageView
            voice_msg_img,
            text_msg_img,
            biaoqing_msg_img,
            often_msg_img,
            gengduo_msg_img,
            function_image_img,
            function_laction_img,
            function_shipin_img,
            function_tel_img,
            function_file_img,
            function_camera_img,
            function_video_img;
    private LinearLayout function_layout, speaker_ly;
    private Button fasong_msg_btn;
    protected String friendName, imageFileName;
    private int pageSize = 20;
    private List<MessageBean> msgList;
    private BiaoqingView biaoqing_layout;
    private EditTexts editTexts;
    private VoiceView voiceView;
    private MessageAdapter messageAdapter;
    private Handler handler = new Handler();


    @Override
    protected void initView() {
        super.initView();
        msg_root_ly = (RelativeLayout) findViewById(R.id.msg_root_ly);
        start_voice_rl = (RelativeLayout) findViewById(R.id.start_voice_rl);
        start_voice_text = (TextView) findViewById(R.id.start_voice_text);
        voice_msg_img = (ImageView) findViewById(R.id.voice_msg_img);
        text_msg_img = (ImageView) findViewById(R.id.text_msg_img);
        biaoqing_msg_img = (ImageView) findViewById(R.id.biaoqing_msg_img);
        gengduo_msg_img = (ImageView) findViewById(R.id.gengduo_msg_img);
        often_msg_img = (ImageView) findViewById(R.id.often_msg_img);
        function_layout = (LinearLayout) findViewById(R.id.function_layout);
        biaoqing_layout = (BiaoqingView) findViewById(R.id.biaoqing_layout);
        editTexts = (EditTexts) findViewById(R.id.text_msg_edit);
        fasong_msg_btn = (Button) findViewById(R.id.fasong_msg_btn);
        voiceView = (VoiceView) findViewById(R.id.voice_view);
        speaker_ly = (LinearLayout) findViewById(R.id.speaker_ly);
        speaker_text = (TextView) findViewById(R.id.speaker_text);

        function_image_img = (ImageView) findViewById(R.id.function_image_img);
        function_laction_img = (ImageView) findViewById(R.id.function_laction_img);
        function_shipin_img = (ImageView) findViewById(R.id.function_shipin_img);
        function_tel_img = (ImageView) findViewById(R.id.function_tel_img);
        function_file_img = (ImageView) findViewById(R.id.function_file_img);
        function_camera_img = (ImageView) findViewById(R.id.function_camera_img);
        function_video_img = (ImageView) findViewById(R.id.function_video_img);

        start_voice_rl.setOnTouchListener(this);//事件分发

        fasong_msg_btn.setOnClickListener(this);
        voice_msg_img.setOnClickListener(this);
        often_msg_img.setOnClickListener(this);
        text_msg_img.setOnClickListener(this);
        biaoqing_msg_img.setOnClickListener(this);
        start_voice_rl.setOnClickListener(this);
        gengduo_msg_img.setOnClickListener(this);
        editTexts.setOnClickListener(this);
        function_image_img.setOnClickListener(this);
        function_laction_img.setOnClickListener(this);
        function_shipin_img.setOnClickListener(this);
        function_tel_img.setOnClickListener(this);
        function_file_img.setOnClickListener(this);
        function_camera_img.setOnClickListener(this);
        function_video_img.setOnClickListener(this);

        editTexts.addTextChangedListener(this);//文本改变监听

        biaoqing_layout.onBiaoqingClick(editTexts);
        //添加layout大小发生改变监听器
        mRecyclerView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        setIsFastClick(false);//不启用快速点击回避
        editTexts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    showSoftInputFromWindow(editTexts);
                } else {
                    // 此处为失去焦点时的处理内容
                    hideSoftInputFromWindow(editTexts);
                }
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
            }
        });
        setIsLoadMore(false);//禁止加载更多o'n
        setIsRefresh(false);//禁止下拉刷新v
        friendName = getString(MapContant.MESSAGE_USER_NAME, null);
        mTitleView.setTitleText(friendName);
        loadingView();
        List<EMMessage> listAll = SendMessageHelper.getInstance().getEMMessageList(friendName);
        msgList = new ArrayList<>();
        if (listAll != null && listAll.size() > 0) {
            List<EMMessage> listMsg = SendMessageHelper.getInstance().getEMMessageList(friendName, listAll.get(listAll.size() - 1).getMsgId(), pageSize);
            listMsg.add(listAll.get(listAll.size() - 1));
            if (listMsg != null && listMsg.size() > 0) {
                for (EMMessage emMessage : listMsg) {
                    MessageBean messageBean = new MessageBean();
                    messageBean.setBackStatus(1);
                    messageBean.setEmMessage(emMessage);
                    messageBean.setUserName(emMessage.getUserName());
                    msgList.add(messageBean);
                }
                getWindowWidth();
            }
        }
        messageAdapter = new MessageAdapter(this, msgList, friendName, speaker_ly, speaker_text, recyclerView);
        messageAdapter.getLastHour();//将数据显示到最后一行
        setmAdapter(messageAdapter);
        loadingClose();
        ReceiveMessageHelper.getInstance().markAllMessagesAsRead(friendName);
    }

    @Override
    protected void onResume() {
        //注册消息监听
        EMChatManager.getInstance().registerEventListener(this);
        stopService(new Intent(this, MessageService.class));
        super.onResume();
        if (messageAdapter != null && messageAdapter.data != null && messageAdapter.data.size() > 0)
            messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        //无效的关闭
        EMChatManager.getInstance().unregisterEventListener(this);//注销消息监听
        super.onStop();
        messageAdapter.stopVoicePlay();
        BitmapCacheFunction.getInstance().closeLruCache();
    }

    /**
     * 发送消息回调状态
     *
     * @param messageBean
     */
    @Override
    protected void getMessageStatus(MessageBean messageBean) {
        //改变item的视图状态
        //如果回调的消息不属于任何一个消息id，说明它是一条新的消息
        if (messageAdapter.data != null && messageAdapter.data.size() > 0) {
            for (int i = messageAdapter.data.size() - 1; i > 0; i--) {
                MessageBean msg = (MessageBean) messageAdapter.data.get(i);
                if (msg.getEmMessage().getMsgId().equals(messageBean.getEmMessage().getMsgId())) {
                    messageAdapter.data.set(i, messageBean);
                    //messageAdapter.notifyDataSetChanged();
                    messageAdapter.getLastHour();
                    return;
                }
            }
        }
        messageAdapter.data.add(messageBean);
        messageAdapter.getLastHour();
        switch (messageBean.getGetMsgCode()) {
            case MessageContant.sendMsgByText://发送文本消息后状态回调
                editTexts.setText("");
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

    @Override
    public void getNewCMDMessage(MessageBean messageBean) {
        //对方撤回的一条消息
        String a = ((CmdMessageBody) messageBean.getEmMessage().getBody()).action;
        if (a.equals(MessageContant.sendRevokeMessage)) {
            messageAdapter.setRevokeMessage(messageBean);
        }
    }

    @Override
    public void getNewMessage(MessageBean messageBean) {
        //super.getNewMessage(messageBean);
        EMMessage emMessage = messageBean.getEmMessage();
        if (TextUtils.isEmpty(friendName)) return;
        if (emMessage.getFrom().equals(friendName)) {
            messageAdapter.data.add(messageBean);
            SendMessageHelper.getInstance().getMarkAsRead(friendName, emMessage, true);
            messageAdapter.getLastHour();
            setPlayMusic(false);
        } else {
            setPlayMusic(true);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        voiceView.onPressToSpeakBtnTouch(v, event, new VoiceView.EaseVoiceRecorderCallback() {
            @Override
            public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                //录音完成，发送、
                SendMessageHelper.getInstance().sendConversationByVoice(friendName, voiceFilePath, voiceTimeLength, false, null);
            }
        });
        return false;
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
                MessageBean messageBean = SendMessageHelper.getInstance().sendConversationByText(friendName, editTexts.getText().toString(), false, null);
                messageAdapter.data.add(messageBean);
                editTexts.setText("");
                recyclerView.smoothScrollToPosition(messageAdapter.data.size() - 1);
                break;
            case R.id.voice_msg_img://切换语音消息
                hideSoftInputFromWindow(editTexts);
                start_voice_rl.setVisibility(View.VISIBLE);
                text_msg_img.setVisibility(View.VISIBLE);
                biaoqing_msg_img.setVisibility(View.GONE);
                editTexts.setVisibility(View.GONE);
                voice_msg_img.setVisibility(View.GONE);
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.often_msg_img://常用语
                showLongToast("直接发送常用语，暂未开放该功能");
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.text_msg_img://切换文本消息
                start_voice_rl.setVisibility(View.GONE);
                text_msg_img.setVisibility(View.GONE);
                biaoqing_msg_img.setVisibility(View.VISIBLE);
                editTexts.setVisibility(View.VISIBLE);
                voice_msg_img.setVisibility(View.VISIBLE);
                showSoftInputFromWindow(editTexts);
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.biaoqing_msg_img://打开表情
                hideSoftInputFromWindow(editTexts);
                biaoqing_layout.setVisibility(View.VISIBLE);
                function_layout.setVisibility(View.GONE);
                //recyclerView.smoothScrollToPosition(messageAdapter.data.size() - 1);
                break;
            case R.id.start_voice_rl://按住说话
                //语音
                break;
            case R.id.gengduo_msg_img://打开更多功能
                hideSoftInputFromWindow(editTexts);
                function_layout.setVisibility(View.VISIBLE);
                biaoqing_layout.setVisibility(View.GONE);
                //recyclerView.smoothScrollToPosition(messageAdapter.data.size() - 1);
                break;
            case R.id.text_msg_edit:
                function_layout.setVisibility(View.GONE);
                biaoqing_layout.setVisibility(View.GONE);
                break;
            case R.id.ee_del:// 删除表情，当时表情时删除“[fac”的长度，是文字时删除一个长度
                BiaoqingUtil.getInstance().removeBiaoqing(editTexts);
                break;
            case R.id.function_image_img://发送图片
                PickLocalImageUtils.toAlbum(MessageActivity.this);
                break;
            case R.id.function_laction_img://发送地理位置
                startActivityForResult(LocationSourceActivity.class, AMapContant.LOCATION_GO_CODE);
                break;
            case R.id.function_shipin_img://发送视频
                PickLocalImageUtils.toVideo(MessageActivity.this, VideoActivity.class);
                break;
            case R.id.function_tel_img://请求普通电话

                break;
            case R.id.function_file_img://发送文件
                PickLocalImageUtils.toFile(MessageActivity.this);
                break;
            case R.id.function_camera_img://打开相机
                imageFileName = DateUtil.format(System.currentTimeMillis(), "'" + friendName + "'" + "_yyyyMMddHHmmss") + ".jpg";
                PickLocalImageUtils.toCamera(MessageActivity.this, imageFileName);
                break;
            case R.id.function_video_img://请求视频通话

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imagePath;
            String videoPath;
            switch (requestCode) {
                case PickLocalImageUtils.CODE_FOR_ALBUM://来自于系统相册的回调
                    if (data == null) return;
                    Uri uri = data.getData();
                    imagePath = PickLocalImageUtils.getPath(uri, getContentResolver());
                    SendMessageHelper.getInstance().sendConversationByImage(friendName, imagePath, false, false, null);
                    break;
                case PickLocalImageUtils.CODE_FOR_CAMERA://来自于系统相机的回调
                    imagePath = FileContact.YEHUI_SAVE_IMG_PATH + imageFileName;
                    SendMessageHelper.getInstance().sendConversationByImage(friendName, imagePath, false, false, null);
                    //PickLocalImageUtils.toCrop(this, imagePath);
                    break;
                case PickLocalImageUtils.CODE_FOR_CROP://来自于剪切照片的回调
                    if (data == null) return;
                    imagePath = data.getStringExtra(ImageCroppingActivity.KEY_SAVE_IMAGE_PATH);
                    Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(imagePath, 100, 100);
                    BitmapUtil.saveBitmap(bitmap, imagePath, 100);
                    break;
                case PickLocalImageUtils.CODE_FOR_VIDEO://选择视频的回调
                    if (data == null) return;
                    videoPath = data.getStringExtra(VideoActivity.KEY_SAVE_VIDEO_PATH);
                    SendMessageHelper.getInstance().sendConversationByVideo(friendName, videoPath, false, null);
                    break;
            }
        } else if (resultCode == AMapContant.LOCATION_BACK_CODE) {
            switch (requestCode) {
                case AMapContant.LOCATION_GO_CODE://来自于地图的回调
                    if (data == null) return;
                    String location = data.getStringExtra(AMapContant.AMAP_LOCATION);
                    double lo_long = data.getDoubleExtra(AMapContant.LOCATION_LONG, 0);
                    double lo_lat = data.getDoubleExtra(AMapContant.LOCATION_LAT, 0);
                    SendMessageHelper.getInstance().sendConversationByLocation(friendName, location, lo_long, lo_lat, false, null);
                    break;
            }
        }
    }

    /**
     * 显示发送按钮
     */
    private void showSendBtn() {
        if (editTexts.length() <= 0) {
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
    public void afterTextChanged(final Editable s) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSendBtn();
                if (editTexts.isCopy()) {
                    editTexts.setText(BiaoqingUtil.getInstance().showBiaoqing(MessageActivity.this, s.toString()));
                    editTexts.setCopy(false);
                }
            }
        }, 10);

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
        keyHeight = getWindowHeight() / 4;
        if (messageAdapter == null || messageAdapter.data == null || messageAdapter.data.size() == 0)
            return;
        //现在认为只要控件将Activity向上推的高度超过了1/4屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            recyclerView.smoothScrollToPosition(messageAdapter.data.size() - 1);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            recyclerView.smoothScrollToPosition(messageAdapter.data.size() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**************************
     * 废弃代码
     ********************************************************************/

    @Override
    protected int getItemLayoutById(int resId) {
        return 0;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return null;
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {

    }

    /**************************
     * 废弃代码
     ********************************************************************/
}
