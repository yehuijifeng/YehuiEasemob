package com.yehui.utils.activity.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.yehui.utils.utils.DisplayUtil;

/**
 * Created by yehuijifeng
 * on 2015/12/29.
 * 瀑布流布局的代理类
 */
public abstract class BaseStaggeredActivity extends BaseListActivity {

    /**
     * 每一行瀑布流视图的个数，子类需要实现
     */
    protected abstract int stagViewByNumber();

    /**
     * 瀑布流的管理器
     */
    protected StaggeredGridLayoutManager layoutManager;

    /**
     * 瀑布流的边框线
     */
    protected SpaceItemDecoration spaceItemDecoration;

    @Override
    protected void initView() {
        super.initView();
        /**
         * 重写baselistview中的加横线方法，为了去掉父类中的横线
         */
        setItemDecoration(0);
        layoutManager = new StaggeredGridLayoutManager(stagViewByNumber(), stagViewOrientation()==0?StaggeredGridLayoutManager.VERTICAL:StaggeredGridLayoutManager.HORIZONTAL);
        spaceItemDecoration = new SpaceItemDecoration(decorationSize());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(spaceItemDecoration);
        mRecyclerView.onRcvScrollListener.stagNumber=stagViewByNumber();
    }

    /**
     * 瀑布流的方向
     * 0,默认，垂直
     * 1,水平
     */
    protected int stagViewOrientation(){
        return 0;
    }

    /**
     * gridview四条边的线，子类可以重写
     * 紧紧针对stagview可用！
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
                        this.left = DisplayUtil.dip2px(BaseStaggeredActivity.this, space[0]);
                        break;
                    case 1:
                        this.top = DisplayUtil.dip2px(BaseStaggeredActivity.this, space[1]);
                        break;
                    case 2:
                        this.right = DisplayUtil.dip2px(BaseStaggeredActivity.this, space[2]);
                        break;
                    case 3:
                        this.bottom = DisplayUtil.dip2px(BaseStaggeredActivity.this, space[3]);
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
