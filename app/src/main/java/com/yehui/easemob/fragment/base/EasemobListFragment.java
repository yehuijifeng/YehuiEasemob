package com.yehui.easemob.fragment.base;

import com.yehui.easemob.bean.FriendBean;
import com.yehui.utils.fragment.base.BaseListFragment;

/**
 * Created by yehuijifeng
 * on 2016/1/4.
 * 列表fragment的代理类
 */
public abstract class EasemobListFragment extends BaseListFragment {

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }

}
