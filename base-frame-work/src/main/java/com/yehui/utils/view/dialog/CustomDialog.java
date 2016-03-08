package com.yehui.utils.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yehui.utils.R;
import com.yehui.utils.utils.AppUtil;

/**
 * Created by yehuijifeng
 * on 2016/1/8.
 * 自定义提示框
 */
public class CustomDialog extends View implements View.OnClickListener {

    private View root;
    private Button dialog_default_ok_btn, dialog_default_cancel_btn;
    private AlertDialog dialog;
    private CustomOnClickListener customOnClickListener;
    private LinearLayout dialog_custom_layout;

    public CustomDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        root = View.inflate(getContext(), R.layout.dialog_custom, null);
        dialog_custom_layout = (LinearLayout) root.findViewById(R.id.dialog_custom_layout);
        dialog_default_ok_btn = (Button) root.findViewById(R.id.dialog_default_ok_btn);
        dialog_default_cancel_btn = (Button) root.findViewById(R.id.dialog_default_cancel_btn);
        dialog_default_ok_btn.setOnClickListener(this);
        dialog_default_cancel_btn.setOnClickListener(this);
        if (AppUtil.getAndroidSDKVersion() >= 21)
            dialog = new AlertDialog.Builder(getContext(), R.style.CustomDialog).setView(new EditText(getContext())).create();
        else
            dialog = new AlertDialog.Builder(getContext()).setView(new EditText(getContext())).create();

        dialog.setCanceledOnTouchOutside(false);
        //dialog.requestWindowFeature(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);//设置dialog与输入法交互
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_default_ok_btn) {
            dismissCustomDialog();
            customOnClickListener.onDetermine();
        } else {
            dismissCustomDialog();
            customOnClickListener.onCancel();
        }
    }

    /**
     * 确定和返回键的回调接口
     */
    public interface CustomOnClickListener {
        void onDetermine();

        void onCancel();
    }

    public void showCustomDialog(View customView, CustomOnClickListener customOnClickListener) {
        this.customOnClickListener = customOnClickListener;
        dialog.show();
        dialog.setContentView(root);
        if (dialog_custom_layout != null) {
            dialog_custom_layout.removeAllViews();
            dialog_custom_layout.addView(customView);
        }
    }

    /**
     * 关闭dialog
     */
    public void dismissCustomDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * 隐藏dialog
     */
    public void hideCustomDialog() {
        if (dialog != null)
            dialog.hide();
    }
}
