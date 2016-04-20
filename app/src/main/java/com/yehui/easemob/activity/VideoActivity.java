package com.yehui.easemob.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yehui.easemob.R;
import com.yehui.easemob.bean.VideoEntityBean;
import com.yehui.easemob.utils.BitmapUtil;
import com.yehui.utils.activity.base.BaseGridActivity;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.LogUtil;
import com.yehui.utils.utils.files.FileContact;
import com.yehui.utils.utils.files.FileSizeUtil;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Luhao
 * on 2016/4/15.
 * 选择本地sd卡视频文件
 */
public class VideoActivity extends BaseGridActivity implements View.OnClickListener {
    private View handerView;
    private FrameLayout video_fl_handler;
    private List<VideoEntityBean> videoList;
    protected final static String KEY_SAVE_VIDEO_BEAN = "video_file_bean";
    private AsyncImageLoader asyncImageLoader;

    /**
     * ListView异步加载图片是非常实用的方法，
     * 凡是是要通过网络获取图片资源一般使用这种方法比较好，
     * 用户体验好，下面就说实现方法，先贴上主方法的代码：
     */
    //异步加载image
    public static class AsyncImageLoader {

        private HashMap<String, SoftReference<Bitmap>> imageCacheMap;
        private String imagePath;

        public AsyncImageLoader() {
            //软引用
            imageCacheMap = new HashMap<>();
        }

