package com.yehui.easemob.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.yehui.easemob.activity.ImageCroppingActivity;

/**
 * Created by Luhao on 2016/3/7.
 */
public class CropUtil {

    public static final int CODE_FOR_CROP = 2002;

    /**
     * 去剪裁图片
     *
     * @param activity
     * @param imagePath
     * @param crop_width
     * @param crop_height
     * @param aspectRatioX
     * @param aspectRatioY
     * @param saveImagePath
     */
    public static void toCrop(Activity activity, String imagePath, int crop_width, int crop_height, int aspectRatioX, int aspectRatioY, String saveImagePath) {
        Intent intent = new Intent(activity, ImageCroppingActivity.class);
        intent.putExtra(ImageCroppingActivity.KEY_IMAGE_PATH, imagePath);
        intent.putExtra(ImageCroppingActivity.KEY_CROP_WIDTH, crop_width);
        intent.putExtra(ImageCroppingActivity.KEY_CROP_HEIGHT, crop_height);
        intent.putExtra(ImageCroppingActivity.ASPECT_RATIO_X, aspectRatioX);
        intent.putExtra(ImageCroppingActivity.ASPECT_RATIO_Y, aspectRatioY);
        if (!TextUtils.isEmpty(saveImagePath))
            intent.putExtra(ImageCroppingActivity.KEY_SAVE_IMAGE_PATH, saveImagePath);
        activity.startActivityForResult(intent, CODE_FOR_CROP);
    }

    /**
     * 去剪裁图片
     *
     * @param activity
     * @param imagePath
     */
    public static void toCrop(Activity activity, String imagePath) {
        toCrop(activity, imagePath, ImageCroppingActivity.DEFAULT_CROP_WIDTH, ImageCroppingActivity.DEFAULT_CROP_HEIGHT, ImageCroppingActivity.DEFAULT_ASPECT_RATIO_VALUES, ImageCroppingActivity.DEFAULT_ASPECT_RATIO_VALUES, null);
    }

}
