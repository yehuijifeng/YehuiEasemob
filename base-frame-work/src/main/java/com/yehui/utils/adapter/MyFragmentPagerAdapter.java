package com.yehui.utils.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yehuijifeng
 * on 2015/12/3.
 * viewpager的适配器
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mViewList;
    protected List<Class<? extends Fragment>> fragmentList;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> mViewList) {
        super(fm);
        this.mViewList = mViewList;
    }


    /**
     * 当前item的fragment实例
     *
     * @param position item的下标
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return mViewList.get(position);
    }

    /**
     * fragment实例的数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return mViewList.size();
    }

    /**
     * 当前item的位置
     *
     * @param object fragment的实例
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    /**
     * 销毁某个item,传入position即可
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 返回实例化的某个view，取出list中的，传入position即可
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * 返回当前view的集合
     *
     * @return
     */
    public List<Fragment> getViewList() {
        return mViewList;
    }

}

