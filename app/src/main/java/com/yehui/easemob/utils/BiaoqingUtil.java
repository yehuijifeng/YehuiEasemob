package com.yehui.easemob.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.yehui.easemob.contants.BiaoqingMap;

/**
 * Created by Luhao on 2016/3/16.
 */
public class BiaoqingUtil {

    public SpannableString getBiaoqing(Context context, String editStr) {
        if (!TextUtils.isEmpty(editStr)) {
            int i = editStr.lastIndexOf("[");// 获取表情开始位置
            if (i == -1) return null;
            String edits = editStr.substring(i);//阶段字符串从表情开始位置
            int j = edits.lastIndexOf("]");// 获取表情结束位置
            if (j == -1) return null;
            String biaoqings = edits.substring(i, j);//阶段字符串从表情结束位置
            if (!TextUtils.isEmpty(biaoqings)) {
                String cs = biaoqings.substring(0, 3);
                if (cs.indexOf("[ee_") != -1) {// 判断是否是一个表情
                    String biaoqingNumber = biaoqings.substring(3, biaoqings.length() - 1);//得到表情编号
                    int drawableId = BiaoqingMap.getInstance().getBiaoqingMap().get(biaoqingNumber);
                    SpannableString spannableString = new SpannableString(biaoqingNumber);
                    //获取Drawable资源
                    Drawable drawable = context.getResources().getDrawable(drawableId);
                    //这句话必须，不然图片不显示
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    //创建ImageSpan
                    ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                    //用ImageSpan替换文本
                    spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        Editable e = editText.getText();
//                        int st = editText.getSelectionStart();
//                        int en = editText.getSelectionEnd();
//                        e.replace(st, en, spannableString);
                    return spannableString;
                }
                getBiaoqing(context, edits);
            } else
                return null;
        }
        return null;
    }
}
