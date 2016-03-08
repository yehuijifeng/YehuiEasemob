package com.yehui.utils.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yehuijifeng
 * on 2015/12/3.
 * 基类的viewpager重写
 */
public class MyViewPager extends ViewPager {

    /**
     * 是否可滚动
     */
    private boolean scrollable = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 拦截触摸事件
     * @param arg0 滚动事件
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean b = false;
        try {
            b = scrollable && super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 触摸事件
     * @param arg0 滚动事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        try {
            return scrollable && super.onTouchEvent(arg0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @return 获取viewpager的滚动状态
     */
    public boolean isScrollable() {
        return scrollable;
    }

    /**
     * @return 设置viewpager是否可滚动，默认可滚动
     */
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

}
