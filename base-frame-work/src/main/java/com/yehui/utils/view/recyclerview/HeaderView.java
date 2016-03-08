package com.yehui.utils.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.utils.DateUtil;

import java.text.ParseException;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;

/**
 * Created by yehuijifeng
 * on 2015/12/26.
 * 下拉刷新自定义的头
 */
public class HeaderView extends LinearLayout implements PtrUIHandler {

    private LayoutInflater inflater;

    // 下拉刷新视图（头部视图）
    private View headView;
    // 下拉刷新文字
    private TextView custom_header_hint_text, custom_header_time;
    // 下拉图标
    private ImageView custom_header_image;
    //正在加载的状态
    private ProgressBar custom_header_bar;

    private String past_time, current_time;
    private HeaderView.RefreshListener refreshListener;

    public HeaderView.RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(HeaderView.RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public HeaderView(Context context) {
        super(context);
        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化视图
     */
    private void initView(Context context) {
        inflater = LayoutInflater.from(context);//父容器
        past_time = context.getResources().getString(R.string.past_time);
        /**
         * 头部
         */
        headView = inflater.inflate(R.layout.layout_default_header, this);
        custom_header_hint_text = (TextView) headView.findViewById(R.id.custom_header_hint_text);
        custom_header_time = (TextView) headView.findViewById(R.id.custom_header_time);
        custom_header_image = (ImageView) headView.findViewById(R.id.custom_header_image);
        custom_header_bar = (ProgressBar) headView.findViewById(R.id.custom_header_bar);
        custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
    }

    /**
     * 下拉刷新监听接口
     */
    public interface RefreshListener {
        void onRefreshPrepare(boolean bl, PtrFrameLayout frame);

        void onRefreshBegin(boolean bl, PtrFrameLayout frame);

        void onRefreshComplete(boolean bl, PtrFrameLayout frame);
    }

    /**
     * 重置ui
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
        custom_header_hint_text.setText("下拉刷新");
        custom_header_bar.setVisibility(GONE);
        custom_header_image.setVisibility(VISIBLE);
        custom_header_image.setRotation(0);//图片旋转
    }

    /**
     * 准备刷新
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        custom_header_hint_text.setText("下拉刷新");
        custom_header_bar.setVisibility(GONE);
        custom_header_image.setVisibility(VISIBLE);
        custom_header_image.setRotation(0);//图片旋转
        getRefreshListener().onRefreshPrepare(true, frame);
        try {
            if(current_time!=null)
            custom_header_time.setText(DateUtil.getTimeReduction(current_time));
            else
                custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
        } catch (ParseException e) {
            e.printStackTrace();
            custom_header_time.setText(DateUtil.getNow(DateUtil.getDatePattern()));
        }
    }

    /**
     * 开始刷新
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        custom_header_hint_text.setText("正在刷新... ...");
        custom_header_bar.setVisibility(VISIBLE);
        custom_header_image.setVisibility(GONE);
        getRefreshListener().onRefreshBegin(true, frame);
    }

    /**
     * 完成刷新
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        custom_header_hint_text.setText("刷新完成");
        custom_header_bar.setVisibility(GONE);
        custom_header_image.setVisibility(VISIBLE);
        getRefreshListener().onRefreshComplete(true, frame);
        current_time= DateUtil.getNow(DateUtil.getDatePattern());

    }

    /**
     * 位置改变
     *
     * @param frame
     * @param isUnderTouch    是否在触摸
     * @param status          状态码：
     *                        PTR_STATUS_INIT = 1; 初始化
     *                        PTR_STATUS_PREPARE = 2; 准备
     *                        PTR_STATUS_LOADING = 3; 加载中
     *                        PTR_STATUS_COMPLETE = 4; 加载完成
     * @param oldPosition     原来的位置
     * @param currentPosition 当前位置
     * @param oldPercent      原来的百分比
     * @param currentPercent  当前的百分比
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, int oldPosition, int currentPosition, float oldPercent, float currentPercent) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();

        /**如果视图的达到下拉刷新高度大于当前位置，并且小于或等于原来的视图的高度则为下拉刷新未达到状态*/
        if (currentPosition > mOffsetToRefresh && oldPosition <= mOffsetToRefresh) {
//            LogUtil.i("当前位置："+currentPosition);
//            LogUtil.i("原来的位置："+oldPosition);
//            LogUtil.i("控件高度："+mOffsetToRefresh);
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                custom_header_hint_text.setText("松开刷新");
                custom_header_bar.setVisibility(GONE);
                custom_header_image.setVisibility(VISIBLE);
                custom_header_image.setRotation(180);//图片旋转
            }
        } else if (currentPosition < mOffsetToRefresh && oldPosition > mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                custom_header_hint_text.setText("下拉刷新");
                custom_header_bar.setVisibility(GONE);
                custom_header_image.setVisibility(VISIBLE);
                custom_header_image.setRotation(0);//图片旋转
            }
        }
    }
}
