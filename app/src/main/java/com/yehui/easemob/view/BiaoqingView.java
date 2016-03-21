package com.yehui.easemob.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yehui.easemob.R;
import com.yehui.easemob.adapter.BiaoqingViewPagerAdapter;
import com.yehui.easemob.utils.BiaoqingUtil;
import com.yehui.utils.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luhao on 2016/3/18.
 */
public class BiaoqingView extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {


    private BiaoqingViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private List<View> mViewList;
    private RelativeLayout root;
    private View itemView, bq_view_one, bq_view_tow;
    private LinearLayout biaoqing_bar_ly;
    private ImageView biaoqing_bar_img;
    private ImageView
            ee_1,
            ee_2,
            ee_3,
            ee_4,
            ee_5,
            ee_6,
            ee_7,
            ee_8,
            ee_9,
            ee_10,
            ee_11,
            ee_12,
            ee_13,
            ee_14,
            ee_15,
            ee_16,
            ee_17,
            ee_18,
            ee_19,
            ee_20,
            ee_21,
            ee_22,
            ee_23,
            ee_24,
            ee_25,
            ee_26,
            ee_27,
            ee_28,
            ee_29,
            ee_30,
            ee_31,
            ee_32,
            ee_33,
            ee_34,
            ee_35,
            ee_del_one,
            ee_del_tow;
    private ImageView[] imageViews;

    public BiaoqingView(Context context) {
        super(context);
        initView();
    }

