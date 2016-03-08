package com.yehui.utils.activity.base;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yehui.utils.R;
import com.yehui.utils.adapter.MyFragmentPagerAdapter;
import com.yehui.utils.utils.DisplayUtil;
import com.yehui.utils.view.viewpager.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yehuijifeng
 * on 2015/11/25.
 * viewpager滑动页
 */
public abstract class BaseViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    /**
     * 默认viewpager
     */
    private MyViewPager mViewPager;
    private LinearLayout mViewpagerTab;

    /**
     * 存放view的集合
     */
    protected List<Fragment> mViewList;
    protected List<View> tabViewList;
    protected int itemSize;

    /**
     * viewpager的适配器
     */
    private MyFragmentPagerAdapter fragmentPagerAdapter;

    /**
     * 默认的tab
     */
    protected LinearLayout defaultItemTabView;
    private ImageView defaultItemImageView;

    private View itemView;//当前显示的view

    /**
     * 进度条移动值
     **/
    private Integer moveI;
    private LinearLayout.LayoutParams currLayoutParams;

    @Override
    protected void initView() {
        mViewPager = (MyViewPager) findViewById(R.id.my_viewpager_view);
        mViewpagerTab = (LinearLayout) findViewById(R.id.my_viewpager_view_tab);
        defaultItemTabView = (LinearLayout) mViewpagerTab.findViewById(R.id.viewpager_btn_layout);
        defaultItemImageView = (ImageView) mViewpagerTab.findViewById(R.id.viewpager_image_bar);
        if (isShowBar())
            currLayoutParams = (LinearLayout.LayoutParams) defaultItemImageView.getLayoutParams();//初始化游标的params
        mViewPager.addOnPageChangeListener(this);
        mViewList = new ArrayList<>();
        tabViewList = new ArrayList<>();
    }

    @Override
    protected void initData() {
        itemSize = mViewList.size();
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mViewList);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setCurrentItem(getPageNumber());
        initTabView();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getWindowWidth() / mViewList.size(), DisplayUtil.dip2px(this,3));
        tabViewList.get(getPageNumber()).setSelected(true);
        if (isShowBar()) {
            defaultItemImageView.setLayoutParams(layoutParams);
            currLayoutParams = (LinearLayout.LayoutParams) defaultItemImageView.getLayoutParams();
            tabMove(getPageNumber() == 0 ? 0 : getPageNumber() - 1, 0);
        }

    }

    /**
     * 初始化tab
     */
    private void initTabView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getWindowWidth() / mViewList.size(), RelativeLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < mViewList.size(); i++) {
            tabViewList.add(setTabView(defaultItemTabView, i));
            tabViewList.get(i).setLayoutParams(layoutParams);
            defaultItemTabView.addView(tabViewList.get(i));
            tabViewList.get(i).setOnClickListener(new ItemTabOnClick(i));
            tabViewList.get(i).setSelected(false);
        }

    }

    /**
     * 每一个tab的点击事件，点击后直接跳转到该tab下的pager页面
     */
    private class ItemTabOnClick implements View.OnClickListener {

        private int item;

        private ItemTabOnClick(int item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {

            //获得当前选项卡的id，传入viepager中
            mViewPager.setCurrentItem(item);
        }
    }

    /**
     * 重写该方法可以自定义viewpager的tab
     *
     * @return
     */
    protected abstract View setTabView(ViewGroup container, int position);

    /**
     * @return 初始化显示的页码
     */
    private int pageNumber;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return 是否显示进度条
     */
    protected boolean isShowBar() {
        return true;
    }

    /**
     * 获得当前显示的view
     *
     * @return
     */
    protected View getItemView() {
        if (itemView != null)
            return itemView;
        return null;
    }

    /**
     *viewpager三个方法最先执行，比initview和initdata都优先
     */

    /**
     * 当页面滑动的时候会调用此方法
     * <p>
     * arg0 :当前页面，及你点击滑动的页面
     * arg1:当前页面偏移的百分比
     * arg2:当前页面偏移的像素位置
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isShowBar())
            tabMove(position, positionOffsetPixels);
        else
            defaultItemImageView.setVisibility(View.GONE);
    }

    /**
     * 进度条移动 :核心的移动算法
     *
     * @param position             当前页位置
     * @param positionOffsetPixels 移动像素值
     */
    private void tabMove(int position, int positionOffsetPixels) {
        moveI = (int) (defaultItemImageView.getWidth() * position + (((double) positionOffsetPixels / getWindowWidth()) * defaultItemImageView.getWidth()));
        currLayoutParams.leftMargin = moveI;
        defaultItemImageView.setLayoutParams(currLayoutParams);
    }

    /**
     * 此方法是页面跳转完后得到调用
     * <p>
     * arg0是你当前选中的页面的Position(位置编号)。
     */
    @Override
    public void onPageSelected(int position) {
        itemView = mViewList.get(position).getView();
        /**
         * 改变tab的选中状态,
         * 注意，这时候的tabViewList还未加入数据，所以tabViewList.size()为0
         */
        for (int i = 0; i < tabViewList.size(); i++) {
            if (i == position)
                tabViewList.get(position).setSelected(true);
            else
                tabViewList.get(i).setSelected(false);
        }

    }

    /**
     * 此方法是在状态改变的时候调用
     * <p>
     * 中arg0这个参数
     * 有三种状态(0，1，2)。state ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
     */
    @Override
    public void onPageScrollStateChanged(int state) {
//        if (state == 0) {
//            showShortToast("什么都没做");
//        } else if (state == 1) {
//            showShortToast("正在滑动");
//        } else if (state == 2) {
//            showShortToast("滑动完毕");
//        }
    }
}