        //第二个参数callback为图片回收
        public Bitmap loadBitmap(final String imageUrl, final ImageCallback imageCallback) {

            //如果HashMap中有缓存的image路径，则从map中取出来，运用软引用
            if (imageCacheMap.containsKey(imageUrl)) {
                SoftReference<Bitmap> softReference = imageCacheMap.get(imageUrl);
                Bitmap bitmap = softReference.get();
                if (bitmap != null) {
                    return bitmap;
                }
            }

            final Handler handler = new Handler() {
                //定义的回收图片接口，实现其中的方法
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Bitmap) message.obj, imagePath);
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //将加载过的图片先存在hashmap中
                    Bitmap bitmap = loadImageFromUrl(imageUrl);
                    imagePath = FileContact.YEHUI_CACHE_IMG_PATH + DateUtil.getDatePattern() + "_video_cache" + ".png";
                    imageCacheMap.put(imageUrl, new SoftReference<>(bitmap));
                    Message message = new Message();
                    message.what = 1;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                    BitmapUtil.saveBitmapByFile(bitmap, imagePath);
                }
            }).start();
            return null;
        }

        //将图片用流读入到drawable中
        public Bitmap loadImageFromUrl(String url) {
            return ThumbnailUtils.createVideoThumbnail(url, 3);
        }

        //图片回收接口
        public interface ImageCallback {
            void imageLoaded(Bitmap imageBitmap, String imageUrl);
        }
    }

    @Override
    protected int gridViewByNumber() {
        return 3;
    }

    @Override
    protected float[] decorationSize() {
        return new float[]{2, 2, 2, 2};
    }

    @Override
    protected int getItemLayoutById(int resId) {
        return R.layout.item_video_gridview;
    }

    @Override
    protected void initView() {
        super.initView();
        handerView = inflate(R.layout.item_video_gridview_hander, null);
        mAdapter.setHeaderView(handerView);
        video_fl_handler = (FrameLayout) handerView.findViewById(R.id.video_fl_handler);
        video_fl_handler.setOnClickListener(this);
        setIsLoadMore(false);//不加载更多
        setIsRefresh(true);//下拉刷新
    }

    @Override
    protected void refresh() {
        clearAll();
        getVideoFile();
        refreshSuccess();
        notifyDataChange();
    }

    @Override
    protected void initItemData(BaseViewHolder holder, final int position) {
        final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        final VideoEntityBean videoEntityBean = (VideoEntityBean) data.get(position);

        videoViewHolder.video_size_text.setText(FileSizeUtil.getFileSize(videoEntityBean.getSize()));
        videoViewHolder.video_time_text.setText(getDateSize(videoEntityBean.getDuration()));

        //异步加载主要方法
        Bitmap cachedImage = asyncImageLoader.loadBitmap(videoEntityBean.getFilePath(), new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Bitmap imageBitmap, String imageUrl) {
                /**
                 * 如果imageview中加入了url后不为null则将这个url缓存的图片取出来，直接放在最外层的listview的image视图中
                 * 判断，listview中是否有
                 */
                videoEntityBean.setImgPath(imageUrl);
                data.set(position, videoEntityBean);
                videoViewHolder.video_img.setImageBitmap(imageBitmap);
            }
        });

        //如果drawable里面为null的话则在imageview中添加默认图片
        //如果有则说明该图片在hashmap中已经存在过了
        if (cachedImage != null)
            videoViewHolder.video_img.setImageBitmap(cachedImage);
    }

    private String getDateSize(long size) {
        size = size / 1000;
        String time = size + " s";
        if (size > 60) {
            long endSize = size / 60;
            long sSize = size - (endSize * 60);
            time = endSize + " : " + (sSize > 10 ? sSize : "0" + sSize);
        }
        if (size > 60 * 60) {
            long hSize = size / 60 / 60;
            long minSize = (size - (hSize * 60 * 60)) / 60;
            long sSize = size - (hSize * 60 * 60 + minSize * 60);
            time = hSize + " : " + (minSize > 0 ? (minSize > 10 ? minSize : "0" + minSize) : "00") + " : " + (sSize > 10 ? sSize : "0" + sSize);
        }
        return time;
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new VideoViewHolder(itemView);
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {

        VideoEntityBean videoEntityBean = (VideoEntityBean) data.get(position);
        if (videoEntityBean.getSize() > 1024 * 1024 * 10) {
            showShortToast("文件大小超过10M");
            return;
        }
        //点击之后的回调
        Intent intent = new Intent();
        intent.putExtra(KEY_SAVE_VIDEO_BEAN, videoEntityBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_video);
    }

    @Override
    protected String setTitleText() {
        return "选择视频";
    }

    @Override
    protected void initData() {
        getVideoFile();
        asyncImageLoader = new AsyncImageLoader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_fl_handler://点击后调用系统相机录像
                startActivityForResult(RecorderVideoActivity.class, RecorderVideoActivity.RECORDER_BACK);
                break;
        }
    }

    private Handler handler = new Handler() {
        //定义的回收图片接口，实现其中的方法
        public void handleMessage(Message message) {
            if (message.what == 1) {
                VideoEntityBean videoEntityBean= (VideoEntityBean) message.obj;
                //点击之后的回调
                Intent intent = new Intent();
                intent.putExtra(KEY_SAVE_VIDEO_BEAN, videoEntityBean);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RecorderVideoActivity.RECORDER_BACK) {
                if (data == null) {
                    return;
                }
                Uri uri = data.getParcelableExtra("uri");
                String[] projects = new String[]{MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.DURATION};
                Cursor cursor = this.getContentResolver().query(
                        uri, projects, null,
                        null, null);
                final VideoEntityBean videoEntityBean = new VideoEntityBean();
                if (cursor.moveToFirst()) {

//                    int size = (int) cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
//                    if (size > 1024 * 1024 * 10) {
//                        showShortToast("文件大小超过10M");
//                        return;
//                    }
//
//                    videoEntityBean.setSize(size);
                    // 路径：MediaStore.Audio.Media.DATA
                    final String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    final int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String imagePath = FileContact.YEHUI_CACHE_IMG_PATH + DateUtil.getNow(DateUtil.FORMAT_FULL_NO) + "_video_cache" + ".png";
                            BitmapUtil.saveBitmapByFile(ThumbnailUtils.createVideoThumbnail(filePath, 3), imagePath);
                            videoEntityBean.setImgPath(imagePath);
                            videoEntityBean.setFilePath(filePath);
                            // 总播放时长：MediaStore.Audio.Media.DURATION
                            videoEntityBean.setDuration(duration);
                            //videoEntityBean.setID((int) com.yehui.easemob.utils.DateUtil.timeToStamp(DateUtil.getDatePattern()));
                            // 名称：MediaStore.Audio.Media.TITLE
                            //String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                            //videoEntityBean.setTitle(title);
                            // 大小：MediaStore.Audio.Media.SIZE
                            Message message = new Message();
                            message.what = 1;
                            message.obj = videoEntityBean;
                            handler.sendMessage(message);
                        }
                    }).start();
                    LogUtil.d("duration:" + duration);
                }
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }

            }
        }
    }

    class VideoViewHolder extends BaseViewHolder {
        private FrameLayout video_fl;
        private ImageView video_img;
        private TextView video_size_text, video_time_text;

        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            video_fl = (FrameLayout) itemView.findViewById(R.id.video_fl);
            video_img = (ImageView) itemView.findViewById(R.id.video_img);
            video_size_text = (TextView) itemView.findViewById(R.id.video_size_text);
            video_time_text = (TextView) itemView.findViewById(R.id.video_time_text);
        }
    }

    private void getVideoFile() {
        videoList = new ArrayList<>();
        ContentResolver mContentResolver = this.getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // ID:MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                // 名称：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                // 路径：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                // 总播放时长：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                // 大小：MediaStore.Audio.Media.SIZE
                int size = (int) cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                VideoEntityBean entty = new VideoEntityBean();
                entty.setID(id);
                entty.setTitle(title);
                entty.setFilePath(url);
                entty.setDuration(duration);
                entty.setSize(size);
                videoList.add(entty);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        addAll(videoList);
    }
}
