package com.yehui.utils.utils.files;

import android.os.Environment;

import java.io.File;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 */
public class FileContact {
    /**
     * 防止被实例化
     */
    private FileContact() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * sd卡根目录路径
     */
    public static File getSDFile() {
        return Environment.getExternalStorageDirectory();
    }


    /**
     * sd卡根目录
     */
    public static String getSDPath() {
        return getSDFile().getPath();
    }

    /**
     * app根目录
     */
    public final static String YEHUI_PATH = getSDPath() + "/YehuiEasemob/";

    /**
     * app缓存目录
     */
    public final static String YEHUI_CHACHE = YEHUI_PATH + "Cache/";


    /**
     * app图片缓存路径
     */
    public final static String YEHUI_CACHE_IMG_PATH = YEHUI_CHACHE + "CacheImage/";

    /**
     * app图片缓存路径
     */
    public final static String YEHUI_CACHE_VIDEO_PATH = YEHUI_CHACHE + "CacheImage/";

    /**
     * app录音缓存路径
     */
    public final static String YEHUI_CACHE_VOICE_PATH = YEHUI_CHACHE + "CacheVoice/";

    /**
     * app图片保存路径
     */
    public final static String YEHUI_SAVE_IMG_PATH = YEHUI_PATH + "SaveImage/";


    /**
     * app日志存放路径
     */
    public final static String YEHUI_LOG_PATH = YEHUI_PATH + "Log/";

    /**
     * app配置文件存放路径
     */
    public final static String YEHUI_SETTINGS_PATH = YEHUI_PATH + "Settings/";

    /**
     * app文件存放路径
     */
    public final static String YEHUI_FILES_PATH = YEHUI_PATH + "Files/";

    /**
     * 创建sd卡下存放照片的文件夹
     */
    public static void createSaveImage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path1 = new File(FileContact.YEHUI_SAVE_IMG_PATH);
            if (!path1.exists())
                path1.mkdirs();
        }
    }

    /**
     * 创建sd卡下存放缓存图片的文件夹
     */
    public static void createCacheImage() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path1 = new File(FileContact.YEHUI_CACHE_IMG_PATH);
            if (!path1.exists())
                path1.mkdirs();
        }
    }

    /**
     * 创建sd卡下存放缓存语音的文件夹
     */
    public static void createCacheVoice() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path = new File(FileContact.YEHUI_CACHE_VOICE_PATH);
            if (!path.exists())
                path.mkdirs();
        }
    }

    /**
     * 创建sd卡下存放缓存视频的文件夹
     */
    public static void createCacheVideo() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path = new File(FileContact.YEHUI_CACHE_VIDEO_PATH);
            if (!path.exists())
                path.mkdirs();
        }
    }


    /**
     * 创建sd卡下存放日志的文件夹
     */
    public static void createLog() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path1 = new File(FileContact.YEHUI_LOG_PATH);
            if (!path1.exists())
                path1.mkdirs();
        }
    }

    /**
     * 创建sd卡下存放配置文件的文件夹
     */
    public static void createSettigns() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            String path = FileContact.YEHUI_SETTINGS_PATH;
            File path1 = new File(FileContact.YEHUI_SETTINGS_PATH);
            if (!path1.exists())
                path1.mkdirs();
        }
    }

    /**
     * 创建sd卡下存放配置文件的文件夹
     */
    public static void createFiles() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File path1 = new File(FileContact.YEHUI_FILES_PATH);
            if (!path1.exists())
                path1.mkdirs();
        }
    }

}
