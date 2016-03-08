package com.yehui.easemob.fragment;

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
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.fragment.base.EasemobFragment;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.ServerStatusHelper;
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
    private TextView user_name_square;
    private RelativeLayout
            user_exit_rl,
            user_close_cache_rl,
            add_friend_rl,
            ic_register_user_rl;
    private PromptDialog promptDialog;
    private CustomDialog customDialog;

    private int SUCCESS_MESSAGE = 1;
    private int FINAL_MESSAGE = SUCCESS_MESSAGE++;
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

        user_image_square.setOnClickListener(this);
        user_exit_rl.setOnClickListener(this);
        user_close_cache_rl.setOnClickListener(this);
        ic_register_user_rl.setOnClickListener(this);
        add_friend_rl.setOnClickListener(this);

        imageLoader.displayImage("file:///"+EasemobAppliaction.user.getUserIconPath(),user_image_square,EasemobAppliaction.defaultOptions);

    }

    @Override
    protected void initData() {
        if (EasemobAppliaction.user != null) {
            user_name_square.setText(EasemobAppliaction.user.getUserName());
        }
        promptDialog = new PromptDialog(parentActivity);//提示框
        loadingDialog = new LoadingDialog(parentActivity);//加载框
        customDialog = new CustomDialog(parentActivity);//自定义视图框
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image_square://点击头像跳转个人信息详情
                startActivity(UserCenterActivity.class);
                break;
            case R.id.user_exit_rl://推出登陆
                promptDialog.showPromptDialog("提示", "确定要退出当前账号?", new PromptDialog.PromptOnClickListener() {
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
                showShortToast("清除缓存");
                break;
            case R.id.ic_register_user_rl:
                startActivity(RegisteredActivity.class);
                break;
            case R.id.add_friend_rl:
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
