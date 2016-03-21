package com.yehui.easemob.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.yehui.easemob.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luhao on 2016/3/16.
 */
public class BiaoqingUtil {

    /**
     * Pattern.compile(String str);//编译正则表达式,在此过程中创建一个新的模式实例。
     * Pattern.quote(String str);//引用给定字符串使用" \ Q "和" \ E”,这样所有其它元字符失去他们的特殊意义。
     */
    private BiaoqingUtil() {
        for (int i = 0; i < emojis.length; i++) {
            biaoqingMap.put(Pattern.compile(Pattern.quote(emojis[i])), icons[i]);
        }
    }

    private static BiaoqingUtil instance;

    public static synchronized BiaoqingUtil getInstance() {
        if (instance == null) {
            synchronized (BiaoqingUtil.class) {
                if (instance == null) {
                    instance = new BiaoqingUtil();
                }
            }
        }
        return instance;
    }

    public final String ee_1 = "[ee_1]";
    public final String ee_2 = "[ee_2]";
    public final String ee_3 = "[ee_3]";
    public final String ee_4 = "[ee_4]";
    public final String ee_5 = "[ee_5]";
    public final String ee_6 = "[ee_6]";
    public final String ee_7 = "[ee_7]";
    public final String ee_8 = "[ee_8]";
    public final String ee_9 = "[ee_9]";
    public final String ee_10 = "[ee_10]";
    public final String ee_11 = "[ee_11]";
    public final String ee_12 = "[ee_12]";
    public final String ee_13 = "[ee_13]";
    public final String ee_14 = "[ee_14]";
    public final String ee_15 = "[ee_15]";
    public final String ee_16 = "[ee_16]";
    public final String ee_17 = "[ee_17]";
    public final String ee_18 = "[ee_18]";
    public final String ee_19 = "[ee_19]";
    public final String ee_20 = "[ee_20]";
    public final String ee_21 = "[ee_21]";
    public final String ee_22 = "[ee_22]";
    public final String ee_23 = "[ee_23]";
    public final String ee_24 = "[ee_24]";
    public final String ee_25 = "[ee_25]";
    public final String ee_26 = "[ee_26]";
    public final String ee_27 = "[ee_27]";
    public final String ee_28 = "[ee_28]";
    public final String ee_29 = "[ee_29]";
    public final String ee_30 = "[ee_30]";
    public final String ee_31 = "[ee_31]";
    public final String ee_32 = "[ee_32]";
    public final String ee_33 = "[ee_33]";
    public final String ee_34 = "[ee_34]";
    public final String ee_35 = "[ee_35]";

    public String[] emojis = new String[]{
            ee_1,
            ee_2,
            ee_3,
            ee_4,
            ee_5,
            ee_6,
            ee_7,
            ee_8,
            ee_9,
            ee_10,
            ee_11,
            ee_12,
            ee_13,
            ee_14,
            ee_15,
            ee_16,
            ee_17,
            ee_18,
            ee_19,
            ee_20,
            ee_21,
            ee_22,
            ee_23,
            ee_24,
            ee_25,
            ee_26,
            ee_27,
            ee_28,
            ee_29,
            ee_30,
            ee_31,
            ee_32,
            ee_33,
            ee_34,
            ee_35,
    };

    public int[] icons = new int[]{
            R.drawable.ee_1,
            R.drawable.ee_2,
            R.drawable.ee_3,
            R.drawable.ee_4,
            R.drawable.ee_5,
            R.drawable.ee_6,
            R.drawable.ee_7,
            R.drawable.ee_8,
            R.drawable.ee_9,
            R.drawable.ee_10,
            R.drawable.ee_11,
            R.drawable.ee_12,
            R.drawable.ee_13,
            R.drawable.ee_14,
            R.drawable.ee_15,
            R.drawable.ee_16,
            R.drawable.ee_17,
            R.drawable.ee_18,
            R.drawable.ee_19,
            R.drawable.ee_20,
            R.drawable.ee_21,
            R.drawable.ee_22,
            R.drawable.ee_23,
            R.drawable.ee_24,
            R.drawable.ee_25,
            R.drawable.ee_26,
            R.drawable.ee_27,
            R.drawable.ee_28,
            R.drawable.ee_29,
            R.drawable.ee_30,
            R.drawable.ee_31,
            R.drawable.ee_32,
            R.drawable.ee_33,
            R.drawable.ee_34,
            R.drawable.ee_35,
    };

    /**
     * 表情集合
     */
    private final Map<Pattern, Object> biaoqingMap = new HashMap<>();

    /**
     * 仅作参考，不是方法，不可调用
     *
     * @param context
     * @param message
     * @param editText
     */
    private void getTextStr(Context context, String message, EditText editText) {
        Spannable span = showBiaoqing(context, message);
        // 设置内容
        editText.setText(span, TextView.BufferType.SPANNABLE);
    }

