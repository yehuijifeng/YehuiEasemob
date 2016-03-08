package com.yehui.utils.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.utils.AppUtil;
import com.yehui.utils.utils.MD5Util;


/**
 * Created by luhao
 * on 2015/10/15.
 * 密码输入框
 */
public class PwdDialog extends View implements TextWatcher, View.OnClickListener, View.OnKeyListener {

    private EditText pwdEdit;
    private TextView pwd_frame_content;
    private LinearLayout pwd_linear;
    private ImageView image_pwd_one, image_pwd_tow, image_pwd_three, image_pwd_four, image_pwd_five, image_pwd_six;
    private Button pwd_ok_btn, pwd_cancel_btn;
    private AlertDialog alertDialog;
    private View root;
    private PwdDialogListener pwdDialogListener;

    /**
     * 因为所有的提示框都只会在一个app中同时实例化一个，所以可以用单例控制
     */
    public PwdDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        //show()必须在serContentView
        root = View.inflate(getContext(), R.layout.dialog_password, null);
        pwdEdit = (EditText) root.findViewById(R.id.user_account_pwd_text);
        pwd_frame_content = (TextView) root.findViewById(R.id.pwd_frame_content);
        pwdEdit.addTextChangedListener(this);
        pwd_linear = (LinearLayout) root.findViewById(R.id.pwd_linear);
        pwd_linear.setOnClickListener(this);
        image_pwd_one = (ImageView) root.findViewById(R.id.image_pwd_one);
        image_pwd_one = (ImageView) root.findViewById(R.id.image_pwd_one);
        image_pwd_one = (ImageView) root.findViewById(R.id.image_pwd_one);
        image_pwd_tow = (ImageView) root.findViewById(R.id.image_pwd_tow);
        image_pwd_three = (ImageView) root.findViewById(R.id.image_pwd_three);
        image_pwd_four = (ImageView) root.findViewById(R.id.image_pwd_four);
        image_pwd_five = (ImageView) root.findViewById(R.id.image_pwd_five);
        image_pwd_six = (ImageView) root.findViewById(R.id.image_pwd_six);
        pwd_ok_btn = (Button) root.findViewById(R.id.dialog_default_ok_btn);
        pwd_ok_btn.setOnClickListener(this);
        pwd_cancel_btn = (Button) root.findViewById(R.id.dialog_default_cancel_btn);
        pwd_cancel_btn.setOnClickListener(this);
        pwd_ok_btn.setEnabled(false);
        if (AppUtil.getAndroidSDKVersion() >= 21)
            alertDialog = new AlertDialog.Builder(getContext(), R.style.CustomDialog).setView(new EditText(getContext())).create();
        else
            alertDialog = new AlertDialog.Builder(getContext()).setView(new EditText(getContext())).create();
        //alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                dismissPwdDialog();
                pwdDialogListener.onCancel();
                return true;
            }
            return false;//已处理
        }
        return false;
    }

    /**
     * 确定和返回键的回调接口
     */
    public interface PwdDialogListener {
        void onDetermine(String password);

        void onCancel();
    }

    /**
     * 关闭dialog，不占内存，中断dialog中的操作
     */
    public void dismissPwdDialog() {
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    /**
     * 隐藏dialog，占内存，但不中断dialog中的操作
     */
    public void hidePwdDialog() {
        if (alertDialog != null)
            alertDialog.hide();
    }

    /**
     * 显示dialog
     *
     * @param wpdStr            提示框中的文字
     * @param pwdDialogListener 确定，返回键的回调函数
     */
    public void showPwdDialog(final String wpdStr, final PwdDialogListener pwdDialogListener) {
        this.pwdDialogListener = pwdDialogListener;
        initView();
        alertDialog.show();
        alertDialog.setContentView(root);
        pwd_frame_content.setText(wpdStr + "");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pwd_linear) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //显示键盘
            imm.showSoftInput(pwdEdit, 0);//editText为需要点击的文本框
        } else if (v.getId() == R.id.dialog_default_ok_btn) {
            dismissPwdDialog();
            pwdDialogListener.onDetermine(pwdEdit != null && pwdEdit.length() == 6 ? MD5Util.MD5(pwdEdit.getText().toString()) : null);
        } else if (v.getId() == R.id.dialog_default_cancel_btn) {
            dismissPwdDialog();
            pwdDialogListener.onCancel();

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int pwdLength = pwdEdit.length();
        image_pwd_one.setVisibility(View.INVISIBLE);
        image_pwd_tow.setVisibility(View.INVISIBLE);
        image_pwd_three.setVisibility(View.INVISIBLE);
        image_pwd_four.setVisibility(View.INVISIBLE);
        image_pwd_five.setVisibility(View.INVISIBLE);
        image_pwd_six.setVisibility(View.INVISIBLE);
        if (pwdLength > 0) {
            pwd_ok_btn.setEnabled(false);
            image_pwd_one.setVisibility(View.VISIBLE);
            if (pwdLength > 1) {
                image_pwd_tow.setVisibility(View.VISIBLE);
                if (pwdLength > 2) {
                    image_pwd_three.setVisibility(View.VISIBLE);
                    if (pwdLength > 3) {
                        image_pwd_four.setVisibility(View.VISIBLE);
                        if (pwdLength > 4) {
                            image_pwd_five.setVisibility(View.VISIBLE);
                            if (pwdLength > 5) {
                                image_pwd_six.setVisibility(View.VISIBLE);
                                pwd_ok_btn.setEnabled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}



