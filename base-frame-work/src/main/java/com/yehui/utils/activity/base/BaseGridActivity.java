package com.yehui.utils.activity.base;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yehui.utils.utils.DisplayUtil;

/**
 * Created by yehuijifeng
 * on 2015/12/28.
 * gridview的处理类
 */
public abstract class BaseGridActivity extends BaseListActivity {

    /**
     * 每一行gridview的视图个数，子类需要实现
     */
    protected abstract int gridViewByNumber();

    /**
     * 表格布局的管理器
     */
    protected GridLayoutManager layoutManager;

    /**
     * 表格布局的边框线
     */
    protected SpaceItemDecoration spaceItemDecoration;

    @Override
    protected void initView() {
        super.initView();
        /**
         * 重写baselistview中的加横线方法，为了去掉父类中的横线
         */
        setItemDecoration(0);
        layoutManager = new GridLayoutManager(this, gridViewByNumber());
        spaceItemDecoration = new SpaceItemDecoration(decorationSize());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(spaceItemDecoration);
    }

    /**
     * gridview四条边的线，子类可以重写
     * 紧紧针对gridview可用！
     */
    protected float[] decorationSize() {
        return new float[]{0, 0, 0, 0};
    }

    /**
     * 给recyclerview加横线
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int left, top, right, bottom;

        public SpaceItemDecoration(float[] space) {
            for (int i = 0; i < space.length; i++) {

                switch (i) {
                    default:
                    case 0:
                        this.left = DisplayUtil.dip2px(BaseGridActivity.this, space[0]);
                        break;
                    case 1:
                        this.top = DisplayUtil.dip2px(BaseGridActivity.this, space[1]);
                        break;
                    case 2:
                        this.right = DisplayUtil.dip2px(BaseGridActivity.this, space[2]);
                        break;
                    case 3:
                        this.bottom = DisplayUtil.dip2px(BaseGridActivity.this, space[3]);
                        break;
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //改成使用上面的间隔来设置
                outRect.top = top;
                outRect.left = left;
                outRect.right = right;
                outRect.bottom = bottom;
        }
    }
}
