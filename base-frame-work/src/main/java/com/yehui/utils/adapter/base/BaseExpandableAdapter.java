package com.yehui.utils.adapter.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by yehuijifeng
 * on 2015/12/30.
 * 多级列表的适配器
 */
public abstract class BaseExpandableAdapter<VH extends BaseExpandableViewHolder> extends BaseExpandableListAdapter {

    /**
     * 每一行父viewholder的view
     *
     * @param parent
     * @return
     */
    public abstract VH groupViewHolder(View parent, int groupPosition, boolean isExpanded);

    /**
     * 父itemview的视图
     */
    public abstract int groupViewByLayout();

    /**
     * 父view每一行的数据
     */
    public abstract void groupItemData(BaseExpandableViewHolder baseViewHolder, int groupPosition, boolean isExpanded);

    /**
     * 每一行子viewholder的view
     *
     * @return
     */
    public abstract VH childViewHolder(View parent, int groupPosition, int childPosition);

    /**
     * 子itemview的视图
     */
    public abstract int childViewByLayout();

    /**
     * 子view每一行的数据
     */
    public abstract void childItemData(BaseExpandableViewHolder baseViewHolder, int groupPosition, int childPosition);

    private Activity activity;
    private List<?> groupArray;
    private List<List<?>> childArray;

    public BaseExpandableAdapter(Activity activity, List<?> groupArray, List<List<?>> childArray) {
        this.activity = activity;
        this.groupArray = groupArray;
        this.childArray = childArray;
    }

    /**
     * @param list 删除全部数据
     */
    public void clearGroupAll(List<?> list) {
        list.clear();
        notifyDataSetChanged();
    }
    public void clearChildAll(List<?> list) {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 清除某条数据
     *
     * @param list
     * @param position
     */
    public void clearOne(List<?> list, int position) {
        if (list.size() >= position) {
            list.remove(position);
            notifyDataSetChanged();
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
    public <T> void addGroupOne(List<T> list, T object, int position) {
        list.add(position, object);
        notifyDataSetChanged();
    }
    public <T> void addCilndOne(List<List<T>> list, List<T> listObj, int position) {
        list.add(position, listObj);
        notifyDataSetChanged();
    }

    /**
     * 子数据
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
    }

    /**
     * 子id
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 根据父下标获得其父view下子数据的个数
     *
     * @param groupPosition
     * @return
     */
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    /**
     * 子view
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View v;
        BaseExpandableViewHolder baseViewHolder;
        if (convertView == null) {
            // 加载行布局文件，产生具体的一行
            v = activity.getLayoutInflater().inflate(childViewByLayout(), null);
            // 创建存储一行控件的对象
            baseViewHolder =  childViewHolder(v, groupPosition, childPosition);
            // 将该行的控件全部存储到vh中
            v.setTag(baseViewHolder);// 将vh存储到行的Tag中
        } else {
            v = convertView;
            // 取出隐藏在行中的Tag--取出隐藏在这一行中的vh控件缓存对象
            baseViewHolder = (BaseExpandableViewHolder) convertView.getTag();
        }
        // 从ViewHolder缓存的控件中改变控件的值
        childItemData(baseViewHolder, groupPosition, childPosition);
        return v;
    }

    /**
     * 根据父下标获得其实例
     *
     * @param groupPosition
     * @return
     */
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    /**
     * 获得父view数据的长度
     *
     * @return
     */
    public int getGroupCount() {
        return groupArray.size();
    }

    /**
     * 根据父下标获得其位置
     *
     * @param groupPosition
     * @return
     */
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 父view
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View v;
        BaseExpandableViewHolder baseViewHolder;

        if (convertView == null) {
            // 加载行布局文件，产生具体的一行
            v = activity.getLayoutInflater().inflate(groupViewByLayout(), null);
            // 创建存储一行控件的对象
            baseViewHolder = groupViewHolder(v, groupPosition, isExpanded);
            v.setTag(baseViewHolder);// 将vh存储到行的Tag中
        } else {
            v = convertView;
            // 取出隐藏在行中的Tag--取出隐藏在这一行中的vh控件缓存对象
            baseViewHolder = (BaseExpandableViewHolder) convertView.getTag();
        }
        // 从ViewHolder缓存的控件中改变控件的值
        groupItemData(baseViewHolder, groupPosition, isExpanded);

        return v;
    }


    /**
     * 是否有稳定的id，默认false
     *
     * @return
     */
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 是否是子view的选择项
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        try {
            if (childArray.get(groupPosition).get(childPosition) == null) return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
