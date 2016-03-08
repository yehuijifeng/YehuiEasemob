package com.yehui.easemob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.contants.ResultContant;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.view.dialog.LoadingDialog;

import java.util.List;

/**
 * Created by Luhao
 * on 2016/2/23.
 * 注册页面
 */
public class RegisteredActivity extends EasemobActivity {
    private EditText
            user_register_name_edit,
            user_register_pwd_edit,
            user_register_pwd_determine_edit,
            user_register_email_edit,
            pwd_issue_answer_edit;
    private Spinner pwd_issue_spinner;
    private String
            pwd_issue_str,//密保问题
            user_register_name_str,//用户名
            user_register_pwd_str,//密码
            user_register_email_str,//密保邮箱
            pwd_issue_answer_str;//密保问题答案
    private Button user_register_btn;

    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;

    private LoadingDialog loadingDialog;

    @Override
    protected void setContentView() {
        setContentView(R.layout.layout_registered);
    }

    @Override
    protected String setTitleText() {
        return "用户注册";
    }

    @Override
    protected void initView() {
        user_register_name_edit = (EditText) findViewById(R.id.user_register_name_edit);
        user_register_pwd_edit = (EditText) findViewById(R.id.user_register_pwd_edit);
        user_register_pwd_determine_edit = (EditText) findViewById(R.id.user_register_pwd_determine_edit);
        user_register_email_edit = (EditText) findViewById(R.id.user_register_email_edit);
        pwd_issue_answer_edit = (EditText) findViewById(R.id.pwd_issue_answer_edit);
        pwd_issue_spinner = (Spinner) findViewById(R.id.pwd_issue_spinner);
        user_register_btn = (Button) findViewById(R.id.user_register_btn);
    }

    @Override
    protected void initData() {
        //实例化一个用户信息实体类
        userInfoBean = new UserInfoBean();
        userInfoDao = new UserInfoDao(this);
        loadingDialog = new LoadingDialog(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResourceStringArray(R.array.pwd_issue_content));
        //设置下拉查看资源
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pwd_issue_spinner.setAdapter(arrayAdapter);
        pwd_issue_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                pwd_issue_str = getResourceStringArray(R.array.pwd_issue_content)[arg2];
                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //注册环信账号
        user_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getVerifyData()) return;
                loadingDialog.showLoadingDialog("正在注册，请稍后……");
                ServerStatusHelper.getInstance().createAccountOnServer(user_register_name_str, user_register_pwd_str);
            }
        });
    }

    /**注册状态回调
     * @param serverBean
     */
    public void onEventMainThread(ServerBean serverBean) {
        if (serverBean.getStatusCode() == EasemobContant.createAccountOnServer) {
            loadingDialog.dismissLoadingDialog();
            showLongToast(serverBean.getStatusMsg());
            if (serverBean.isOperation()) {
                saveUseeInfoData();
                List<UserInfoBean> list = userInfoDao.queryAll();
                for (int i = 0; i < list.size(); i++) {
                    if (user_register_name_str.equals(list.get(i).getUserName())) {
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(MapContant.USER_INFO, list.get(i));
                        //把返回数据存入Intent
                        intent.putExtra(MapContant.USER_LOGIN_REGISTER_BUNDLE, bundle);
                        //设置返回数据
                        setResult(ResultContant.RESULT_LOGIN_BY_REGISTER, intent);
                        loadingDialog.dismissLoadingDialog();
                        //关闭Activity
                        finish();
                    }
                }
            }
        }
    }

    /**
     * 验证用户输入的注册信息
     */

    private boolean getVerifyData() {
        if (TextUtils.isEmpty(user_register_name_edit.getText().toString().trim())) {
            showShortToast("请输入注册账号");
            return false;
        }
        if (TextUtils.isEmpty(user_register_pwd_edit.getText().toString().trim())) {
            showShortToast("请输入注册密码");
            return false;
        }
        if (TextUtils.isEmpty(user_register_pwd_determine_edit.getText().toString().trim())) {
            showShortToast("请再次输入密码，以便确认");
            return false;
        }
        if (!user_register_pwd_edit.getText().toString().trim().equals(user_register_pwd_determine_edit.getText().toString().trim())) {
            showShortToast("两次密码不一致");
            return false;
        }
        if (TextUtils.isEmpty(user_register_email_edit.getText().toString().trim())) {
            showShortToast("请输入密保邮箱");
            return false;
        }
        if (TextUtils.isEmpty(pwd_issue_answer_edit.getText().toString().trim())) {
            showShortToast("请输入密保问题的答案");
            return false;
        }
        if (TextUtils.isEmpty(pwd_issue_str)) {
            showShortToast("请选择密保问题");
            return false;
        }
        user_register_name_str = user_register_name_edit.getText().toString().trim();
        user_register_pwd_str = user_register_pwd_edit.getText().toString().trim();
        user_register_email_str = user_register_email_edit.getText().toString().trim();
        pwd_issue_answer_str = pwd_issue_answer_edit.getText().toString().trim();
        return true;
    }

    private void saveUseeInfoData() {
        userInfoBean.setPwdIssue(pwd_issue_str);
        userInfoBean.setUserName(user_register_name_str);
        //userInfoBean.setUserPwd(user_register_pwd_str);
        userInfoBean.setPwdEmail(user_register_email_str);
        userInfoBean.setPwdAnswer(pwd_issue_answer_str);
        //saveUserDatatThread.start();
        userInfoDao.addData(userInfoBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
