package com.yehui.utils.activity.base;

import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yehui.utils.R;
import com.yehui.utils.adapter.base.BaseAdapter;
import com.yehui.utils.adapter.base.BaseViewHolder;
import com.yehui.utils.utils.DisplayUtil;
import com.yehui.utils.view.loadingview.MyLoadingView;
import com.yehui.utils.view.recyclerview.FootView;
import com.yehui.utils.view.recyclerview.HeaderView;
import com.yehui.utils.view.recyclerview.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by yehuijifeng
 * on 2015/11/25.
 * listview的activity
 */
public abstract class BaseListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * 每一行的布局文件id
     *
     * @param resId
     */
    protected abstract int getItemLayoutById(int resId);

    /**
     * 初始化每一行的数据
     *
     * @param holder
     * @param position
     */
    protected abstract void initItemData(BaseViewHolder holder, int position);

    /**
     * 需要放入viewholder
     *
     * @param itemView
     * @param type
     * @return
     */
    protected abstract BaseViewHolder getViewHolder(View itemView, int type);

    /**
     * item的点击事件
     *
     * @param parent
     * @param itemView
     * @param position
     */
    protected abstract void onItemClick(RecyclerView parent, View itemView, int position);

    /**
     * 初始化
     */
    protected MyAdapter mAdapter;
    protected List<Object> data = new ArrayList<>();
    protected MyRecyclerView mRecyclerView;
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected LinearLayoutManager layoutManager;
    private MyLoadingView mLoadingView;
    protected boolean isLoadMore = true, isRefresh = true;


    /**
     * 创建横向的还是纵向的recyclerview
     * 默认,0，纵向；1，横线
     */
    protected int isHorizaontalOrVertical() {
        return 0;
    }

    /**
     * 设置每个item的间距,
     * 紧紧针对listview可用！(其他代理类有自己的方法)
     */
    private int itemDecoration=1;

    protected int getItemDecoration() {
        return itemDecoration;
    }

    protected void setItemDecoration(int itemDecoration) {
        this.itemDecoration = DisplayUtil.dip2px(this,itemDecoration);
    }

    @Override
    protected void initView() {
        //实例化recyclerview
        mRecyclerView = (MyRecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView = mRecyclerView.recyclerView;
        mLoadingView = mRecyclerView.loadingLayout;
        // 创建一个线性布局管理器
        layoutManager = new LinearLayoutManager(this);
        // 默认是Vertical
        layoutManager.setOrientation(isHorizaontalOrVertical() == 0 ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //实例化adapter
        mAdapter = new MyAdapter(data);
        //添加适配器
        recyclerView.setAdapter(mAdapter);
        //添加item的间距
        recyclerView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(this, getItemDecoration())));
        setIsRefresh(false);
        setIsLoadMore(true);
        if (mRecyclerView.footView != null) mRecyclerView.footView.onFootViewEmpty();
    }

    /**
     * 加载失败的点击事件
     */
    class loadingFailBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadingView(null);
            reLoad();
        }
    }


    /**
     * 加载失败，提示语
     *
     * @param failTextStr 文本提示
     * @param fialBtnStr  按钮上的文字
     */
    @Override
    protected void loadingFail(String failTextStr, String fialBtnStr) {
        mLoadingView.loadingFail(failTextStr, fialBtnStr);
        mLoadingView.loadingFailOnClick(new loadingFailBtnClick());
    }


    /**
     * 下拉刷新监听事件
     */
    private void defaultRefresh() {
            mRecyclerView.setRefreshListener(new HeaderView.RefreshListener() {
                @Override
                public void onRefreshPrepare(boolean bl, PtrFrameLayout frame) {
                    //准备刷新
                }

                @Override
                public void onRefreshBegin(boolean bl, PtrFrameLayout frame) {
                    //刷新中
                    refresh();
                }

                @Override
                public void onRefreshComplete(boolean bl, PtrFrameLayout frame) {
                    //刷新完成
                }
            });
    }

    /**
     * 默认刷新，当松手的时候会调用此方法(官方下拉刷新控件才用到此方法)
     */
    @Override
    public void onRefresh() {
        if (isRefresh()) {
            refresh();
        }
    }

    /**
     * 默认加载更多的方法
     */
    private void defaultLoadMore() {
            mRecyclerView.setLoadMoreListener(new FootView.LoadMoreListener() {
                @Override
                public void onLoadMorePrepare(boolean bl) {
                    //准备加载
                }

                @Override
                public void onLoadMoreBegin(boolean bl) {
                    //正在加载
                    loadMore();
                }

                @Override
                public void onLoadMoreComplete(boolean bl) {
                    //加载完成
                }
            });
    }

    /**
     * 重新加载,子类需要重写
     */
    protected void reLoad() {
    }

    /**
     * 下拉刷新,子类需要重写
     */
    protected void refresh() {
    }

    /**
     * 上拉加载,子类需要重写
     */
    protected void loadMore() {

    }

    /**
     * 是否下拉刷新
     */
    private boolean isRefresh() {
        return isRefresh;
    }

    /**
     * 是否上拉加载更多
     */
    private boolean isLoadMore() {
        return isLoadMore;
    }

    /**
     * 子类可以调用该方法，动态的改变上拉加载的存在方式
     * isLoadMore 是否可以上拉加载
     */
    protected void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore=isLoadMore;
        mRecyclerView.setIsLoadMore(isLoadMore);
        defaultLoadMore();
        if (isLoadMore()) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mRecyclerView.footView.setLayoutParams(layoutParams);
            //添加尾部视图
            mAdapter.setLoadMoreView(mRecyclerView.footView);
        } else {
            mAdapter.removeLoadMoreView();
        }
     }

    /**
     * 子类可以调用该方法，动态的改变下拉刷新的存在方式
     * isRefresh 是否可以下拉刷新
     */
    protected void setIsRefresh(boolean isRefresh) {
        this.isRefresh=isRefresh;
        mRecyclerView.setIsRefresh(isRefresh);
        defaultRefresh();
    }

    /**
     * 加载更多成功
     */
    protected void loadMoreSuccess() {
        if (isLoadMore()) {
            mRecyclerView.closeLoadMoreView();
        }
        loadingClose();
    }

    /**
     * 加载更多失败
     *
     * @param failTextStr 失败的提示语
     * @param fialBtnStr  按钮上的文字
     */
    protected void loadMoreFail(String failTextStr, String fialBtnStr) {
        if (isLoadMore()) {
            mRecyclerView.closeLoadMoreView();
        }
        loadingFail(failTextStr, fialBtnStr);
    }

    /**
     * 加载更多失败
     */
    protected void loadMoreFail() {
        if (isLoadMore()) {
            mRecyclerView.closeLoadMoreView();
        }
        loadingFail();
    }

    /**
     * 加载了更多数据，不再有加载更多的状态
     */
    protected void loadMoreAll() {
        if (isLoadMore()) {
            mRecyclerView.closeLoadMoreView();
        }
        loadingFail();
    }

    /**
     * 刷新成功
     */
    protected void refreshSuccess() {
        if (isRefresh()) {
            mRecyclerView.closeRefreshView();
        }
        loadingClose();
    }

    /**
     * 刷新失败
     */
    protected void refreshFail() {
        if (isRefresh()) {
            mRecyclerView.closeRefreshView();
        }
        loadingFail();
    }

    /**
     * 刷新失败
     *
     * @param failTextStr 提示语
     * @param fialBtnStr  按钮上的文字
     */
    protected void refreshFail(String failTextStr, String fialBtnStr) {
        if (isRefresh()) {
            mRecyclerView.closeRefreshView();
        }
        loadingFail(failTextStr, fialBtnStr);
    }

    /**
     * 给recyclerview加横线
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            //设置左右的间隔如果想设置的话自行设置，我这用不到就注释掉了
            //outRect.left = space;
            //outRect.right = space;
            //outRect.bottom = space;

            //改成使用上面的间隔来设置
            //if (parent.getChildAdapterPosition(view) != 0)
            outRect.bottom = space;
        }
    }

    /**
     * 刷新数据
     */
    protected void notifyDataChange() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param data
     */
    protected void addAll(List<?> data) {
        if (data == null) return;
        this.data.addAll(data);
    }

    /**
     * 删除数据
     */
    protected void clearAll() {
        if (data == null) return;
        mAdapter.clearAll(data);
    }

    /**
     * 添加一条数据数据
     */
    protected void addOne(Object obj, int position) {
        if (data == null) return;
        mAdapter.addOne(data, obj, position);
    }

    /**
     * 删除某条数据
     */
    protected void clearOne(int position) {
        if (data == null) return;
        mAdapter.clearOne(data, position);
    }

    /**
     * 获取数据长度
     *
     * @return
     */
    protected int getCount() {
        return data.size();
    }

    /**
     * 默认适配器
     */
    public class MyAdapter extends BaseAdapter<Object> {

        public MyAdapter(List<Object> data) {
            super(data);
        }

        /**
         * 每一行item的view都会同时进入getViewHolder和onCreateViewHolderItem
         * getviewholder代表着头或者尾部
         */
        @Override
        public BaseViewHolder getViewHolder(View view) {
            return BaseListActivity.this.getViewHolder(view, -1);
        }

        /**
         * 每一行item数据的初始化都会在该方法回调
         *
         * @param holder   强转成继承了baseviewholder的viewholder
         * @param position item的标识
         */
        @Override
        public void onBindDataForItem(BaseViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new onClickListener(position));
            initItemData(holder, position);
        }

        /**
         * 创建item的viewholder
         * itemview（可以用它实例化item中的控件）
         * viewType用于判断当前item的类型从而new的hivewholder
         */
        @Override
        public BaseViewHolder onCreateViewHolderItem(ViewGroup parent, int viewType) {
            View itemView = inflate(getItemLayoutById(viewType), parent, false);
            return BaseListActivity.this.getViewHolder(itemView, viewType);
        }

        /**
         * item的tyoe取决于getviewholder方法中的第二个参数
         */
        @Override
        public int getItemType(int position) {
            return BaseListActivity.this.getItemType(position);
        }
    }

    /**
     * 获得当前item的类型
     *
     * @param position
     * @return
     */
    protected int getItemType(int position) {
        return position;
    }


    /**
     * item的点击事件
     */
    class onClickListener implements View.OnClickListener {

        private int position;

        public onClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            onItemClick(mRecyclerView.recyclerView, v, position);
        }
    }

}
