package com.yehui.easemob.fragment.base;

import com.yehui.easemob.bean.FriendBean;
import com.yehui.utils.fragment.base.BaseExpandableListViewFragment;

/**
 * Created by yehuijifeng
 * on 2016/1/4.
 * 多级列表的fragment的代理类
 */
public abstract class EasemobExpandableListViewFragment extends BaseExpandableListViewFragment {

    public void onEventMainThread(FriendBean friendBean) {
        getFriendStatus(friendBean);
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }
}
