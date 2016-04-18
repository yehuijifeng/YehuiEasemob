package com.yehui.easemob.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.yehui.utils.utils.files.FileContact;

import java.util.ArrayList;
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
    protected final static String KEY_SAVE_VIDEO_PATH = "video_file_path";
    protected final static String KEY_SAVE_VIDEO_IMAGE = "video_image_path";
    private String imagePath, filePath;

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
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        VideoEntityBean videoEntityBean = (VideoEntityBean) data.get(position);
        videoViewHolder.video_size_text.setText(videoEntityBean.getSize());
        videoViewHolder.video_time_text.setText(videoEntityBean.getDuration());
        Bitmap bitmap = BitmapUtil.getVideoThumb(videoEntityBean.getFilePath());
        imagePath = FileContact.YEHUI_CACHE_IMG_PATH + DateUtil.getTimeString() + "_" + videoEntityBean.getTitle() + ".png";
        filePath = videoEntityBean.getFilePath();
        videoViewHolder.video_img.setImageBitmap(bitmap);
        BitmapUtil.saveBitmapByFile(bitmap, imagePath);
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new VideoViewHolder(itemView);
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {
        //点击之后的回调
        Intent intent = new Intent();
        intent.putExtra(KEY_SAVE_VIDEO_PATH, filePath);
        intent.putExtra(KEY_SAVE_VIDEO_IMAGE, imagePath);
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
        videoList = new ArrayList<>();
        getVideoFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_fl_handler://点击后调用系统相机录像
                showLongToast("调用系统相机录像");
                break;

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
