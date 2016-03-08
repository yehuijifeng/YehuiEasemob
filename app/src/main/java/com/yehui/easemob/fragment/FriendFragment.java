package com.yehui.easemob.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yehui.easemob.R;
import com.yehui.easemob.appliaction.EasemobAppliaction;
import com.yehui.easemob.bean.FriendBean;
import com.yehui.easemob.contants.EasemobContant;
import com.yehui.easemob.fragment.base.EasemobListFragment;
import com.yehui.easemob.helper.FriendStatushelper;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.view.CircularImageView;
import com.yehui.utils.view.dialog.ListDialog;
import com.yehui.utils.view.dialog.PromptDialog;
import com.yehui.utils.view.titleview.MyTitleView;

/**
 * Created by Luhao
 * on 2016/2/20.
 * 好友列表
 */
public class FriendFragment extends EasemobListFragment {

    private MyTitleView titleView;
    private ListDialog listDialog;
    private PromptDialog promptDialog;
    private String[] friendOperation = new String[]{"修改备注", "删除好友", "加入黑名单"};

    @Override
    protected void refresh() {
        super.refresh();
        clearAll();
        //loadingView();
        FriendStatushelper.getInstance().getFriendsLists(2);//加载好友列表
    }

    @Override
    protected void reLoad() {
        super.reLoad();
        loadingView();
        clearAll();
        FriendStatushelper.getInstance().getFriendsLists(3);//加载好友列表
    }

    @Override
    protected void initView(View parentView) {
        super.initView(parentView);
        titleView = (MyTitleView) parentView.findViewById(R.id.my_title_view);
        titleView.setTitleText("好友");
        titleView.setTitleMode(MyTitleView.TitleMode.NO_BACK_IMAGE);
    }

    @Override
    protected void initData() {
        super.initData();
        listDialog = new ListDialog(parentActivity);
        promptDialog = new PromptDialog(parentActivity);
        loadingView();
        FriendStatushelper.getInstance().getFriendsLists(1);//加载好友列表
    }

    @Override
    public void getFriendStatus(FriendBean friendBean) {
        switch (friendBean.getStatusCode()) {
            case EasemobContant.addUserToBlackList://添加黑名单
                showLongToast(friendBean.getStatusMsg());
                break;
            case EasemobContant.deleteContact://删除好友
                showLongToast(friendBean.getStatusMsg());
                if (friendBean.isOperation()) {
                    reLoad();
                }
                break;
            case EasemobContant.getFriendList://获取好友列表
                if (friendBean.isOperation()) {
                    addAll(friendBean.getFriendNameist());
                    if (friendBean.getStatusMsg().equals("1")) {//正常加载
                        loadingSuccess();
                        notifyDataChange();
                    } else if (friendBean.getStatusMsg().equals("2")) {//刷新加载
                        refreshSuccess();
                    } else if (friendBean.getStatusMsg().equals("3")) {//重载
                        loadingSuccess();
                        //notifyDataChange();
                    }
                    if (data == null || data.size() == 0) {
                        loadingEmpty("你还没有好友，快去个人中心添加好友吧！");
                    }
                } else {
                    loadingFail("获取好友列表失败", "重试");
                }
                break;
        }
    }

    @Override
    protected int getItemLayoutResId(int type) {
        return R.layout.item_friend;
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        String userName = (String) data.get(position);
        FriendViewHolder messageViewHolder = (FriendViewHolder) holder;
        imageLoader.displayImage("", messageViewHolder.friend_icon_img, EasemobAppliaction.roundOptions);
        messageViewHolder.friend_name_text.setText(userName);
        //messageViewHolder.friend_message_text.setText(emMessage.getFrom());
        messageViewHolder.friend_status_text.setText("4G/wifi");
        //messageViewHolder.message_number_text.setText(emConversation.getMsgCount() + "");
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new FriendViewHolder(itemView);
    }

    @Override
    protected void onItemClick(RecyclerView parent, View itemView, int position) {
        //跳转聊天页面
        showShortToast("第" + position + "行");
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, final int position) {
        //显示更多操作
        listDialog.showListDialog(friendOperation, new ListDialog.ListOnClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onItems(int item, String itemName) {
                if (friendOperation[item].equals("修改备注")) {
                    //修改备注
                } else if (friendOperation[item].equals("删除好友")) {
                    //删除好友
                    promptDialog.showPromptDialog("确定删除你的好友：" + data.get(position) + "？", new PromptDialog.PromptOnClickListener() {
                        @Override
                        public void onDetermine() {
                            FriendStatushelper.getInstance().deleteFriends(data.get(position) + "");//删除好友，好友name
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else if (friendOperation[item].equals("加入黑名单")) {
                    //添加黑名单
                    promptDialog.showPromptDialog("确定将你的好友：" + data.get(position) + "加入黑名单？", new PromptDialog.PromptOnClickListener() {
                        @Override
                        public void onDetermine() {
                            FriendStatushelper.getInstance().addUserToBlackLists(data.get(position) + "", true);//加入黑名单，好友name，是否双发都不发送接收消息
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected int setFragmentLayoutId() {
        return R.layout.fragment_friend;
    }

    private class FriendViewHolder extends BaseViewHolder {

        private CircularImageView friend_icon_img;
        private TextView friend_name_text, friend_message_text, friend_status_text, message_number_text;

        public FriendViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            friend_icon_img = (CircularImageView) itemView.findViewById(R.id.friend_icon_img);
            friend_name_text = (TextView) itemView.findViewById(R.id.friend_name_text);
            //friend_message_text = (TextView) itemView.findViewById(R.id.friend_message_text);
            friend_status_text = (TextView) itemView.findViewById(R.id.friend_status_text);
            //message_number_text = (TextView) itemView.findViewById(R.id.message_number_text);
        }
    }
}









