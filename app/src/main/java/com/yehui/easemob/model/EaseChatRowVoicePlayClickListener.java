/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yehui.easemob.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.VoiceMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.utils.SpeakerUtil;
import com.yehui.utils.adapter.base.BaseAdapter;
import com.yehui.utils.utils.LogUtil;

import java.io.File;

/**
 * 语音row播放点击事件监听
 */
public class EaseChatRowVoicePlayClickListener implements View.OnClickListener {
    EMMessage message;
    VoiceMessageBody voiceBody;
    ImageView voiceIconView;

    private AnimationDrawable voiceAnimation = null;
    MediaPlayer mediaPlayer = null;
    ImageView iv_read_status;
    Activity activity;
    LinearLayout speaker_ly;
    TextView speaker_text;
    private EMMessage.ChatType chatType;
    private BaseAdapter adapter;

    public static boolean isPlaying = false;
    public static EaseChatRowVoicePlayClickListener currentPlayListener = null;
    public static String playMsgId;

    public EaseChatRowVoicePlayClickListener(EMMessage message, ImageView v, ImageView iv_read_status, BaseAdapter adapter, Activity context, LinearLayout speaker_ly, TextView speaker_text) {
        this.message = message;
        voiceBody = (VoiceMessageBody) message.getBody();
        this.iv_read_status = iv_read_status;
        this.adapter = adapter;
        voiceIconView = v;
        this.activity = context;
        this.chatType = message.getChatType();
        this.speaker_ly = speaker_ly;
        this.speaker_text = speaker_text;
    }

    public void stopPlayVoice() {
        if (voiceAnimation != null) voiceAnimation.stop();
        if (speaker_ly != null) speaker_ly.setVisibility(View.GONE);
        if (message.direct == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
        } else {
            voiceIconView.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
        //adapter.notifyDataSetChanged();
    }

    public void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        if (speaker_ly != null) speaker_ly.setVisibility(View.VISIBLE);
        playMsgId = message.getMsgId();
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        //是否打开了扬声器
        if (SpeakerUtil.getInstance(activity).isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            if (speaker_text != null) speaker_text.setText("话筒");
            speaker_ly.setOnClickListener(new OnCloseSpeaker());
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            if (speaker_text != null) speaker_text.setText("扬声器");
            speaker_ly.setOnClickListener(new OnOpenSpeaker());
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();
            // 如果是接收的消息
            if (message.direct == EMMessage.Direct.RECEIVE) {
                if (!message.isAcked() && chatType == EMMessage.ChatType.Chat) {
                    // 告知对方已读这条消息
                    if (chatType != EMMessage.ChatType.GroupChat && chatType != EMMessage.ChatType.ChatRoom)
                        EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                }
                if (!message.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                    message.setListened(true);
                    EMChatManager.getInstance().setMessageListened(message);
                }
            }
        } catch (Exception e) {
            System.out.println();
        }
    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        if (message.direct == EMMessage.Direct.RECEIVE) {
            voiceIconView.setImageResource(R.drawable.voice_from_icon);
        } else {
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    /**
     * 打开扬声器
     */
    class OnOpenSpeaker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SpeakerUtil.getInstance(activity).openSpeaker();
            if (speaker_text != null) speaker_text.setText("话筒");
            speaker_ly.setOnClickListener(new OnCloseSpeaker());
        }
    }

    /**
     * 关闭扬声器
     */
    class OnCloseSpeaker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SpeakerUtil.getInstance(activity).closeSpeaker();
            if (speaker_text != null) speaker_text.setText("扬声器");
            speaker_ly.setOnClickListener(new OnOpenSpeaker());
        }
    }

    @Override
    public void onClick(View v) {
        String st = "正在下载语音，稍后点击";
        if (isPlaying) {
            if (playMsgId != null && playMsgId.equals(message.getMsgId())) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

        if (message.direct == EMMessage.Direct.SEND) {
            // 对于未经发送的语音，我们尝试用当地获取
            playVoice(voiceBody.getLocalUrl());//当地的url
        } else {
            if (message.status == EMMessage.Status.SUCCESS) {
                //如果本地有语音文件，则直接从本地获取
                File file = new File(voiceBody.getLocalUrl());
                if (file.exists() && file.isFile())
                    playVoice(voiceBody.getLocalUrl());
                else
                    LogUtil.e("文件不存在");

            } else if (message.status == EMMessage.Status.INPROGRESS) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
            } else if (message.status == EMMessage.Status.FAIL) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        //下载语音
                        EMChatManager.getInstance().asyncFetchMessage(message);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        //adapter.notifyDataSetChanged();
                    }
                }.execute();
            }
        }
    }
}