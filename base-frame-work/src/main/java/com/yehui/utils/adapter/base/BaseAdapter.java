package com.yehui.utils.adapter.base;

import android.view.View;

import java.util.List;


/**
 * Created by yehuijifeng
 * on 2015/12/8.
 * recyclerview的主要适配器
 */
public abstract class BaseAdapter<T> extends UltimateViewAdapter<BaseViewHolder> {

    protected List<T> data;

    public BaseAdapter(List<T> data) {
        this.data = data;
    }

    /**
     * 绑定每一行的数据
     *
     * @param holder
     * @param position
     */
    public abstract void onBindDataForItem(BaseViewHolder holder, int position);

    /**
     * 绑定viewholder
     *
     * @param holder   ，viewholder
     * @param position 标识
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        // && !holder.itemView.equals(customHeaderView)
        //判断绑定的该viewholder是否属于尾部view，如果是，则不绑定
        if (!holder.itemView.equals(customLoadMoreView)&& !holder.itemView.equals(customHeaderView)) {
            if (customHeaderView != null) position--;
            onBindDataForItem(holder, position);
        } else if (holder.itemView.equals(customLoadMoreView) && data.size() == 0) {
            if (customHeaderView != null) position--;
            holder.itemView.setVisibility(View.GONE);
        }else if(holder.itemView.equals(customHeaderView) && data.size() == 0){
            holder.itemView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取适配器item的数量，抽象类来自于Ultimateviewadapter
     */
    @Override
    public int getAdapterItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ((position == getItemCount() - 1 && customLoadMoreView != null) || (position == 0 && customHeaderView != null)) {
            return super.getItemViewType(position);
        }
        return getItemType(position);
    }


    /**
     * 每一行item的类型
     *
     * @param position，如果返回1001，1002，1003则表示头，尾，尾
     */
    protected int getItemType(int position) {
        return position;
    }


    /**
     * @param list 删除全部数据
     */
    public void clearAll(List<?> list) {
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);//范围删除
    }

    /**
     * 清除某条数据
     *
     * @param list
     * @param position
     */
    public void clearOne(List<?> list, int position) {
        if (list.size() > 0) {
            list.remove(customHeaderView != null ? position - 1 : position);
            notifyItemRemoved(position);//精确删除
        }
    }

    /**
     * 添加某条数据
     *
     * @param list     添加的list
     * @param object   添加的数据
     * @param position 添加的位置
     * @param <T>
     */
    public <T> void addOne(List<T> list, T object, int position) {
        list.add(position, object);
        if (customHeaderView != null) position++;
        notifyItemInserted(position);//精确添加
    }
}
