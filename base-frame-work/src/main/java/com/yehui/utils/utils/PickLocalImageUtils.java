package com.yehui.utils.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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



    /**去相册
     * @param activity
     */
    public static void toAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CODE_FOR_ALBUM);
    }

    /**去相机
     * @param activity
     * @param imageFileName
     */
    public static void toCamera(Activity activity, String imageFileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                FileContact.YEHUI_SAVE_IMG_PATH, imageFileName)));
        activity.startActivityForResult(intent, CODE_FOR_CAMERA);
    }




    /**获得照片存放路径
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
}