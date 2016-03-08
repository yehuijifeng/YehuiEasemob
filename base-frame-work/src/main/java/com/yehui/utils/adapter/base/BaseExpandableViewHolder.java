package com.yehui.utils.adapter.base;

import android.view.View;

/**
 * Created by yehuijifeng
 * on 2016/1/4.
 * listview类型的viewholder
 */
public abstract class BaseExpandableViewHolder {
    public View itemView;

    public BaseExpandableViewHolder(View itemView) {
        this.itemView = itemView;
        initItemView(itemView);
    }

    public abstract void initItemView(View itemView);
}