    public BiaoqingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        root = (RelativeLayout) inflater.inflate(R.layout.layout_biaoqing, this);
        mViewPager = (ViewPager) root.findViewById(R.id.biaoqing_viewpager);
        biaoqing_bar_ly = (LinearLayout) root.findViewById(R.id.biaoqing_bar_ly);
        //biaoqing_bar_img = (ImageView) root.findViewById(R.id.biaoqing_bar_img);
        mViewPager.addOnPageChangeListener(this);
        initData(inflater);
    }

    public void initData(LayoutInflater inflater) {
        mViewList = new ArrayList<>();
        bq_view_one = inflater.inflate(R.layout.layout_biaoqing_one, null);
        bq_view_tow = inflater.inflate(R.layout.layout_biaoqing_tow, null);
        mViewList.add(bq_view_one);
        mViewList.add(bq_view_tow);
        mAdapter = new BiaoqingViewPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);
        imageViews = new ImageView[mViewList.size()];
        for (int i = 0; i < mViewList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.bg_biaoqing_bar));
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.bottomMargin = DisplayUtil.dip2px(getContext(), 3f);
            layout.topMargin = DisplayUtil.dip2px(getContext(), 3f);
            layout.leftMargin = DisplayUtil.dip2px(getContext(), 3f);
            layout.rightMargin = DisplayUtil.dip2px(getContext(), 3f);
            imageView.setLayoutParams(layout);
            imageView.setId(i);
            imageViews[i] = imageView;
            biaoqing_bar_ly.addView(imageView);
        }
        imageViews[0].setSelected(true);

        ee_del_one = (ImageView) bq_view_one.findViewById(R.id.ee_del);
        ee_del_tow = (ImageView) bq_view_tow.findViewById(R.id.ee_del);
        ee_1 = (ImageView) bq_view_one.findViewById(R.id.ee_1);
        ee_2 = (ImageView) bq_view_one.findViewById(R.id.ee_2);
        ee_3 = (ImageView) bq_view_one.findViewById(R.id.ee_3);
        ee_4 = (ImageView) bq_view_one.findViewById(R.id.ee_4);
        ee_5 = (ImageView) bq_view_one.findViewById(R.id.ee_5);
        ee_6 = (ImageView) bq_view_one.findViewById(R.id.ee_6);
        ee_7 = (ImageView) bq_view_one.findViewById(R.id.ee_7);
        ee_8 = (ImageView) bq_view_one.findViewById(R.id.ee_8);
        ee_9 = (ImageView) bq_view_one.findViewById(R.id.ee_9);
        ee_10 = (ImageView) bq_view_one.findViewById(R.id.ee_10);
        ee_11 = (ImageView) bq_view_one.findViewById(R.id.ee_11);
        ee_12 = (ImageView) bq_view_one.findViewById(R.id.ee_12);
        ee_13 = (ImageView) bq_view_one.findViewById(R.id.ee_13);
        ee_14 = (ImageView) bq_view_one.findViewById(R.id.ee_14);
        ee_15 = (ImageView) bq_view_one.findViewById(R.id.ee_15);
        ee_16 = (ImageView) bq_view_one.findViewById(R.id.ee_16);
        ee_17 = (ImageView) bq_view_one.findViewById(R.id.ee_17);
        ee_18 = (ImageView) bq_view_one.findViewById(R.id.ee_18);
        ee_19 = (ImageView) bq_view_one.findViewById(R.id.ee_19);
        ee_20 = (ImageView) bq_view_one.findViewById(R.id.ee_20);
        ee_21 = (ImageView) bq_view_one.findViewById(R.id.ee_21);
        ee_22 = (ImageView) bq_view_one.findViewById(R.id.ee_22);
        ee_23 = (ImageView) bq_view_one.findViewById(R.id.ee_23);
        ee_24 = (ImageView) bq_view_one.findViewById(R.id.ee_24);
        ee_25 = (ImageView) bq_view_tow.findViewById(R.id.ee_25);
        ee_26 = (ImageView) bq_view_tow.findViewById(R.id.ee_26);
        ee_27 = (ImageView) bq_view_tow.findViewById(R.id.ee_27);
        ee_28 = (ImageView) bq_view_tow.findViewById(R.id.ee_28);
        ee_29 = (ImageView) bq_view_tow.findViewById(R.id.ee_29);
        ee_30 = (ImageView) bq_view_one.findViewById(R.id.ee_30);
        ee_31 = (ImageView) bq_view_one.findViewById(R.id.ee_31);
        ee_32 = (ImageView) bq_view_one.findViewById(R.id.ee_32);
        ee_33 = (ImageView) bq_view_tow.findViewById(R.id.ee_33);
        ee_34 = (ImageView) bq_view_tow.findViewById(R.id.ee_34);
        ee_35 = (ImageView) bq_view_tow.findViewById(R.id.ee_35);

        ee_del_one.setOnClickListener(this);
        ee_del_tow.setOnClickListener(this);
        ee_1.setOnClickListener(this);
        ee_2.setOnClickListener(this);
        ee_3.setOnClickListener(this);
        ee_4.setOnClickListener(this);
        ee_5.setOnClickListener(this);
        ee_6.setOnClickListener(this);
        ee_7.setOnClickListener(this);
        ee_8.setOnClickListener(this);
        ee_9.setOnClickListener(this);
        ee_10.setOnClickListener(this);
        ee_11.setOnClickListener(this);
        ee_12.setOnClickListener(this);
        ee_13.setOnClickListener(this);
        ee_14.setOnClickListener(this);
        ee_15.setOnClickListener(this);
        ee_16.setOnClickListener(this);
        ee_17.setOnClickListener(this);
        ee_18.setOnClickListener(this);
        ee_19.setOnClickListener(this);
        ee_20.setOnClickListener(this);
        ee_21.setOnClickListener(this);
        ee_22.setOnClickListener(this);
        ee_23.setOnClickListener(this);
        ee_24.setOnClickListener(this);
        ee_25.setOnClickListener(this);
        ee_26.setOnClickListener(this);
        ee_27.setOnClickListener(this);
        ee_28.setOnClickListener(this);
        ee_29.setOnClickListener(this);
        ee_30.setOnClickListener(this);
        ee_31.setOnClickListener(this);
        ee_32.setOnClickListener(this);
        ee_33.setOnClickListener(this);
        ee_34.setOnClickListener(this);
        ee_35.setOnClickListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    @Override
    public void onPageSelected(int position) {
        itemView = mViewList.get(position);
        /**
         * 改变tab的选中状态,
         * 注意，这时候的tabViewList还未加入数据，所以tabViewList.size()为0
         */
        for (int i = 0; i < mViewList.size(); i++) {
            if (i == position)
                imageViews[i].setSelected(true);
            else
                imageViews[i].setSelected(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 外部调用，表情点击事件
     */
    public void onBiaoqingClick(EditText text_msg_edit) {
        this.text_msg_edit = text_msg_edit;
    }

    private EditText text_msg_edit;

    @Override
    public void onClick(View v) {
        if (text_msg_edit != null) {
            BiaoqingUtil.getInstance().addBiaoqing(getContext(), text_msg_edit, v.getId());
        }
    }
}
