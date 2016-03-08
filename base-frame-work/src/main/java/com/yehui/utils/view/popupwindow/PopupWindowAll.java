package com.yehui.utils.view.popupwindow;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.yehui.utils.R;

/**
 * Created by Yehuijifeng
 * on 2015/10/29.
 * popupwindow的方法归类
 */
public class PopupWindowAll extends View {

    private PopupWindow popupWindow;

    public PopupWindowAll(Context context) {
        super(context);
    }

    /**
     * 是否让焦点落在popupwindow上，而父view无焦点
     */
    public boolean setFocusable() {
        return false;
    }

    /**
     * 是否使popupwindow以外的视图被点击
     */
    public boolean setOutsideTouchable() {
        return true;
    }

    /**
     * popupwindow初始化设置
     */
    private void initSettings() {
        /**
         * 如果pop不设置背景的话不会在点击视图以外消失
         */
        popupWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.color.transparent));

        /**
         * 设置此项为true时，会让其这个popupWindow处于焦点，其它控件(除主页面键)都会
         * 失去焦点，不可点击,仅仅适用于全屏幕遮挡的popupwindow
         */
        popupWindow.setFocusable(setFocusable());
        /**popupwindow以外可以触摸*/
        popupWindow.setOutsideTouchable(setOutsideTouchable());
    }

    /**
     * 关闭popupwindow
     */
    public void dismissWindow(){
        if(popupWindow!=null)
            popupWindow.dismiss();
    }
    /**
     * 全屏幕显示
     */
    public void showFullWindow(View root) {
        popupWindow = new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        initSettings();
        popupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 显示在指定控件的下面
     *
     * @param root
     * @param parent
     */
    public void showAtLocationByBottom(View root, View parent) {

        popupWindow = new PopupWindow(parent, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initSettings();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        int rootWidth = root.getWidth();
        int rootHeight = root.getHeight();
        int[] location = new int[2];
        // 获得位置
        root.getLocationOnScreen(location);
        popupWindow.setAnimationStyle(R.anim.activity_start_anim);
        popupWindow.showAtLocation(root,Gravity.NO_GRAVITY, (location[0] + rootWidth / 2) - parentWidth / 2, location[1] + rootHeight);
    }


    /**
     * api19以后才可使用该方法
     * 根据手指的点击而出现
     *
     * @param anchor  落下的视图
     * @param xoff
     * @param yoff
     * @param gravity 权重
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View root, View anchor, int xoff, int yoff, int gravity) {
        popupWindow = new PopupWindow(root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initSettings();
        popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View root, View anchor, int xoff, int yoff) {
        showAsDropDown(root, anchor, xoff, yoff, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showAsDropDown(View root, View anchor) {
        showAsDropDown(root, anchor, 0, 0, 0);
    }

}
