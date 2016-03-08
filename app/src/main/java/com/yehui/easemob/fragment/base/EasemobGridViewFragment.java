package com.yehui.easemob.fragment.base;

import com.yehui.easemob.bean.FriendBean;
import com.yehui.utils.fragment.base.BaseGridViewFragment;

/**
 * Created by yehuijifeng
 * on 2016/1/4.
 * 表格布局的fragment的代理类
 */
public abstract class EasemobGridViewFragment extends BaseGridViewFragment {

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }
}
