package com.yehui.utils.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.yehui.utils.R;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by yehuijifeng
 * on 2015/12/30.
 * 多级列表处理类
 */
public class MyExpandableView extends LinearLayout {

    private LayoutInflater inflater;
    private View root;
    public ExpandableListView expandableListView;
    public PtrFrameLayout ptrFrameLayout;//下拉刷新控件
    public HeaderView headerView; //头文件的父布局


    private boolean isRefresh;//是否可以下拉刷新

    public MyExpandableView(Context context) {
        super(context);
        initview(context);
    }

    public MyExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }

    public MyExpandableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initview(context);
    }

    private boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        if (isRefresh) {
            if (ptrFrameLayout == null)
                ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.default_refresh_view);
            ptrFrameLayout.setHeaderView(headerView);
            ptrFrameLayout.addPtrUIHandler(headerView);
        } else {
            ptrFrameLayout = null;
        }
        this.isRefresh = isRefresh;
    }

    public HeaderView.RefreshListener getRefreshListener() {
        return this.headerView.getRefreshListener();
    }

    public void setRefreshListener(HeaderView.RefreshListener refreshListener) {
        this.headerView.setRefreshListener(refreshListener);
    }

    private void initview(Context context) {
        inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.layout_default_expandable_list, this);
        expandableListView = (ExpandableListView) root.findViewById(R.id.default_expandable_list);
        ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.default_refresh_view);
        headerView = new HeaderView(context);
        /**
         * 注意！
         * 该回调方法必须写！
         * 如果不写则下拉刷新不正常！
         * ptrFrameLayout.refreshComplete();为刷新完成还原视图的方法可以在实现了该回调后自行处理
         */
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                //ptrFrameLayout.refreshComplete();//刷新完成
                //ptrFrameLayout.autoRefresh();//自动刷新
            }
        });
    }

    /**
     * 刷新完成成功后的回调
     */
    public void closeRefreshView() {
        if (isRefresh()) {
            if (ptrFrameLayout != null) {
                ptrFrameLayout.refreshComplete();
            }
        }
    }

}
