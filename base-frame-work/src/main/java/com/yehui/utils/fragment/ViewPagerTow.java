//package com.yehui.utils.fragment;
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
//import com.yehui.utils.adapter.base.BaseViewHolder;
//import com.yehui.utils.fragment.base.BaseListFragment;
//import com.yehui.utils.utils.LogUtil;
//import com.yehui.utils.view.titleview.MyTitleView;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yehuijifeng
// * on 2015/12/3.
// * 测试fragment
// */
//public class ViewPagerTow extends BaseListFragment {
//
//
//    @Override
//    protected int getItemLayoutResId(int type) {
//        return R.layout.item_test_recycler;
//    }
//
//    @Override
//    protected void initItemData(BaseViewHolder holder, int position) {
//        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) holder;
//        defaultViewHolder.textViewTest.setText("第" + position + "行");
//        defaultViewHolder.buttonTest.setText(data.get(position) + "");
//    }
//
//    @Override
//    protected BaseViewHolder getViewHolder(View itemView, int type) {
//        return new DefaultViewHolder(itemView);
//    }
//
//    @Override
//    protected void onItemClick(RecyclerView parent, View itemView, int position) {
//        showShortToast("点击了第" + position + "行");
//    }
//
//    @Override
//    protected int setFragmentLayoutId() {
//        return R.layout.activity_test_recycler_view;
//    }
//
//    private MyTitleView my_title_view;
//
//    @Override
//    protected void initView(View parentView) {
//        super.initView(parentView);
//        my_title_view = (MyTitleView) parentView.findViewById(R.id.my_title_view);
//        my_title_view.setVisibility(View.GONE);
//
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        for (int i = 0; i < 15; i++) {
//            addOne("确定" + i, i);
//        }
////        loadingView();
////        handler.sendEmptyMessageDelayed(1, 2000);
//    }
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
//}
