package com.yehui.easemob.activity;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yehui.easemob.activity.base.EasemobListActivity;
import com.yehui.easemob.bean.GetMessageBean;
import com.yehui.easemob.contants.MessageContant;
import com.yehui.utils.adapter.base.BaseViewHolder;

/**
 * Created by Luhao on 2016/3/8.
 */
public class MessageActivity extends EasemobListActivity {

    @Override
    protected void getMessageStatus(GetMessageBean getMessageBean) {
        switch (getMessageBean.getGetMsgCode()) {
            case MessageContant.getMsgByText:

                break;
            case MessageContant.getMsgByVoice://语音
                break;
            case MessageContant.getMsgByImage://图片
                break;
            case MessageContant.getMsgByLocation://地理位置
                break;
            case MessageContant.getMsgByFile://文件
                break;
        }
    }

    @Override
    protected int getItemLayoutById(int resId) {
        return 0;
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {

    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return null;
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {

    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {

    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected String setTitleText() {
        return "好友名称";
    }
}
