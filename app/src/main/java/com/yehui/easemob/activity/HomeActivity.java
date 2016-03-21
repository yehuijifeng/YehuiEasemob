package com.yehui.easemob.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobViewPagerActivity;
import com.yehui.easemob.fragment.FriendFragment;
import com.yehui.easemob.fragment.MessageFragment;
import com.yehui.easemob.fragment.SquareFragment;
import com.yehui.utils.view.titleview.MyTitleView;

/**
 * Created by Luhao
 * on 2016/2/20.
 * 聊天界面的首页，viewpager
 */
public class HomeActivity extends EasemobViewPagerActivity {

    @Override
    protected View setTabView(ViewGroup container, int position) {
        View view = inflate(R.layout.item_easemob_viewpager_tab, container, false);
        TextView tabText = (TextView) view.findViewById(R.id.viewpager_tab_text);
        ImageView tabImage = (ImageView) view.findViewById(R.id.viewpager_tab_img);
        switch (position) {
            case 0:
                tabText.setText("消息");
                tabImage.setImageDrawable(getResourceDrawable(R.drawable.bg_message_selected));
                break;
            case 1:
                tabText.setText("好友");
                tabImage.setImageDrawable(getResourceDrawable(R.drawable.bg_friend_selected));
                break;
            case 2:
                tabText.setText("个人");
                tabImage.setImageDrawable(getResourceDrawable(R.drawable.bg_home_selected));
                break;
        }
        return view;
    }

    @Override

    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected String setTitleText() {
        return null;
    }

    @Override
    protected boolean isShowBar() {
        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitleMode(MyTitleView.TitleMode.NO_BACK_NORMAL);
        mViewList.add(new MessageFragment());
        mViewList.add(new FriendFragment());
        mViewList.add(new SquareFragment());
        setPageNumber(0);
    }

    @Override
    protected void initData() {
        super.initData();
        setIsFastClick(false);
    }
}
