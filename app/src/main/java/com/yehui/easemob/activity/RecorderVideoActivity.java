/************************************************************
 * * EaseMob CONFIDENTIAL
 * __________________
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * NOTICE: All information contained herein is, and remains
 * the property of EaseMob Technologies.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from EaseMob Technologies.
 */
package com.yehui.easemob.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.VideoView;

import com.easemob.util.EMLog;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.utils.VideoUtil;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.LogUtil;
import com.yehui.utils.utils.files.FileContact;
import com.yehui.utils.utils.files.FileOperationUtil;
import com.yehui.utils.view.dialog.LoadingDialog;
import com.yehui.utils.view.dialog.PromptDialog;
import com.yehui.utils.view.titleview.MyTitleView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RecorderVideoActivity extends EasemobActivity implements
        OnClickListener, SurfaceHolder.Callback, OnErrorListener,
        OnInfoListener {
    private final static String CLASS_LABEL = "RecordActivity";
    public final static int RECORDER_BACK = 1001;
    private PowerManager.WakeLock mWakeLock;
    private ImageView btnStart;// 开始录制按钮
    private ImageView btnStop;// 停止录制按钮
    private MediaRecorder mediaRecorder;// 录制视频的类
    private VideoView mVideoView;// 显示视频的控件
    String localPath = "";// 录制的视频路径
    private Camera mCamera;
    // 预览的宽高
    private int previewWidth = 480;
    private int previewHeight = 480;
    private Chronometer chronometer;
    private int frontCamera = 0;// 0是后置摄像头，1是前置摄像头
    private Button btn_switch;
    Parameters cameraParameters = null;
    private SurfaceHolder mSurfaceHolder;
    int defaultVideoFrameRate = -1;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_recorder);
    }

    @Override
    protected String setTitleText() {
        return "录制视频";
    }

    @Override
    protected void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 选择支持半透明模式，在有surfaceview的activity中使用
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                CLASS_LABEL);
        mWakeLock.acquire();
        mTitleView.setTitleMode(MyTitleView.TitleMode.NORMAL);
        initViews();
    }

    @Override
    protected void initData() {
        promptDialog = new PromptDialog(this);
        loadingDialog = new LoadingDialog(this);
    }


    private void initViews() {
        btn_switch = (Button) findViewById(R.id.switch_btn);
        btn_switch.setOnClickListener(this);
        btn_switch.setVisibility(View.VISIBLE);
        mVideoView = (VideoView) findViewById(R.id.mVideoView);
        btnStart = (ImageView) findViewById(R.id.recorder_start);
        btnStop = (ImageView) findViewById(R.id.recorder_stop);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        mSurfaceHolder = mVideoView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
    }

    public void back(View view) {
        releaseRecorder();
        releaseCamera();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWakeLock == null) {
            // 获取唤醒锁,保持屏幕常亮
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    CLASS_LABEL);
            mWakeLock.acquire();
        }
