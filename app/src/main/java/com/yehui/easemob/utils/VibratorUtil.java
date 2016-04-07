package com.yehui.easemob.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;

/**
 * Created by Luhao
 * on 2016/4/7.
 * 手机震动工具类
 */
public class VibratorUtil {
    private static int modeType = 1;

    /**
     * final Activity activity  ：调用该方法的Activity实例
     * long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void vibrate(Context context, long milliseconds) {
        initVibrate(context, milliseconds);
//        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
//        vib.vibrate(milliseconds);
    }

    public static void vibrate(Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    private static void initVibrate(Context context, long milliseconds) {

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final int ringerMode = am.getRingerMode();
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
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
}
