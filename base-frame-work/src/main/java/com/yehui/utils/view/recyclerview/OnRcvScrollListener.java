package com.yehui.utils.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;


/**
 * Created by yehuijifeng
 * on 2015/12/15.
 * recyclerview的滑动监听事件
 */
public class OnRcvScrollListener extends RecyclerView.OnScrollListener {


    private boolean isShowFoot, isLast;//是否进行上拉加载状态
    private RecyclerView.LayoutManager layoutManager;//recyclerview的管理器
    private RecyclerView recyclerView;//代替listview和graidview
    private FootView footView;
    private Context context;
    public int stagNumber=0;

    public boolean isShowFoot() {
        return isShowFoot;
    }

    public void setIsShowFoot(boolean isShowFoot) {
        this.isShowFoot = isShowFoot;
    }


    public OnRcvScrollListener(Context context, RecyclerView recyclerView, FootView footView) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.footView = footView;
    }

    /**
     * 是否滑动到底部
     */
    private boolean isChildScrollToBottom() {
        layoutManager = recyclerView.getLayoutManager();
        int count = recyclerView.getAdapter().getItemCount();
        if (layoutManager instanceof LinearLayoutManager && count > 0) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastItems = new int[stagNumber];
            staggeredGridLayoutManager
                    .findLastCompletelyVisibleItemPositions(lastItems);
            int lastItem = Math.max(lastItems[0], lastItems[1]);
            if (lastItem == count - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 监听下拉事件
     *
     * @param recyclerView 刷新的view
     * @param newState     刷新的状态码，0，停止下拉；1，拖动的时候；2，固定，回弹的时候
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView,
                                     int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //LogUtil.i("下拉中");
            if (isShowFoot()) {
                if (isLast) {
                    if (isChildScrollToBottom()) {
                        footView.onFootViewBegin();
                    } else {
                        footView.onFootPrepare();
                    }
                }
            }
        } else if (newState == 1) {
            //LogUtil.i("滑动中");
            if (isShowFoot()) {
                if (isLast) {
                    if (!isChildScrollToBottom()) {
                        footView.onFootPrepare();
                    }
                }
            }
        } else if (newState == 2) {
            //LogUtil.i("上弹中");
        }
    }

    /**
     * 下拉事件
     *
     * @param recyclerView
     * @param dx
     * @param dy
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dx > 0) {
            //正在向右滚动
            isLast = true;
        } else if (dy > 0) {
            //正在向下滚动
            //LogUtil.i("向右：" + dx + "向下：" + dy);
            isLast = true;
        } else if (dy < 0) {
            isLast = false;
        } else if (dx < 0) {
            isLast = false;
        }
    }

}