//		if (!initCamera()) {
//			showFailDialog();
//		}
    }

    @SuppressLint("NewApi")
    private boolean initCamera() {
        try {
            if (frontCamera == 0) {
                mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
            } else {
                mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
            }
            Parameters camParams = mCamera.getParameters();
            mCamera.lock();
            mSurfaceHolder = mVideoView.getHolder();
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mCamera.setDisplayOrientation(90);

        } catch (RuntimeException ex) {
            LogUtil.e("init Camera fail " + ex.getMessage());
            return false;
        }
        return true;
    }

    private void handleSurfaceChanged() {
        if (mCamera == null) {
            finish();
            return;
        }
        boolean hasSupportRate = false;
        List<Integer> supportedPreviewFrameRates = mCamera.getParameters()
                .getSupportedPreviewFrameRates();
        if (supportedPreviewFrameRates != null
                && supportedPreviewFrameRates.size() > 0) {
            Collections.sort(supportedPreviewFrameRates);
            for (int i = 0; i < supportedPreviewFrameRates.size(); i++) {
                int supportRate = supportedPreviewFrameRates.get(i);

                if (supportRate == 15) {
                    hasSupportRate = true;
                }

            }
            if (hasSupportRate) {
                defaultVideoFrameRate = 15;
            } else {
                defaultVideoFrameRate = supportedPreviewFrameRates.get(0);
            }

        }
        // 获取摄像头的所有支持的分辨率
        List<Size> resolutionList = VideoUtil.getResolutionList(mCamera);
        if (resolutionList != null && resolutionList.size() > 0) {
            Collections.sort(resolutionList, new VideoUtil.ResolutionComparator());
            Size previewSize = null;
            boolean hasSize = false;
            // 如果摄像头支持640*480，那么强制设为640*480
            for (int i = 0; i < resolutionList.size(); i++) {
                Size size = resolutionList.get(i);
                if (size != null && size.width == 640 && size.height == 480) {
                    previewSize = size;
                    previewWidth = previewSize.width;
                    previewHeight = previewSize.height;
                    hasSize = true;
                    break;
                }
            }
            // 如果不支持设为中间的那个
            if (!hasSize) {
                int mediumResolution = resolutionList.size() / 2;
                if (mediumResolution >= resolutionList.size())
                    mediumResolution = resolutionList.size() - 1;
                previewSize = resolutionList.get(mediumResolution);
                previewWidth = previewSize.width;
                previewHeight = previewSize.height;

            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_btn:
                switchCamera();
                break;
            case R.id.recorder_start:
                // start recording
                if (!startRecording())
                    return;
                showShortToast("录像开始");
                btn_switch.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
                btnStart.setEnabled(false);
                btnStop.setVisibility(View.VISIBLE);
                // 重置其他
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
                break;
            case R.id.recorder_stop:
                btnStop.setEnabled(false);
                // 停止拍摄
                stopRecording();
                btn_switch.setVisibility(View.VISIBLE);
                chronometer.stop();
                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                promptDialog.showPromptDialog("是否发送?", new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        sendVideo(null);
                    }

                    @Override
                    public void onCancel() {
                        if (localPath != null) {
                            File file = new File(localPath);
                            if (file.exists())
                                file.delete();
                        }
                        finish();
                    }
                });
            default:
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            if (!initCamera()) {
                showFailDialog();
                return;
            }

        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            handleSurfaceChanged();
        } catch (Exception e1) {
            LogUtil.e("start preview fail " + e1.getMessage());
            showFailDialog();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        LogUtil.v("surfaceDestroyed");
    }

    public boolean startRecording() {
        if (mediaRecorder == null) {
            if (!initRecorder())
                return false;
        }
        mediaRecorder.setOnInfoListener(this);
        mediaRecorder.setOnErrorListener(this);
        mediaRecorder.start();
        return true;
    }

    @SuppressLint("NewApi")
    private boolean initRecorder() {
        if (!FileOperationUtil.isSDCardEnable()) {
            showNoSDCardDialog();
            return false;
        }

        if (mCamera == null) {
            if (!initCamera()) {
                showFailDialog();
                return false;
            }
        }
        mVideoView.setVisibility(View.VISIBLE);
        // TODO init button
        mCamera.stopPreview();
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // 设置录制视频源为Camera（相机）
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (frontCamera == 1) {
            mediaRecorder.setOrientationHint(270);
        } else {
            mediaRecorder.setOrientationHint(90);
        }
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置录制的视频编码h263 h264
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mediaRecorder.setVideoSize(previewWidth, previewHeight);
        // 设置视频的比特率
        mediaRecorder.setVideoEncodingBitRate(384 * 1024);
        // // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        if (defaultVideoFrameRate != -1) {
            mediaRecorder.setVideoFrameRate(defaultVideoFrameRate);
        }
        // 设置视频文件输出的路径
        localPath = FileContact.YEHUI_CACHE_VIDEO_PATH + "/"
                + DateUtil.getNow(DateUtil.FORMAT_FULL_NO) + ".mp4";
        mediaRecorder.setOutputFile(localPath);
        mediaRecorder.setMaxDuration(30000);
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                LogUtil.e("stopRecording error:" + e.getMessage());
            }
        }
        releaseRecorder();

        if (mCamera != null) {
            mCamera.stopPreview();
            releaseCamera();
        }
    }

    private void releaseRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    protected void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("NewApi")
    public void switchCamera() {

        if (mCamera == null) {
            return;
        }
        if (Camera.getNumberOfCameras() >= 2) {
            btn_switch.setEnabled(false);
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            switch (frontCamera) {
                case 0:
                    mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
                    frontCamera = 1;
                    break;
                case 1:
                    mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
                    frontCamera = 0;
                    break;
            }
            try {
                mCamera.lock();
                mCamera.setDisplayOrientation(90);
                mCamera.setPreviewDisplay(mVideoView.getHolder());
                mCamera.startPreview();
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }
            btn_switch.setEnabled(true);

        }

    }

    MediaScannerConnection msc = null;

    public void sendVideo(View view) {
        if (TextUtils.isEmpty(localPath)) {
            EMLog.e("Recorder", "recorder fail please try again!");
            return;
        }
        if (msc == null)
            msc = new MediaScannerConnection(this,
                    new MediaScannerConnectionClient() {

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            LogUtil.i("scanner completed");
                            msc.disconnect();
                            setResult(RESULT_OK, getIntent().putExtra("uri", uri));
                            finish();
                        }

                        @Override
                        public void onMediaScannerConnected() {
                            msc.scanFile(localPath, "video/*");
                        }
                    });

        loadingDialog.showLoadingDialog("处理中……");
        msc.connect();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        EMLog.v("video", "onInfo");
        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
            EMLog.v("video", "max duration reached");
            stopRecording();
            btn_switch.setVisibility(View.VISIBLE);
            chronometer.stop();
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.INVISIBLE);
            chronometer.stop();
            if (localPath == null) {
                return;
            }
            promptDialog.showPromptDialog("是否发送?", new PromptDialog.PromptOnClickListener() {
                @Override
                public void onDetermine() {
                    sendVideo(null);
                }

                @Override
                public void onCancel() {
//                    if (localPath != null) {
//                        File file = new File(localPath);
//                        if (file.exists())
//                            file.delete();
//                    }
//                    finish();
                }
            });
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        LogUtil.e("recording onError:");
        stopRecording();
        showShortToast("Recording error has occurred. Stopping the recording");

    }

    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "a.jpg");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }

    }

    @Override
    public void onBackPressed() {
        back(null);
    }

    private void showFailDialog() {
        promptDialog.showPromptDialog("打开设备失败!", new PromptDialog.PromptOnClickListener() {
            @Override
            public void onDetermine() {
                finish();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }

    private void showNoSDCardDialog() {
        promptDialog.showPromptDialog("没有sd卡!", new PromptDialog.PromptOnClickListener() {
            @Override
            public void onDetermine() {
                finish();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }

}
