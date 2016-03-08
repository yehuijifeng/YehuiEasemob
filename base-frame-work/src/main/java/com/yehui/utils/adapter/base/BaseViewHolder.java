package com.yehui.utils.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yehuijifeng
 * on 2015/12/8.
 * recyclerview的主要viewholder类
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        initItemView(itemView);
    }

    public abstract void initItemView(View itemView);
}
