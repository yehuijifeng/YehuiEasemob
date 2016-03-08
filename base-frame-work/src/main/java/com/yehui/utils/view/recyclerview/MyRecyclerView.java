package com.yehui.utils.view.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by yehuijifeng
 * on 2015/12/9.
 * 默认list的布局
 */

/**
 * RecyclerView.LayoutManager，这是一个抽象类，系统提供了3个实现类：
 LinearLayoutManager 现行管理器，支持横向、纵向。
 GridLayoutManager 网格布局管理器
 StaggeredGridLayoutManager 瀑布就式布局管理器
 */
public class MyRecyclerView extends UltimateRecyclerView {

    public MyRecyclerView(Context context) {
        super(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){

        //设置Item增加、移除动画
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.getItemAnimator().setAddDuration(1000);
//        recyclerView.getItemAnimator().setRemoveDuration(1000);
//        recyclerView.getItemAnimator().setMoveDuration(1000);
//        recyclerView.getItemAnimator().setChangeDuration(1000);

    }

    /**
     * 默认的下拉刷新
     *
     * @param swipeRefreshLayout
     */
//    public final void defaultRefresh(SwipeRefreshLayout swipeRefreshLayout) {
//        swipeRefreshLayout.setColorSchemeResources(
//                R.color.default_load_more_color_one,
//                R.color.default_load_more_color_tow,
//                R.color.default_load_more_color_three,
//                R.color.default_load_more_color_four);
//    }

}
