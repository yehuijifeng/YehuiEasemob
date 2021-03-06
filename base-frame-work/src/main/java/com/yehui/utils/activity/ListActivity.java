//package com.yehui.utils.activity;
//
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import com.yehui.utils.R;
//import com.yehui.utils.activity.base.BaseListActivity;
//import com.yehui.utils.adapter.base.BaseViewHolder;
//import com.yehui.utils.utils.LogUtil;
//import com.yehui.utils.view.titleview.MyTitleView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yehuijifeng
// * on 2015/12/9.
// * recycler代替listview
// */
//public class ListActivity extends BaseListActivity {
//
//    /**
//     * 自定义toolbar的优先级高于toolbarmodel
//     */
////    @Override
////    protected View setCustomToolbar() {
////        return inflate(R.layout.item_test_recycler, null);
////    }
//
//    @Override
//    protected int isHorizaontalOrVertical() {
//        return 0;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        setTitleMode(MyTitleView.TitleMode.OPTIONS);
//        mTitleView.setImageButtonOnClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showShortToast("编辑图片");
//            }
//        });
//        mAdapter.setHeaderView(inflate(R.layout.item_demo_cart, null));
//        setItemDecoration(2);
//        //loadingView();
//        //handler.sendEmptyMessageDelayed(1, 2000);
//    }
//
//
//    @Override
//    protected void initData() {
//        for (int i = 0; i < 15; i++) {
//            addOne("确定" + i, i);
//        }
//        /**
//         * 是否下拉刷新
//         */
//        setIsRefresh(true);
//
//        /**
//         * 是否上拉加载
//         */
//        setIsLoadMore(true);
//
//    }
//
//    @Override
//    protected int getItemLayoutById(int type) {
//        if (type == 2015) {
//            return R.layout.item_test_recycler;
//        }
//        return R.layout.item_test_recycler;
//    }
//
//    /**
//     * 测试
//     */
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    clearAll();
//                    List<String> list2 = new ArrayList<>();
//                    for (int i = 0; i < 20; i++) {
//                        list2.add("加载完成" + i);
//                    }
//                    addAll(list2);
//                    LogUtil.i("加载完成");
//                    notifyDataChange();
//                    loadMoreSuccess();
//                    mAdapter.removeHeaderView();
//                    break;
//                case 1:
//                    LogUtil.i("刷新成功");
//                    clearAll();
//                    List<String> list1 = new ArrayList<>();
//                    for (int i = 0; i < 25; i++) {
//                        list1.add("刷新成功" + i);
//                    }
//                    addAll(list1);
//                    notifyDataChange();
//                    refreshFail("app被外星人带走了", "我要夺回来，我要夺回来，我要夺回来，我要夺回来！");
//                default:
//                    break;
//            }
//
//        }
//    };
//
//    /**
//     * 重新加载
//     */
//    @Override
//    protected void reLoad() {
//        handler.sendEmptyMessageDelayed(0, 2000);
//    }
//
//    /**
//     * 下拉刷新
//     */
//    @Override
//    protected void refresh() {
//        handler.sendEmptyMessageDelayed(1, 2000);
//    }
//
//    /**
//     * 加载更多
//     */
//    @Override
//    protected void loadMore() {
//        handler.sendEmptyMessageDelayed(0, 2000);
//    }
//
//
//    @Override
//    protected void initItemData(BaseViewHolder holder, int position) {
//        if (holder instanceof DefaultTowViewHolder) {
//            DefaultTowViewHolder defaultTowViewHolder = (DefaultTowViewHolder) holder;
//            defaultTowViewHolder.textViewTest.setText("第" + position + "行,这是另外一个viewholder");
//        } else if (holder instanceof DefaultViewHolder) {
//            DefaultViewHolder defaultViewHolder = (DefaultViewHolder) holder;
//            defaultViewHolder.textViewTest.setText("第" + position + "行");
//            if (position == 0) defaultViewHolder.buttonTest.setText(data.get(position) + "特定");
//            else
//                defaultViewHolder.buttonTest.setText(data.get(position) + "");
//        }
//    }
//
//    @Override
//    protected int getItemType(int position) {
//        if (position == 1) {
//            return 2015;
//        }
//        return super.getItemType(position);
//    }
//
//    @Override
//    protected BaseViewHolder getViewHolder(View itemView, int type) {
//        if (type == 2015) {
//            return new DefaultTowViewHolder(itemView);
//        }
//        return new DefaultViewHolder(itemView);
//    }
//
//    @Override
//    protected void onItemClick(RecyclerView parent, View itemView, int position) {
//        if (position == 2) {
//            addOne("新添加的数据", 3);
//        }
//        showShortToast("点击了第" + position + "行");
//    }
//
//    @Override
//    protected void setContentView() {
//        setContentView(R.layout.activity_test_recycler_view);
//    }
//
//
//    @Override
//    protected String setTitleText() {
//        return "recyclerList";
//    }
//
//
//    /**
//     * 如果需要多个适配器，则在具体实现类中创建多个
//     */
//    class DefaultViewHolder extends BaseViewHolder {
//
//        private CheckBox radioButtonTest;
//        private TextView textViewTest;
//        private Button buttonTest;
//
//        public DefaultViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void initItemView(View itemView) {
//            radioButtonTest = (CheckBox) itemView.findViewById(R.id.radioButtonTest);
//            textViewTest = (TextView) itemView.findViewById(R.id.textViewTest);
//            buttonTest = (Button) itemView.findViewById(R.id.buttonTest);
//        }
//    }
//
//    class DefaultTowViewHolder extends BaseViewHolder {
//
//        private CheckBox radioButtonTest;
//        private TextView textViewTest;
//        private Button buttonTest;
//
//        public DefaultTowViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void initItemView(View itemView) {
//            radioButtonTest = (CheckBox) itemView.findViewById(R.id.radioButtonTest);
//            textViewTest = (TextView) itemView.findViewById(R.id.textViewTest);
//            buttonTest = (Button) itemView.findViewById(R.id.buttonTest);
//        }
//    }
//}
