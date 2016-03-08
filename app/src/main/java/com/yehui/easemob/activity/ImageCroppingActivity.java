package com.yehui.easemob.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.edmodo.cropper.CropImageView;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.yehui.easemob.R;
import com.yehui.utils.activity.base.BaseActivity;
import com.yehui.utils.application.YehuiApplication;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.files.FileContact;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 剪切图片
 */
public class ImageCroppingActivity extends BaseActivity implements OnClickListener {

    private CropImageView mCropImageView;

    private Button btn_crop_rotate;

    private Button btn_crop_confirm;

    // Static final constants
    public static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    public static final int DEFAULT_ROTATE_NINETY_DEGREES = 90;
    public static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    public static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    public static final String KEY_ROTATE_NINETY_DEGREES = "KEY_ROTATE_NINETY_DEGREES";
    public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static final String KEY_SAVE_IMAGE_PATH = "KEY_SAVE_IMAGE_PATH";
    public static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    private int mRotateNinetyDegrees = DEFAULT_ROTATE_NINETY_DEGREES;

    private String imagePath;

    private String saveImagePath;

    private Bitmap croppedImage;

    public static final String KEY_CROP_WIDTH = "key_crop_width";
    public static final String KEY_CROP_HEIGHT = "key_crop_height";

    public static final int DEFAULT_CROP_WIDTH = 720;
    public static final int DEFAULT_CROP_HEIGHT = 1280;

    private int crop_width;
    private int crop_height;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_image_crop);
    }

    @Override
    protected String setTitleText() {
        return "图片裁剪";
    }

    @Override
    protected void initView() {
        createDir(FileContact.YEHUI_SAVE_IMG_PATH);
        mAspectRatioX = getInt(ASPECT_RATIO_X,
                DEFAULT_ASPECT_RATIO_VALUES);
        mAspectRatioY = getInt(ASPECT_RATIO_Y,
                DEFAULT_ASPECT_RATIO_VALUES);
        mRotateNinetyDegrees = getInt(KEY_ROTATE_NINETY_DEGREES,
                DEFAULT_ROTATE_NINETY_DEGREES);
        crop_width = getInt(KEY_CROP_WIDTH, DEFAULT_CROP_WIDTH);
        crop_height = getInt(KEY_CROP_HEIGHT, DEFAULT_CROP_HEIGHT);
        imagePath = getString(KEY_IMAGE_PATH, "");
        saveImagePath = getString(KEY_SAVE_IMAGE_PATH,
                FileContact.YEHUI_SAVE_IMG_PATH
                        + DateUtil.getNow("'CROP'_yyyyMMddHHmmss") + ".jpg");
        mCropImageView = (CropImageView) findViewById(R.id.mCropImageView);
        btn_crop_rotate = (Button) findViewById(R.id.btn_crop_rotate);
        btn_crop_confirm = (Button) findViewById(R.id.btn_crop_confirm);
    }

    @Override
    protected void initData() {
        mCropImageView.setFixedAspectRatio(true);
        btn_crop_rotate.setOnClickListener(this);
        btn_crop_confirm.setOnClickListener(this);
        // croppedImage = BitmapUtil.decodeSampledBitmapFromFile(imagePath,
        // crop_width, crop_height);
        ImageSize targetImageSize = new ImageSize(crop_width, crop_height);
        if (!imagePath.contains("file:///")) {
            imagePath = "file:///" + imagePath;
        }
        croppedImage = imageLoader.loadImageSync(imagePath,
                targetImageSize, YehuiApplication.defaultOptions);
        mCropImageView.setImageBitmap(croppedImage);
        mCropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);
        mCropImageView.setGuidelines(ON_TOUCH);
    }

    private void createDir(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * @see OnClickListener#onClick(View)
     */
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_crop_rotate:
                    mCropImageView.rotateImage(mRotateNinetyDegrees);
                    break;
                case R.id.btn_crop_confirm:
                    croppedImage = mCropImageView.getCroppedImage();
                    saveBitmap(croppedImage);
                    Intent data = new Intent();
                    data.putExtra(KEY_SAVE_IMAGE_PATH, saveImagePath);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            showShortToast("该图片已损坏，请选择其他图片。");
        }
    }

    private void saveBitmap(Bitmap bitmap) {
        File f = new File(saveImagePath);
        File file = new File(FileContact.YEHUI_SAVE_IMG_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把Bitmap对象解析成流
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (croppedImage != null) {
            croppedImage.recycle();
            croppedImage = null;
        }
    }
}