    /**
     * 适配所有表情
     *
     * @param context
     * @param text
     * @return
     */
    public Spannable showBiaoqing(Context context, CharSequence text) {
        //获得工厂类的SpannableString对象
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    /**
     * 添加表情
     *
     * @param context
     * @return
     */
    private void addBiaoqing(Context context, EditText editText, int drawableId, String biaoqingKey) {
        SpannableString spannableString = new SpannableString(biaoqingKey);
        //获取Drawable资源
        Drawable drawable = context.getResources().getDrawable(drawableId);
        //这句话必须，不然图片不显示
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //创建ImageSpan
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //用ImageSpan替换文本
        spannableString.setSpan(span, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Editable e = editText.getText();
        int st = editText.getSelectionStart();
        int en = editText.getSelectionEnd();
        e.replace(st, en, spannableString);
    }

    /**
     * 删除表情
     */
    public void removeBiaoqing(EditText editText) {
        int selectionStart = editText.getSelectionStart();// 获取光标的位置
        if (selectionStart > 0) {
            String body = editText.getText().toString();
            if (!TextUtils.isEmpty(body)) {
                String tempStr = body.substring(0, selectionStart);
                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                if (i != -1) {
                    String cs = tempStr.substring(i, selectionStart);
                    if (cs.indexOf("[ee_")!=-1) {// 判断是否是一个表情
                        editText.getEditableText().delete(i, selectionStart);
                        return;
                    }
                }
                editText.getEditableText().delete(tempStr.length() - 1, selectionStart);
            }
        }
    }

    /**
     * 供外部调用的添加表情的方法
     *
     * @param context
     * @param editText
     * @param viewId
     */
    public void addBiaoqing(Context context, EditText editText, int viewId) {
        String ee = null;
        //取出map集合中的键和值

        switch (viewId) {
            case R.id.ee_del:
                BiaoqingUtil.getInstance().removeBiaoqing(editText);
                break;
            case R.id.ee_1:
                ee = ee_1;
                break;
            case R.id.ee_2:
                ee = ee_2;
                break;
            case R.id.ee_3:
                ee = ee_3;
                break;
            case R.id.ee_4:
                ee = ee_4;
                break;
            case R.id.ee_5:
                ee = ee_5;
                break;
            case R.id.ee_6:
                ee = ee_6;
                break;
            case R.id.ee_7:
                ee = ee_7;
                break;
            case R.id.ee_8:
                ee = ee_8;
                break;
            case R.id.ee_9:
                ee = ee_9;
                break;
            case R.id.ee_10:
                ee = ee_10;
                break;
            case R.id.ee_11:
                ee = ee_11;
                break;
            case R.id.ee_12:
                ee = ee_12;
                break;
            case R.id.ee_13:
                ee = ee_13;
                break;
            case R.id.ee_14:
                ee = ee_14;
                break;
            case R.id.ee_15:
                ee = ee_15;
                break;
            case R.id.ee_16:
                ee = ee_16;
                break;
            case R.id.ee_17:
                ee = ee_17;
                break;
            case R.id.ee_18:
                ee = ee_18;
                break;
            case R.id.ee_19:
                ee = ee_19;
                break;
            case R.id.ee_20:
                ee = ee_20;
                break;
            case R.id.ee_21:
                ee = ee_21;
                break;
            case R.id.ee_22:
                ee = ee_22;
                break;
            case R.id.ee_23:
                ee = ee_23;
                break;
            case R.id.ee_24:
                ee = ee_24;
                break;
            case R.id.ee_25:
                ee = ee_25;
                break;
            case R.id.ee_26:
                ee = ee_26;
                break;
            case R.id.ee_27:
                ee = ee_27;
                break;
            case R.id.ee_28:
                ee = ee_28;
                break;
            case R.id.ee_29:
                ee = ee_29;
                break;
            case R.id.ee_30:
                ee = ee_30;
                break;
            case R.id.ee_31:
                ee = ee_31;
                break;
            case R.id.ee_32:
                ee = ee_32;
                break;
            case R.id.ee_33:
                ee = ee_33;
                break;
            case R.id.ee_34:
                ee = ee_34;
                break;
            case R.id.ee_35:
                ee = ee_35;
                break;
        }
        if (!TextUtils.isEmpty(ee)) {
            for (Map.Entry<Pattern, Object> entry : biaoqingMap.entrySet()) {
                Matcher matcher = entry.getKey().matcher(ee);
                if (matcher.find()) {
                    addBiaoqing(context, editText, (Integer) entry.getValue(), ee);
                }
            }
        }
    }

    /**
     * 用表情代替现有spannable
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    private void addSmiles(Context context, Spannable spannable) {
        //取出map集合中的键和值
        for (Map.Entry<Pattern, Object> entry : biaoqingMap.entrySet()) {
            //解析正则表达式
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                /**将工厂类的Spannable确定为imageSpan类型，并且截取span以正则表达式“[xxx]”为规定*/
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class)) {
                    /**如果在规定的正则表达式“[xxx]”中，从matcher.start开始到结束属于这个正则表达式则说明该字符串是一个表情*/
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end()) {
                        //如果是表情，则从这个sannable中删除这段字符串
                        spannable.removeSpan(span);
                    } else {
                        set = false;
                    }
                }
                //如果spannbale中包含表情，则替换表情
                if (set) {
                    Object value = entry.getValue();
                    //判断value是不是来自本地sd卡并且不是来自网络
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        //如果文件路径不是文件夹并且存在则替换表情
                        if (file.exists() && !file.isDirectory()) {
                            spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                    matcher.start(), matcher.end(),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
    }
}
