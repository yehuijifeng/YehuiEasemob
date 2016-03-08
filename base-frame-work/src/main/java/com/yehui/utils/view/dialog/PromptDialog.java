package com.yehui.utils.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.utils.AppUtil;
import com.yehui.utils.utils.EmptyUtil;

/**
 * Created by yehuijifeng
 * on 2016/1/8.
 * 提示框
 */
public class PromptDialog extends View implements View.OnClickListener, View.OnKeyListener {

    private View root;
    private TextView prompt_title_text, prompt_content_text;
    private Button dialog_default_ok_btn, dialog_default_cancel_btn;
    private ProgressDialog dialog;
    private PromptOnClickListener promptOnClickListener;

    /**提供外部调用，禁止按返回键取消dialog
     * @param bl
     */
    public void setCanBack(boolean bl){
        if(dialog!=null&&!bl){
            dialog.setCancelable(false);
        }
    }
    public PromptDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        root = View.inflate(getContext(), R.layout.dialog_prompt, null);
        prompt_title_text = (TextView) root.findViewById(R.id.prompt_title_text);
        prompt_content_text = (TextView) root.findViewById(R.id.prompt_content_text);
        dialog_default_ok_btn = (Button) root.findViewById(R.id.dialog_default_ok_btn);
        dialog_default_cancel_btn = (Button) root.findViewById(R.id.dialog_default_cancel_btn);
        dialog_default_ok_btn.setOnClickListener(this);
        dialog_default_cancel_btn.setOnClickListener(this);

        if (AppUtil.getAndroidSDKVersion() >= 21)
            dialog = new ProgressDialog(getContext(), R.style.CustomDialog);
        else
            dialog = new ProgressDialog(getContext());

        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_default_ok_btn) {
            dismissPromptDialog();
            promptOnClickListener.onDetermine();
        } else {
            dismissPromptDialog();
            promptOnClickListener.onCancel();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK ) {  //表示按返回键 时的操作
                dismissPromptDialog();
                promptOnClickListener.onCancel();
                return true;
            }
            return false;//已处理
        }
        return false;
    }

    /**
     * 确定和返回键的回调接口
     */
    public interface PromptOnClickListener {
        void onDetermine();

        void onCancel();
    }


    public void showPromptDialog(PromptOnClickListener promptOnClickListener) {
        showPromptDialog(null, null, promptOnClickListener);
    }

    public void showPromptDialog(String contentStr, PromptOnClickListener promptOnClickListener) {
        showPromptDialog(null, contentStr, promptOnClickListener);
    }

    public void showPromptDialog(String titleStr, String contentStr, PromptOnClickListener promptOnClickListener) {
        this.promptOnClickListener = promptOnClickListener;
        //initView();
        if (!EmptyUtil.isStringEmpty(titleStr))
            prompt_title_text.setText(titleStr);
        if (!EmptyUtil.isStringEmpty(contentStr))
            prompt_content_text.setText(contentStr);
        dialog.show();
        dialog.setContentView(root);
    }

    /**
     * 关闭dialog
     */
    public void dismissPromptDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    /**
     * 隐藏dialog
     */
    public void hidePromptDialog() {
        if (dialog != null)
            dialog.hide();
    }
}
