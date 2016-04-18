package com.yehui.easemob.fragment.base;

import com.easemob.chat.EMMessage;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.bean.MessageBean;
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

    public void onEventMainThread(MessageBean messageBean) {
        if (messageBean.getEmMessage().getType() == EMMessage.Type.CMD)
            getNewCMDMessage(messageBean);
        else
            getNewMessage(messageBean);
    }

    protected void showMessageNumberIcon() {
        ((EasemobActivity) parentActivity).showMessageCount();
    }

    protected void getFriendStatus(FriendBean friendBean) {

    }

    /**
     * 接收新消息
     *
     * @param messageBean
     */
    public void getNewMessage(MessageBean messageBean) {
        showMessageNumberIcon();
    }

    /**
     * 接收透传消息
     *
     * @param messageBean
     */
    public void getNewCMDMessage(MessageBean messageBean) {

    }

}
