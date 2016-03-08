package com.yehui.easemob.fragment.base;

import com.yehui.easemob.bean.FriendBean;
import com.yehui.utils.fragment.base.BaseStaggeredFragment;

/**
 * Created by yehuijifeng on 2016/1/4.
 */
public abstract class EasemobStaggeredFragment extends BaseStaggeredFragment {

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }
}
