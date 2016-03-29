package com.yehui.easemob.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.yehui.easemob.R;
import com.yehui.easemob.model.EaseChatRowVoicePlayClickListener;
import com.yehui.easemob.model.EaseVoiceRecorder;
import com.yehui.utils.utils.files.FileOperationUtil;



/**
 * Created by Luhao on 2016/3/23.
 * 语音控件
 */
public class VoiceView extends RelativeLayout {

    protected Drawable[] micImages;
    private ImageView voice_img;
    private TextView voice_text;
    private View root;
    protected PowerManager.WakeLock wakeLock;//避免屏幕因为录音时间过长而锁屏
    protected EaseVoiceRecorder voiceRecorder;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            voice_img.setImageDrawable(micImages[msg.what]);
        }
    };

    public VoiceView(Context context) {
        super(context);
        initView();
    }

    public VoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        root = LayoutInflater.from(getContext()).inflate(R.layout.layout_voice, this);
        voice_img = (ImageView) root.findViewById(R.id.voice_img);
        voice_text = (TextView) root.findViewById(R.id.voice_text);
        voiceRecorder = new EaseVoiceRecorder(micImageHandler);//根据麦克风音量的大小来调整动画
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05),
                getResources().getDrawable(R.drawable.ease_record_animate_06),
                getResources().getDrawable(R.drawable.ease_record_animate_07),
                getResources().getDrawable(R.drawable.ease_record_animate_08),
                getResources().getDrawable(R.drawable.ease_record_animate_09),
                getResources().getDrawable(R.drawable.ease_record_animate_10),
                getResources().getDrawable(R.drawable.ease_record_animate_11),
                getResources().getDrawable(R.drawable.ease_record_animate_12),
                getResources().getDrawable(R.drawable.ease_record_animate_13),
                getResources().getDrawable(R.drawable.ease_record_animate_14),};

        wakeLock = ((PowerManager) getContext().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "easemob");
    }


    /**
     * 长按说话按钮touch事件
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    this.setVisibility(VISIBLE);
                    if (EaseChatRowVoicePlayClickListener.isPlaying)
                        EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                this.setVisibility(GONE);
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_NOT_FOUND) {
                            Toast.makeText(getContext(), "记录未经许可", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "录音时间太短了", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "发送失败,请测试是否服务器连接", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    /**
     * 缓存录音机回调接口
     */
    public interface EaseVoiceRecorderCallback {
        /**
         * 录音完毕
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!FileOperationUtil.isSDCardEnable()) {
            Toast.makeText(getContext(), "发送语音需要sdcard支持", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            voice_text.setText("手指松开，发送语音");
            voice_text.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(getContext());
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "录音失败,请再试一次!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        voice_text.setText("手指上划，取消发送");
        voice_text.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);
    }

    public void showMoveUpToCancelHint() {

        voice_text.setText("手指松开，发送语音");
        voice_text.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
