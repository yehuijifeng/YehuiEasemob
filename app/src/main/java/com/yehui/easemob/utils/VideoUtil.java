package com.yehui.easemob.utils;

import android.hardware.Camera;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Luhao
 * on 2016/4/20.
 * 调用摄像头录像的工具类
 */
public class VideoUtil {

    // 获取摄像头的所有支持的分辨率
    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        return previewSizes;
    }


    public static class ResolutionComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height)
                return lhs.height - rhs.height;
            else
                return lhs.width - rhs.width;
        }
    }
}
