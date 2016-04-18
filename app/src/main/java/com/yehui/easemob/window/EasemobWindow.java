package com.yehui.easemob.window;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.yehui.easemob.R;
import com.yehui.easemob.bean.MessageBean;
import com.yehui.easemob.helper.ReceiveMessageHelper;
import com.yehui.easemob.helper.SendMessageHelper;
import com.yehui.easemob.utils.TextUtil;
import com.yehui.utils.adapter.base.BaseAdapter;
import com.yehui.utils.view.popupwindow.PopupWindowAll;

/**
 * Created by Luhao
 * on 2016/4/13.
 * 聊天对话的单个操作
 */
public class EasemobWindow implements View.OnClickListener {

    private Context context;
    private MessageBean messageBean;
    private View popView;
    private TextView window_delete_text,
            window_copy_text,
            window_send_text,
            window_exit_text,
            window_collect_text;
    private PopupWindowAll popupWindowAll;
    private BaseAdapter baseAdapter;

    public EasemobWindow(Context context) {
        this.context = context;
        popView = View.inflate(context, R.layout.window_easemob_longclick, null);
        window_delete_text = (TextView) popView.findViewById(R.id.window_delete_text);
        window_copy_text = (TextView) popView.findViewById(R.id.window_copy_text);
        window_send_text = (TextView) popView.findViewById(R.id.window_send_text);
        window_exit_text = (TextView) popView.findViewById(R.id.window_exit_text);
        window_collect_text = (TextView) popView.findViewById(R.id.window_collect_text);
        window_delete_text.setOnClickListener(this);
        window_copy_text.setOnClickListener(this);
        window_send_text.setOnClickListener(this);
        window_exit_text.setOnClickListener(this);
        window_collect_text.setOnClickListener(this);
        popupWindowAll = new PopupWindowAll(context);
    }

    public void showAtLocation(View parent, MessageBean messageBean, BaseAdapter baseAdapter) {
        this.messageBean = messageBean;
        this.baseAdapter = baseAdapter;
        if (messageBean.getEmMessage().getBody() instanceof TextMessageBody) {
            window_copy_text.setVisibility(View.VISIBLE);
        } else {
            window_copy_text.setVisibility(View.GONE);
        }
        if (messageBean.getEmMessage().direct == EMMessage.Direct.SEND) {
            window_exit_text.setVisibility(View.VISIBLE);
        } else {
            window_exit_text.setVisibility(View.GONE);
        }
        popupWindowAll.showAtLocationByTop(parent, popView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_delete_text://删除消息
                ReceiveMessageHelper.getInstance().removeMessageById(messageBean.getEmMessage().getUserName(), messageBean.getEmMessage());
                popupWindowAll.dismissWindow();
                baseAdapter.data.remove(messageBean);
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.window_copy_text://复制消息到剪切板
                TextUtil.copy(messageBean.getContent(), context);
                Toast.makeText(context, "文本复制成功", Toast.LENGTH_SHORT).show();
                popupWindowAll.dismissWindow();
                break;
            case R.id.window_send_text://转发消息
                Toast.makeText(context, "暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.window_exit_text://撤回消息
                SendMessageHelper.getInstance().sendRevokeMessage(context,messageBean.getEmMessage());
                popupWindowAll.dismissWindow();
                break;
            case R.id.window_collect_text://收藏
                Toast.makeText(context, "暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
