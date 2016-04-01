package com.yehui.easemob.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.UserInfoContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.fragment.SquareFragment;
import com.yehui.easemob.utils.BitmapUtil;
import com.yehui.utils.activity.ImageCroppingActivity;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.PickLocalImageUtils;
import com.yehui.utils.utils.files.FileContact;
import com.yehui.utils.view.CircularImageView;
import com.yehui.utils.view.dialog.ListDialog;
import com.yehui.utils.view.dialog.PromptDialog;

/**
 * Created by Luhao on 2016/3/7.
 * 个人中心
 */
public class UserCenterActivity extends EasemobActivity implements View.OnClickListener, TextWatcher {
    private TextView user_center_name_square;//用户名
    private EditText
            user_center_nickname,//昵称
            user_center_introduce,//个人说明
            user_center_email;//邮箱
    private CircularImageView user_center_image_square;
    private ListDialog listDialog;
    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;
    private String imageFileName;
    private boolean isChange;//文本是否改变

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_center);
    }

    @Override
    protected String setTitleText() {
        return "个人信息";
    }

    @Override
    protected void initView() {
        user_center_image_square = (CircularImageView) findViewById(R.id.user_center_image_square);
        user_center_name_square = (TextView) findViewById(R.id.user_center_name_square);
        user_center_nickname = (EditText) findViewById(R.id.user_center_nickname);
        user_center_introduce = (EditText) findViewById(R.id.user_center_introduce);
        user_center_email = (EditText) findViewById(R.id.user_center_email);
        user_center_image_square.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();
        isChange = false;
        listDialog = new ListDialog(this);
        userInfoDao = new UserInfoDao(this);
        mTitleView.setToBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBackDialog();
            }
        });
        if (userInfoDao.queryByWhere(UserInfoContant.userName, EasemobAppliaction.user.getUserName()) == null)
            return;
        userInfoBean = (UserInfoBean) userInfoDao.queryByWhere(UserInfoContant.userName, EasemobAppliaction.user.getUserName()).get(0);
        user_center_name_square.setText(userInfoBean.getUserName());
        user_center_nickname.setText(userInfoBean.getUserNickname());
        user_center_introduce.setText(userInfoBean.getUserIntroduce());
        user_center_email.setText(userInfoBean.getPwdEmail());
        imageLoader.displayImage(userInfoBean.getUserIconPath(), user_center_image_square, EasemobAppliaction.defaultOptions);

        user_center_nickname.addTextChangedListener(this);
        user_center_introduce.addTextChangedListener(this);
        user_center_email.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_center_image_square:
                listDialog.showListDialog(new String[]{"相机", "相册"}, new ListDialog.ListOnClickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onItems(int item, String itemName) {
                        if (item == 1) {//去相册
                            PickLocalImageUtils.toAlbum(UserCenterActivity.this);
                        } else {//去相机
                            imageFileName = DateUtil.format(System.currentTimeMillis(), "'IMG'_yyyyMMddHHmmss") + ".jpg";
                            PickLocalImageUtils.toCamera(UserCenterActivity.this, imageFileName);
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imagePath = "";
        switch (requestCode) {
            case PickLocalImageUtils.CODE_FOR_ALBUM://来自系统相册
                if (data == null) return;
                Uri uri = data.getData();
                imagePath = PickLocalImageUtils.getPath(uri, getContentResolver());
                break;
            case PickLocalImageUtils.CODE_FOR_CAMERA://来自于系统相机的回调
                imagePath = FileContact.YEHUI_SAVE_IMG_PATH + imageFileName;
                PickLocalImageUtils.toCrop(this, imagePath);
                break;
            case PickLocalImageUtils.CODE_FOR_CROP://来自于剪切照片的回调
                if (data == null) return;
                imagePath = data.getStringExtra(ImageCroppingActivity.KEY_SAVE_IMAGE_PATH);
                Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(imagePath, 100, 100);
                BitmapUtil.saveBitmap(bitmap, imagePath, 100);
                break;
        }
        showImage(imagePath);
    }

    private void showImage(String imagePath) {
        imageLoader.displayImage(imagePath, user_center_image_square, EasemobAppliaction.defaultOptions);
        if (userInfoBean != null) {
            userInfoBean.setUserIconPath(imagePath);
            userInfoDao.updateData(userInfoBean);
            isChange = true;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        isChange = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isChange) {
            getBackDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存信息，返回提示
     */
    private void getBackDialog() {
        if (!isChange) {
            finish();
            return;
        }
        promptDialog.showPromptDialog("您修改了个人信息，确认保存吗?", new PromptDialog.PromptOnClickListener() {
            @Override
            public void onDetermine() {
                if (userInfoBean != null) {
                    userInfoBean.setUserNickname(user_center_nickname.getText().toString().trim());
                    userInfoBean.setUserIntroduce(user_center_introduce.getText().toString().trim());
                    userInfoBean.setPwdEmail(user_center_email.getText().toString().trim());
                    userInfoDao.updateData(userInfoBean);
                    showShortToast("保存成功！");
                    Intent intent = new Intent();
                    intent.putExtra(SquareFragment.USER_INFO, userInfoBean);
                    setResult(SquareFragment.ICON_BACK, intent);
                    finish();
                }
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
    }
}
