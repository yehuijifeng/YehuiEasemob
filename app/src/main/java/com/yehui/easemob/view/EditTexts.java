package com.yehui.easemob.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Luhao on 2016/3/21.
 * 重写EditText的监听事件，目的是为了监听粘贴
 */
public class EditTexts extends EditText {
    private boolean isCopy;

    public EditTexts(Context context) {
        super(context);
    }

    public EditTexts(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean copy) {
        isCopy = copy;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        /**
         * id:16908319
         全选
         id:16908328
         选择
         id:16908320
         剪贴
         id:16908321
         复制
         id:16908322
         粘贴
         id:16908324
         输入法
         **/
        if (id == 16908322) {
            setCopy(true);
        }
        return super.onTextContextMenuItem(id);
    }

}
