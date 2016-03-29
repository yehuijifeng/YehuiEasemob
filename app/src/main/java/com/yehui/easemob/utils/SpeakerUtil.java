package com.yehui.easemob.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Luhao
 * on 2016/3/23.
 * 扬声器工具类
 */
public class SpeakerUtil {
    private AudioManager audioManager;
    private static volatile SpeakerUtil instance;

    private SpeakerUtil(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static synchronized SpeakerUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SpeakerUtil.class) {
                if (instance == null) {
                    instance = new SpeakerUtil(context);
                }
            }
        }
        return instance;
    }

    /**
     * 判断扬声器是否在打开
     *
     * @return
     */
    public boolean isSpeakerOpened() {
        audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        return audioManager.isSpeakerphoneOn();
    }

    /**
     * 获取当前通话音量
     *
     * @return
     */
    public int getStreamVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    /**
     * 打开扬声器
     */
    public void openSpeaker() {
        if (!audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }
    }

    /**
     * 关闭扬声器
     */
    public void closeSpeaker() {
        if (audioManager != null) {
            if (audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, getStreamVolume(),
                        AudioManager.STREAM_VOICE_CALL);
            }
        }
    }
}
