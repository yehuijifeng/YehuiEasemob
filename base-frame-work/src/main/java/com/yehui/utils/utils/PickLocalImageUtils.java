package com.yehui.utils.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.yehui.utils.activity.ImageCroppingActivity;
import com.yehui.utils.utils.files.FileContact;

import java.io.File;

/**
 * Created by yehuijifeng
 * on 2015/9/17 12:42.
 * 从当地获取照片资源工具类，相册，拍照
 */
public class PickLocalImageUtils {

    public static final int CODE_FOR_ALBUM = 2000;

    public static final int CODE_FOR_CAMERA = CODE_FOR_ALBUM + 1;
    public static final int CODE_FOR_CROP = CODE_FOR_CAMERA + 1;
    public static final int CODE_FOR_VIDEO = CODE_FOR_CROP + 1;

    /**
     * 去相册
     *
     * @param activity
     */
    public static void toAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CODE_FOR_ALBUM);
    }

    /**
     * 去相机
     *
     * @param activity
     * @param imageFileName
     */
    public static void toCamera(Activity activity, String imageFileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                FileContact.YEHUI_SAVE_IMG_PATH, imageFileName)));
        activity.startActivityForResult(intent, CODE_FOR_CAMERA);
    }

    /**
     * 去视频资源
     *
     * @param activity
     */
    public static void toVideo(Activity activity,Class cla) {
        Intent intent = new Intent(activity, cla);
        activity.startActivityForResult(intent, CODE_FOR_VIDEO);
    }

    /**
     * 去本地sd卡找资源
     *
     * @param activity
     */
    public static void toFile(Activity activity) {
        Toast.makeText(activity, "文件资源，暂未开放", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(activity, FileListActivity.class);
        //activity.startActivityForResult(intent, CODE_FOR_VIDEO);
    }

    /**
     * 获得照片存放路径
     *
     * @param uri
     * @param resolver
     * @return
     */
    public static String getPath(Uri uri, ContentResolver resolver) {
        String path;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = resolver.query(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } else {
            path = uri.getPath();
        }
        return path;
    }

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