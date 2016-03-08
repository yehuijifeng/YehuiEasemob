//package com.yehui.utils.fragment;
//
//import android.view.View;
//import android.widget.Button;
//
//import com.yehui.utils.R;
//import com.yehui.utils.activity.StaggeredActivity;
//import com.yehui.utils.fragment.base.BaseFragment;
//
///**
// * Created by yehuijifeng
// * on 2015/12/3.
// * 测试fragment
// */
//public class ViewPagerOne extends BaseFragment {
//    private Button button_text;
//
//    @Override
//    protected int setFragmentLayoutId() {
//        return R.layout.layout_test_viewpager_item;
//    }
//
//    @Override
//    protected void initView(View parentView) {
//        button_text = (Button) parentView.findViewById(R.id.button_text);
//        button_text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(StaggeredActivity.class);
//            }
//        });
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//}
