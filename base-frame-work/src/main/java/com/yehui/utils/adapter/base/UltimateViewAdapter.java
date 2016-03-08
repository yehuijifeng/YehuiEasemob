package com.yehui.utils.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yehuijifeng
 * on 2015/12/8.
 * recyclerview的最终适配器
 */
public abstract class UltimateViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /**
     * 查询添加的viewholder是不是属于特定类型
     * 如果想要返回特定类型的itemtype，则需要参照这些值
     */
    public class VIEW_TYPES {
        public static final int NORMAL = 1000;//正常的
        public static final int HEADER = 1001;//头
        public static final int FOOTER = 1002;//页脚
        public static final int CHANGED_FOOTER = 1003;//修改页脚
    }

    /**
     * 泛型，传进来viewholder需要用到的view,也属于item，但是只用于头和尾
     *
     * @param view
     * @return
     */
    public abstract VH getViewHolder(View view);

    /**
     * 每一行viewholder的view
     *
     * @param parent
     * @param viewType
     * @return
     */
    public abstract VH onCreateViewHolderItem(ViewGroup parent, int viewType);

    /**
     * 获得适配器中数据的总行数
     */
    public abstract int getAdapterItemCount();

    protected View customLoadMoreView = null;//自定义加载更多
    protected View customHeaderView = null;//自定义头视图
    public boolean isLoadMoreChanged = false;//加载更多的view的状态

    /**
     * 添加加载更多的方法
     *
     * @param loadView
     */
    public void setLoadMoreView(View loadView) {
        customLoadMoreView = loadView;
    }

    /**
     * 删除尾部视图
     */
    public void removeLoadMoreView() {
        customLoadMoreView = null;
    }

    /**
     * 添加头的方法
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        customHeaderView = headerView;
    }

    /**
     * 删除头视图
     */
    public void removeHeaderView() {
        customHeaderView = null;
    }
    public View getLoadMoreView(){
        return customLoadMoreView;
    }
    public View getHeaderView(){
        return  customHeaderView;
    }
    /**
     * viewholder每一行的视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPES.HEADER) {//头
            VH viewHolder = getViewHolder(customHeaderView);
            if (customHeaderView != null)
                return viewHolder;
        } else if (viewType == VIEW_TYPES.FOOTER) {//尾
            VH viewHolder = getViewHolder(customLoadMoreView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        } else if (viewType == VIEW_TYPES.CHANGED_FOOTER) {//尾
            VH viewHolder = getViewHolder(customLoadMoreView);
            if (getAdapterItemCount() == 0)
                viewHolder.itemView.setVisibility(View.GONE);
            return viewHolder;
        }
        //正常的
        return onCreateViewHolderItem(parent, viewType);
    }

    /**
     * @return 适配器中如果有头和尾，则加入总数中
     */
    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (customHeaderView != null) itemCount++;
        if (customLoadMoreView != null) itemCount++;
        return itemCount + getAdapterItemCount();
    }

    /**
     * 获取item的类型
     * @param position
     */
    @Override
    public int getItemViewType(int position) {
        //是否是加载更多的view，并判断加载更多view的状态
        if (position == getItemCount() - 1 && customLoadMoreView != null) {
            if (isLoadMoreChanged) {
                return VIEW_TYPES.CHANGED_FOOTER;
            } else {
                return VIEW_TYPES.FOOTER;
            }
        }
        //是否是头视图
        else if (position == 0 && customHeaderView != null) {
            return VIEW_TYPES.HEADER;
        }
        //如果不是头view和尾view则属于正常view
        else
            return VIEW_TYPES.NORMAL;
    }
}
