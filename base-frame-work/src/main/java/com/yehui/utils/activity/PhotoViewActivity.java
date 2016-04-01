package com.yehui.utils.activity;

import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.activity.base.BaseActivity;
import com.yehui.utils.application.YehuiApplication;
import com.yehui.utils.contacts.SettingContact;
import com.yehui.utils.view.photoView.PhotoView;
import com.yehui.utils.view.viewpager.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 查看图片
 */
public class PhotoViewActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener {

    private List<String> imgUrls = new ArrayList<>();
    private MyViewPager viewPager;
    private TextView mLabel;
    private int position;
    private PicAdapter picAdapter;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_look_image);
    }

    @Override
    protected String setTitleText() {
        return "图片";
    }

    @Override
    protected void initView() {
        imgUrls = getStringArrayList(SettingContact.PHOTO_VIEW_LIST);
        position = 0;
        mLabel = (TextView) findViewById(R.id.mLabel);
        viewPager = (MyViewPager) findViewById(R.id.viewPager);
        picAdapter = new PicAdapter();
        viewPager.setAdapter(picAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem(position);
        mLabel.setText((position + 1) + "/" + imgUrls.size());
    }

    @Override
    protected void initData() {

    }

    class PicAdapter extends PagerAdapter {

        private List<PhotoView> photoViews = new ArrayList<>();

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        /**
         * 销毁当前page的相隔2个及2个以上的item时调用
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /**
             * 删除页卡，如果不删除则会让图片叠加
             */
            container.removeView((View) object); // 删除页卡

        }

        // 这个方法用来实例化页卡
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoViewActivity.this.position = position;
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 设置手指点击photoview的时候，finish掉当前视图，变为滑动viewpager
                     */
                    finish();
                }
            });
            String imgUrl = imgUrls.get(position);
            /**
             * 如果图片路径的开头是http，则用imageloader去网上获取
             * 如果不是，则交给photo去处理
             * */
            if ("http".equalsIgnoreCase(imgUrl.substring(0, 4))) {
                imageLoader.displayImage(imgUrl, photoView, YehuiApplication.defaultOptions);
            } else {
                photoView.setImageBitmap(BitmapFactory.decodeFile(imgUrl));
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            photoViews.add(photoView);
            return photoView; // 返回该view对象，作为key
        }

        @Override
        public int getCount() {
            return imgUrls.size();
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mLabel.setText((position + 1) + "/" + imgUrls.size());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
