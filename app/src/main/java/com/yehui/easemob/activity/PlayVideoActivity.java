package com.yehui.easemob.activity;

import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;

/**
 * Created by Luhao
 * on 2016/4/20.
 */
public class PlayVideoActivity extends EasemobActivity {
    public static final String VIDEO_URL = "play_video_url";
    private String url;
    VideoView videoView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_play_video);
    }

    @Override
    protected String setTitleText() {
        return "播放视频";
    }

    @Override
    protected void initView() {
        videoView = (VideoView) findViewById(R.id.videoview);
    }

    @Override
    protected void initData() {
        super.initData();
        url = getString(VIDEO_URL, "");
        videoView.setVideoURI(Uri.parse(url));
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();
    }
}
