package com.yehui.easemob.fragment.base;

import com.yehui.easemob.bean.FriendBean;
import com.yehui.utils.fragment.base.BaseFragment;

/**
 * Created by yehuijifeng
 * on 2015/12/6.
 * fragment的基本类
 */
public abstract class EasemobFragment extends BaseFragment {

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }

}
