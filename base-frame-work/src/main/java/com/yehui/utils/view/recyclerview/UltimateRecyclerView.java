package com.yehui.utils.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yehui.utils.R;
import com.yehui.utils.view.loadingview.MyLoadingView;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by yehuijifeng
 * on 2015/12/12.
 * 最终的recyclerview
 */
public class UltimateRecyclerView extends LinearLayout {

    /**
     * SwipeRefreshLayout里面需要注意的Api：
     * 1、setOnRefreshListener(OnRefreshListener listener)  设置下拉监听，当用户下拉的时候会去执行回调
     * 2、setColorSchemeColors(int... colors) 设置 进度条的颜色变化，最多可以设置4种颜色
     * 3、setProgressViewOffset(boolean scale, int start, int end) 调整进度条距离屏幕顶部的距离
     * 4、setRefreshing(boolean refreshing) 设置SwipeRefreshLayout当前是否处于刷新状态，一般是在请求数据的时候设置为true，在数据被加载到View中后，设置为false。
     */

    public RecyclerView recyclerView;//代替listview和graidview
    public PtrFrameLayout ptrFrameLayout;//下拉刷新控件
    private Context context;//实现了该布局的上下文
    public View root;//父布局
    private LayoutInflater inflater;//父容器
    public HeaderView headerView; //头文件的父布局
    public FootView footView;//为布局
    public MyLoadingView loadingLayout;//listview的遮罩层
    public OnRcvScrollListener onRcvScrollListener;
    private boolean isRefresh;//是否可以下拉刷新

    private boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh()) {
            headerView.setVisibility(VISIBLE);
            ptrFrameLayout.addPtrUIHandler(headerView);
        } else {
            headerView.setVisibility(GONE);
            ptrFrameLayout.removePtrUIHandler(headerView);
        }

    }

    private boolean isLoadMore;//是否可以上拉加载

    private boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        onRcvScrollListener.setIsShowFoot(isLoadMore());
    }

    public HeaderView.RefreshListener getRefreshListener() {
        return this.headerView.getRefreshListener();
    }

    public void setRefreshListener(HeaderView.RefreshListener refreshListener) {
        this.headerView.setRefreshListener(refreshListener);
    }

    public FootView.LoadMoreListener getLoadMoreListener() {
        return this.footView.getLoadMoreListener();
    }

    public void setLoadMoreListener(FootView.LoadMoreListener loadMoreListener) {
        this.footView.setLoadMoreListener(loadMoreListener);
    }

    public UltimateRecyclerView(Context context) {
        super(context);
        initialize(context);
    }

    public UltimateRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public UltimateRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    /**
     * 初始化数据
     *
     * @param context
     */
    private void initialize(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.activity_default_recycler_view, this);
        loadingLayout = (MyLoadingView) root.findViewById(R.id.my_loading_layout);
        ptrFrameLayout = (PtrFrameLayout) root.findViewById(R.id.default_refresh_view);
        recyclerView = (RecyclerView) root.findViewById(R.id.default_recycler_view);
        footView = new FootView(context);
        onRcvScrollListener = new OnRcvScrollListener(context, recyclerView, footView);
        recyclerView.addOnScrollListener(onRcvScrollListener);
        onRcvScrollListener.setIsShowFoot(isLoadMore());
        headerView = new HeaderView(context);
        ptrFrameLayout.setHeaderView(headerView);
        ptrFrameLayout.addPtrUIHandler(headerView);
        /* 下拉时阻止事件分发 */
        ptrFrameLayout.setInterceptEventWhileWorking(true);
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
        ptrFrameLayout.setPullToRefresh(false);
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

    /**
     * 加载完全部数据，不再下拉加载
     */
    public void closeLoadMoreView() {
        if (isLoadMore()) {
            if (footView != null)
                footView.onFootViewAll();
            onRcvScrollListener.setIsShowFoot(false);
        }
    }

    /**
     * 加载更多后隐藏视图
     */
    public void onFootViewEmpty() {
        if (isLoadMore()) {
            if (footView != null)
                footView.onFootViewEmpty();
            onRcvScrollListener.setIsShowFoot(true);
        }
    }

}