package com.yehui.easemob.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.RegisteredActivity;
import com.yehui.easemob.activity.UserCenterActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.fragment.base.EasemobFragment;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.utils.AppUtil;
import com.yehui.utils.utils.files.FileContact;
import com.yehui.utils.utils.files.FileFoundUtil;
import com.yehui.utils.utils.files.FileOperationUtil;
import com.yehui.utils.view.dialog.CustomDialog;
import com.yehui.utils.view.dialog.LoadingDialog;
import com.yehui.utils.view.dialog.PromptDialog;
import com.yehui.utils.view.titleview.MyTitleView;

/**
 * Created by Luhao
 * on 2016/2/20.
 * 个人中心
 */
public class SquareFragment extends EasemobFragment implements View.OnClickListener {
    private MyTitleView titleView;
    private ImageView user_image_square;
    private TextView user_name_square, cache_size_text;
    private RelativeLayout
            user_exit_rl,
            user_close_cache_rl,
            add_friend_rl,
            ic_register_user_rl;
    private PromptDialog promptDialog;
    private CustomDialog customDialog;

    private static final int SUCCESS_MESSAGE = 1;
    private static final int FINAL_MESSAGE = SUCCESS_MESSAGE + 1;
    public static final int ICON_BACK = FINAL_MESSAGE + 1;
    private static final int GO_USER_CENTER = ICON_BACK + 1;
    public static final String USER_INFO = "com.easemob.user";
    private LoadingDialog loadingDialog;

    @Override
    protected void initView(View parentView) {
        titleView = (MyTitleView) parentView.findViewById(R.id.my_title_view);
        titleView.setTitleText("个人中心");
        titleView.setTitleMode(MyTitleView.TitleMode.NO_BACK_NORMAL);
        user_image_square = (ImageView) parentView.findViewById(R.id.user_image_square);
        user_name_square = (TextView) parentView.findViewById(R.id.user_name_square);
        user_exit_rl = (RelativeLayout) parentView.findViewById(R.id.user_exit_rl);
        user_close_cache_rl = (RelativeLayout) parentView.findViewById(R.id.user_close_cache_rl);
        ic_register_user_rl = (RelativeLayout) parentView.findViewById(R.id.ic_register_user_rl);
        add_friend_rl = (RelativeLayout) parentView.findViewById(R.id.add_friend_rl);
        cache_size_text = (TextView) parentView.findViewById(R.id.cache_size_text);
        user_image_square.setOnClickListener(this);
        user_exit_rl.setOnClickListener(this);
        user_close_cache_rl.setOnClickListener(this);
        ic_register_user_rl.setOnClickListener(this);
        add_friend_rl.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if (EasemobAppliaction.user != null) {
            user_name_square.setText(TextUtils.isEmpty(EasemobAppliaction.user.getUserNickname()) ? EasemobAppliaction.user.getUserName() : EasemobAppliaction.user.getUserNickname());
            imageLoader.displayImage( EasemobAppliaction.user.getUserIconPath(), user_image_square, EasemobAppliaction.defaultOptions);
        }
        promptDialog = new PromptDialog(parentActivity);//提示框
        loadingDialog = new LoadingDialog(parentActivity);//加载框
        customDialog = new CustomDialog(parentActivity);//自定义视图框
        cache_size_text.setText(FileOperationUtil.getSDCacheSize());//显示缓存大小
    }


    @Override
    protected void getFriendStatus(FriendBean friendBean) {
        super.getFriendStatus(friendBean);
        switch (friendBean.getStatusCode()) {
            case EasemobContant.addContact://添加好友
                loadingDialog.dismissLoadingDialog();
                showLongToast(friendBean.getStatusMsg());
                break;
        }
    }

    @Override
    protected int setFragmentLayoutId() {
        return R.layout.fragment_square;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GO_USER_CENTER) {
            if (resultCode == ICON_BACK) {
                if (data == null) return;
                UserInfoBean userInfoBean = data.getParcelableExtra(USER_INFO);
                imageLoader.displayImage(userInfoBean.getUserIconPath(), user_image_square, EasemobAppliaction.defaultOptions);
                user_name_square.setText(TextUtils.isEmpty(userInfoBean.getUserNickname()) ? userInfoBean.getUserName() : userInfoBean.getUserNickname());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image_square://点击头像跳转个人信息详情
                startActivityForResult(UserCenterActivity.class, GO_USER_CENTER);
                break;
            case R.id.user_exit_rl://退出登陆
                promptDialog.showPromptDialog("确定要退出当前账号?", new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        ServerStatusHelper.getInstance().outlogin(parentActivity);
                    }

                    @Override
                    public void onCancel() {
                    }
                });

                break;
            case R.id.user_close_cache_rl://清楚缓存
                promptDialog.showPromptDialog("确定要清除 \"" + AppUtil.getAppName(parentActivity) + "\" 的缓存?", new PromptDialog.PromptOnClickListener() {
                    @Override
                    public void onDetermine() {
                        FileFoundUtil.deleteFileByPath(parentActivity, FileContact.YEHUI_CHACHE);
                        cache_size_text.setText(FileOperationUtil.getSDCacheSize());//显示缓存大小
                        showShortToast("清理完成");
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
            case R.id.ic_register_user_rl://注册
                startActivity(RegisteredActivity.class);
                break;
            case R.id.add_friend_rl://添加好友
                View customView = inflate(R.layout.dialog_add_friend, null);
                final EditText friend_name_edit = (EditText) customView.findViewById(R.id.friend_name_edit);
                final EditText friend_cause_edit = (EditText) customView.findViewById(R.id.friend_cause_edit);
                customDialog.showCustomDialog(customView, new CustomDialog.CustomOnClickListener() {
                    @Override
                    public void onDetermine() {
                        if (TextUtils.isEmpty(friend_name_edit.getText())) {
                            showShortToast("请填写添加的好友名称");
                            return;
                        }
                        if (TextUtils.isEmpty(friend_cause_edit.getText())) {
                            showShortToast("请填写添加好友的原因");
                            return;
                        }
                        loadingDialog.showLoadingDialog("正在发送好友请求……");
                        FriendStatushelper.getInstance().addFriends(friend_name_edit.getText().toString().trim(), friend_cause_edit.getText().toString().trim());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;
        }
    }
}
