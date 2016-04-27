package com.yehui.easemob.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yehui.easemob.R;
import com.yehui.easemob.activity.base.EasemobActivity;
import com.yehui.easemob.helper.GroupHelper;

/**
 * Created by Luhao on 2016/4/27.
 */
public class GroupCreateActivity extends EasemobActivity {
    private EditText group_name_edit, group_desc_edit;
    private CheckBox is_allow_invite_check, id_need_approval_required_check;
    private RadioGroup max_users_radio_group, is_public_radio_group;
    private RadioButton
            tow_users_radio_btn,
            five_users_radio_btn,
            ten_users_radio_btn,
            tow_ten_users_radio_btn,
            public_radio_btn,
            private_radio_btn;
    private Button group_ok_btn;

    private String groupName;//要创建的群聊的名称
    private String desc;//群聊简介
    private String[] members;//群聊成员,用户名;为空时这个创
    private boolean isAllowInvite;//是否允许群成员邀请
    private int maxUsers;//创建群的数量
    private boolean isPublic;//是否是公开群；公开群：公开群
    private boolean idNeedApprovalRequired;//是否需要群验证

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_create_group);
    }

    @Override
    protected String setTitleText() {
        return "创建群";
    }

    @Override
    protected void initView() {
        group_name_edit = (EditText) findViewById(R.id.group_name_edit);
        group_desc_edit = (EditText) findViewById(R.id.group_desc_edit);
        is_allow_invite_check = (CheckBox) findViewById(R.id.is_allow_invite_check);
        id_need_approval_required_check = (CheckBox) findViewById(R.id.id_need_approval_required_check);
        max_users_radio_group = (RadioGroup) findViewById(R.id.max_users_radio_group);
        is_public_radio_group = (RadioGroup) findViewById(R.id.is_public_radio_group);
        tow_users_radio_btn = (RadioButton) findViewById(R.id.tow_users_radio_btn);
        five_users_radio_btn = (RadioButton) findViewById(R.id.five_users_radio_btn);
        ten_users_radio_btn = (RadioButton) findViewById(R.id.ten_users_radio_btn);
        tow_ten_users_radio_btn = (RadioButton) findViewById(R.id.tow_ten_users_radio_btn);
        public_radio_btn = (RadioButton) findViewById(R.id.public_radio_btn);
        private_radio_btn = (RadioButton) findViewById(R.id.private_radio_btn);
        group_ok_btn = (Button) findViewById(R.id.group_ok_btn);
        group_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加群信息
                GroupHelper.getInstance().createGroup(groupName, desc, members, isAllowInvite, maxUsers, isPublic, idNeedApprovalRequired);
            }
        });

    }
}
