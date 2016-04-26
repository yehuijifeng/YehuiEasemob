package com.yehui.easemob.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobListActivity;
import com.yehui.easemob.helper.GroupHelper;
import com.yehui.utils.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Luhao on 2016/4/26.
 * 群管理
 */
public class GroupManageActivity extends EasemobListActivity implements View.OnClickListener {
    private RelativeLayout query_group_rl;

    @Override
    protected int getItemLayoutById(int id) {
        return R.layout.item_group_manage;
    }

    @Override
    protected void initView() {
        super.initView();
        query_group_rl = (RelativeLayout) findViewById(R.id.query_group_rl);
        query_group_rl.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        loadingView();
        GroupHelper.getInstance().getGroupsFromServer();//从服务器获取群列表
    }

    public void onEventMainThread(List<EMGroup> grouplist) {
        if (grouplist == null || grouplist.size() == 0) {
            loadingEmpty("你还没有加入任何群，赶快去加群吧!");
            return;
        }
        addAll(grouplist);
        notifyDataChange();
        loadingClose();
    }

    @Override
    protected void initItemData(BaseViewHolder holder, int position) {
        GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
        EMGroup emGroup = (EMGroup) data.get(position);
        groupViewHolder.group_name_text.setText(emGroup.getGroupName());
        groupViewHolder.group_number_text.setText(emGroup.getMaxUsers() + "/" + emGroup.getAffiliationsCount());
    }

    @Override
    protected BaseViewHolder getViewHolder(View itemView, int type) {
        return new GroupViewHolder(itemView);
    }

    @Override
    protected void onLongItemClick(RecyclerView parent, View itemView, int position) {
        //进入群聊界面
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_group_manage);
    }

    @Override
    protected String setTitleText() {
        return "群组管理";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_group_rl://查找群
                break;
        }
    }

    class GroupViewHolder extends BaseViewHolder {

        private ImageView group_manage_img;
        private TextView group_name_text, group_number_text;

        public GroupViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initItemView(View itemView) {
            group_manage_img = (ImageView) itemView.findViewById(R.id.group_manage_img);
            group_name_text = (TextView) itemView.findViewById(R.id.group_name_text);
            group_number_text = (TextView) itemView.findViewById(R.id.group_number_text);
        }
    }
}
