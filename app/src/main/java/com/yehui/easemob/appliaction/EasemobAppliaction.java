package com.yehui.easemob.appliaction;

import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.easemob.contants.BiaoqingMap;
import com.yehui.easemob.helper.ServerStatusHelper;
import com.yehui.utils.application.YehuiApplication;

/**
 * Created by
 * Luhao on 2016/2/20.
 * 全局的appliaction
 */
public class EasemobAppliaction extends YehuiApplication {

    public static UserInfoBean user;

    @Override
    public void initAppliaction() {

        //全局捕获异常的代理类
        //CrashHandler.getInstance().init(getApplicationContext());
        //初始化环信
        ServerStatusHelper.getInstance().initEasemob(this);
        //打印日志，调试模式开启，正式打包关闭
        ServerStatusHelper.getInstance().setDebugMode(true);
        //监听环信服务器状态
        ServerStatusHelper.getInstance().getConnectionStatus();
        //新消息回执
        //ReceiveMessageHelper.getInstance().allEvent();
        //初始化表情包
        BiaoqingMap.getInstance().initMap();
    }


}
