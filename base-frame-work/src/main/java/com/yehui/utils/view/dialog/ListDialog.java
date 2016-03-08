package com.yehui.utils.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.utils.AppUtil;
import com.yehui.utils.utils.DisplayUtil;

/**
 * Created by yehuijifeng
 * on 2016/1/8.
 * 菜单列表提示框
 */
public class ListDialog extends View implements View.OnClickListener {

    private View root;
    private ProgressDialog alertDialog;
    private LinearLayout list_layout;
    private ListOnClickListener listOnClickListener;
    private LinearLayout list_exit_layout;
    private LinearLayout.LayoutParams layoutParams;
    View itemView;
    TextView textView;

    public ListDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        root = View.inflate(getContext(), R.layout.dialog_list, null);
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissListDialog();
            }
        });
        list_layout = (LinearLayout) root.findViewById(R.id.list_layout);
        list_exit_layout = (LinearLayout) root.findViewById(R.id.list_exit_layout);
        list_exit_layout.setOnClickListener(this);
        if (AppUtil.getAndroidSDKVersion() >= 21)
            alertDialog = new ProgressDialog(getContext(), R.style.CustomDialog);
        else
            alertDialog = new ProgressDialog(getContext());

        alertDialog.setInverseBackgroundForced(true);
    }

    /**
     * 确定和返回键的回调接口
     */
    public interface ListOnClickListener {
        void onCancel();

        void onItems(int item, String itemName);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.list_exit_layout) {
            dismissListDialog();
            listOnClickListener.onCancel();
        }
    }

    class onItemClick implements OnClickListener {
        private int item;
        private String itemName;

        onItemClick(int item, String itemName) {
            this.item = item;
            this.itemName = itemName;
        }

        @Override
        public void onClick(View v) {
            dismissListDialog();
            listOnClickListener.onItems(item, itemName);
        }
    }

    public void showListDialog(String[] itemStr, ListOnClickListener listOnClickListener) {
        this.listOnClickListener = listOnClickListener;
        //initView();
        list_layout.removeAllViews();
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = DisplayUtil.dip2px(getContext(), 1);
        for (int i = 0; i < itemStr.length; i++) {
            itemView = View.inflate(getContext(), R.layout.dialog_list_item, null);
            textView = (TextView) itemView.findViewById(R.id.list_item_text);
            textView.setText(itemStr[i]);
            itemView.setLayoutParams(layoutParams);
            list_layout.addView(itemView);
            itemView.setOnClickListener(new onItemClick(i, itemStr[i]));
        }
        alertDialog.show();
        alertDialog.setContentView(root);
    }

    /**
     * 关闭dialog
     */
    public void dismissListDialog() {

        if (alertDialog != null)
            alertDialog.dismiss();
    }

    /**
     * 隐藏dialog
     */
    public void hideListDialog() {
        if (alertDialog != null)
            alertDialog.hide();
    }

}
