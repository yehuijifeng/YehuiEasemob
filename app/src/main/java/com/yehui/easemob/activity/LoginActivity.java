package com.yehui.easemob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.contants.ResultContant;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.UserInfoContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.view.CircularImageView;
import com.yehui.utils.view.dialog.LoadingDialog;

import java.util.List;

/**
 * Created by Luhao
 * on 2016/2/23.
 * 用户登录界面
 */
public class LoginActivity extends EasemobActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private CircularImageView user_login_img;
    private EditText user_login_name_edit, user_login_pwd_edit;
    private Button user_login_btn;
    private CheckBox save_pwd_check, automatic_login_check;
    private TextView forget_pwd_text, registered_text;
    private LoadingDialog loadingDialog;
    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected String setTitleText() {
        return null;
    }

    @Override
    protected void initView() {
        user_login_img = (CircularImageView) findViewById(R.id.user_login_img);
        user_login_name_edit = (EditText) findViewById(R.id.user_login_name_edit);
        user_login_pwd_edit = (EditText) findViewById(R.id.user_login_pwd_edit);
        user_login_btn = (Button) findViewById(R.id.user_login_btn);
        save_pwd_check = (CheckBox) findViewById(R.id.save_pwd_check);
        automatic_login_check = (CheckBox) findViewById(R.id.automatic_login_check);
        forget_pwd_text = (TextView) findViewById(R.id.forget_pwd_text);
        registered_text = (TextView) findViewById(R.id.registered_text);

        user_login_btn.setOnClickListener(this);
        forget_pwd_text.setOnClickListener(this);
        registered_text.setOnClickListener(this);

        save_pwd_check.setOnCheckedChangeListener(this);
        automatic_login_check.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initData() {
        //实例化一个loading提示框
        loadingDialog = new LoadingDialog(LoginActivity.this);
        userInfoBean = new UserInfoBean();
        userInfoDao = new UserInfoDao(this);
        isSavePwd = sharedPreferences.getBoolean(MapContant.USER_IS_SAVE_PWD, false);
        save_pwd_check.setChecked(isSavePwd);
        if (isSavePwd) {
            user_login_name_edit.setText(sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_NAME));
            user_login_pwd_edit.setText(sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_PWD));
        }
        isAutomaticLogin = sharedPreferences.getBoolean(MapContant.USER_AUTOMSTIC_LOGIN, false);
        automatic_login_check.setChecked(isAutomaticLogin);
        if (isAutomaticLogin) {
            if (!getBoolean(MapContant.IS_OUT_LOGIN, false)) {
                getLogin(sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_NAME, null), sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_PWD, null));
            }
        }
        ServerStatusHelper.getInstance().setAutoLogin(isAutomaticLogin);
    }

    public void onEventMainThread(ServerBean serverBean) {
        if(serverBean.getStatusCode()== EasemobContant.userLogin){
            loadingDialog.dismissLoadingDialog();

            if(serverBean.isOperation()){
                List<UserInfoBean> list = userInfoDao.queryByWhere(UserInfoContant.userName, serverBean.getUserInfoBean().getUserName());
                if (list != null && list.size() != 0)
                    userInfoBean = list.get(0);
                userInfoBean.setUserName(serverBean.getUserInfoBean().getUserName());
                userInfoBean.setUserPwd(serverBean.getUserInfoBean().getUserPwd());
                if (list == null || list.size() == 0)
                    userInfoDao.addData(userInfoBean);
                else
                    userInfoDao.updateData(userInfoBean);
                EasemobAppliaction.user = userInfoBean;
                sharedPreferences.saveBoolean(MapContant.USER_IS_SAVE_PWD, isSavePwd);
                sharedPreferences.saveBoolean(MapContant.USER_AUTOMSTIC_LOGIN, isAutomaticLogin);
                if (isSavePwd) {
                    sharedPreferences.saveString(MapContant.USER_AUTOMSTIC_LOGIN_NAME,serverBean.getUserInfoBean().getUserName());
                    sharedPreferences.saveString(MapContant.USER_AUTOMSTIC_LOGIN_PWD, serverBean.getUserInfoBean().getUserPwd());
                }
                FriendStatushelper.getInstance().getFriendStatus();//初始化好友体系
                EMChat.getInstance().setAppInited();//告诉环信，ui已经初始化好了
                startActivity(HomeActivity.class);
                finish();
            }else{
                showLongToast(serverBean.getStatusMsg());
                user_login_pwd_edit.setText("");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultContant.RESULT_LOGIN) {//请求码
            switch (resultCode) {//返回码
                case ResultContant.RESULT_LOGIN_BY_REGISTER://如果是从注册页面返回的
                    Bundle bundle = data.getBundleExtra(MapContant.USER_LOGIN_REGISTER_BUNDLE);
                    userInfoBean = bundle.getParcelable(MapContant.USER_INFO);
                    user_login_name_edit.setText(userInfoBean.getUserName());
                    user_login_pwd_edit.setText(userInfoBean.getUserPwd());
                    break;
            }
        }
    }

    private void getLogin(String name,String pwd) {
        if (TextUtils.isEmpty(name)) {
            showLongToast("请输入十位数字用户名");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showLongToast("请输入密码");
            return;
        }
        loadingDialog.showLoadingDialog("正在登陆，请稍后……");
        ServerStatusHelper.getInstance().login(name, pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_login_btn://登陆
                getLogin(user_login_name_edit.getText().toString().trim(), user_login_pwd_edit.getText().toString().trim());
                break;
            case R.id.forget_pwd_text://忘记密码

                break;
            case R.id.registered_text://注册
                startActivityForResult(RegisteredActivity.class, null, ResultContant.RESULT_LOGIN);
                break;
        }
    }

    private boolean isSavePwd, isAutomaticLogin;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.save_pwd_check) {
            //记住密码
            isSavePwd = isChecked;
            if (!isSavePwd) {
                isAutomaticLogin = isSavePwd;
                automatic_login_check.setChecked(isSavePwd);
            }
        } else {
            //自动登陆
            isAutomaticLogin = isChecked;
            if (isAutomaticLogin) {
                isSavePwd = isAutomaticLogin;
                save_pwd_check.setChecked(isAutomaticLogin);
            }
        }
    }
}
