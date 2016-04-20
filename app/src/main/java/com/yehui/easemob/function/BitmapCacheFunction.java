package com.yehui.easemob.function;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Luhao
 * on 2016/4/5.
 * 关于聊天中本地图片的缓存
 */
public class BitmapCacheFunction {
    private LruCache<String, Bitmap> mMemoryCache;
    // 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
    // LruCache通过构造函数传入缓存值，以KB为单位。
    private int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // 使用最大可用内存值的1/8作为缓存的大小。
    private int cacheSize = maxMemory / 8;

    private BitmapCacheFunction() {
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    private static volatile BitmapCacheFunction instance;

    public static BitmapCacheFunction getInstance() {
        if (instance == null) {
            synchronized (BitmapCacheFunction.class) {
                if (instance == null) {
                    instance = new BitmapCacheFunction();
                }
            }
        }
        return instance;
    }

    /**
     * 向map中添加图片
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToLruCache(String key, Bitmap bitmap) {
        if (bitmap!=null&&getBitmapFromLruCache(key) == null) {
                mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 向map中替换图片
     *
     * @param key
     * @param bitmap
     */
    public void setBitmapToLruCache(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    /**
     * 根据key获得bitmap实例
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromLruCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 查看该key下是否有缓存
     *
     * @param key
     * @return
     */
    public boolean isBitmapByLruCache(String key) {
        return mMemoryCache.get(key) != null;
    }

    /**
     * 关闭内存缓存
     */
    public void closeLruCache() {
        mMemoryCache = null;
        instance = null;
    }


}
