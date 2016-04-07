package com.yehui.easemob.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import com.yehui.easemob.R;

/**
 * Created by Luhao
 * on 2016/4/7.
 * 播放短暂提示音
 */
public class MusicUtil {
    private SoundPool soundPool;
    private final int musicId = 1;
    private int modeType = 1;
    private AudioManager mAudioManager;
    private Handler handler = new Handler();

    private MusicUtil() {
        //参数，1，同时播放数；2，播放音乐类型；3，音乐品质
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
    }

    private static volatile MusicUtil instance;

    public static MusicUtil getInstance() {
        if (instance == null) {
            synchronized (MusicUtil.class) {
                if (instance == null) {
                    instance = new MusicUtil();
                }
            }
        }
        return instance;
    }

    public void playMusic(Context contexts) {
        AudioManager am = (AudioManager) contexts.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = am.getRingerMode();
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                //normal正常
                modeType = 1;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                //vibrate震动
                modeType = 2;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                //silent静音
                modeType = 3;
                break;
        }
        if (modeType == 3) return;
        if (mAudioManager == null)
            mAudioManager = (AudioManager) contexts.getSystemService(Context.AUDIO_SERVICE);
        /**
         * 第一个参数为id，id即为放入到soundPool中的顺序，比如现在collide.wav是第一个，因此它的id就是1。
         * 第二个和第三个参数为左右声道的音量控制。
         * 第四个参数为优先级，由于只有这一个声音，因此优先级在这里并不重要。
         * 第五个参数为是否循环播放，0为不循环，-1为循环。
         * 最后一个参数为播放比率，从0.5到2，一般为1，表示正常播放。
         **/
        if (soundPool == null) return;
        //第一个参数为上下文参数，第二个参数为声音的id，
        soundPool.load(contexts, R.raw.shuidi, musicId);
        //获得音乐音量
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        soundPool.play(1, current, current, 0, 0, 1);
    }

    public void stopMusic() {
        if (soundPool == null) return;
        soundPool.stop(musicId);
    }


    private void musicSize() {
        //通话音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);

        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

        //系统音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);

        //铃声音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);

        //音乐音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //提示声音音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
    }
}
