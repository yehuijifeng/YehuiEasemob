package com.yehui.easemob.activity;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.ServerBean;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.contants.MapContant;
import com.yehui.easemob.contants.UserInfoContant;
import com.yehui.easemob.db.UserInfoDao;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.utils.AppUtil;

import java.util.List;

/**
 * Created by Luhao
 * on 2016/3/16.
 * loading页
 */
public class LoadingActivity extends EasemobActivity {
    private ImageView loading_img;
    private TextView welcome_text, app_version_text;
    private boolean isAutomaticLogin;
    private UserInfoBean userInfoBean;
    private UserInfoDao userInfoDao;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_loading);
    }

    @Override
    protected String setTitleText() {
        return null;
    }

    @Override
    protected void initView() {
        loading_img = (ImageView) findViewById(R.id.loading_img);
        welcome_text = (TextView) findViewById(R.id.welcome_text);
        app_version_text = (TextView) findViewById(R.id.app_version_text);

    }

    @Override
    protected void initData() {
        super.initData();
        userInfoBean = new UserInfoBean();
        userInfoDao = new UserInfoDao(this);
        app_version_text.append(AppUtil.getVersionName(this));
        isAutomaticLogin = sharedPreferences.getBoolean(MapContant.USER_AUTOMSTIC_LOGIN, false);
        if (isAutomaticLogin) {
            ServerStatusHelper.getInstance().login(sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_NAME, null), sharedPreferences.getString(MapContant.USER_AUTOMSTIC_LOGIN_PWD, null));
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private Handler handler = new Handler();

    /**
     * 登录回调
     *
     * @param serverBean
     */
    public void onEventMainThread(final ServerBean serverBean) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (serverBean.getStatusCode() == EasemobContant.userLogin) {
                    if (serverBean.isOperation()) {
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
                        FriendStatushelper.getInstance().getFriendStatus();//初始化好友体系
                        EMChat.getInstance().setAppInited();//告诉环信，ui已经初始化好了
                        startActivity(HomeActivity.class);
                        finish();
                    } else {
                        showLongToast(serverBean.getStatusMsg());
                        startActivity(LoginActivity.class);
                    }
                }
            }
        }, 1000);
    }
}
