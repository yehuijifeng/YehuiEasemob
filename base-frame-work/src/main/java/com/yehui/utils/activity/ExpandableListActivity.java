//package com.yehui.utils.activity;
//
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.TextView;
//
//import com.yehui.utils.R;
//import com.yehui.utils.activity.base.BaseExpandableListViewActivity;
//import com.yehui.utils.adapter.base.BaseExpandableViewHolder;
//import com.yehui.utils.view.titleview.MyTitleView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by yehuijifeng
// * on 2015/12/30.
// * 两级列表
// */
//public class ExpandableListActivity extends BaseExpandableListViewActivity {
//
//
//    @Override
//    protected void initView() {
//        super.initView();
//
//        setTitleMode(MyTitleView.TitleMode.NORMAL);
//
//        List<String> list = new ArrayList<>();
//        List<List<String>> lists = new ArrayList<>();
//
//        for (int i = 0; i < 20; i++) {
//            list.add("确定" + i);
//
//        }
//        for (int i = 0; i < groupData.size(); i++) {
//            lists.add(list);
//        }
//        addGroupAll(list);
//        for (int i = 0; i < groupData.size(); i++) {
//            addChildAll(i, list);
//        }
//        showShortToast(getClindCount(0) + "");
//
//    }
//
//
//    /**
//     * 旋转图片
//     *
//     * @param image
//     * @param degrees
//     */
//    private void rotateImage(ImageView image, int degrees) {
//        if (degrees == 0) {
//            image.setImageDrawable(getResourceDrawable(R.drawable.ic_drop_right));
//            return;
//        }
//
//        Bitmap mBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degrees);
//        image.setImageMatrix(matrix);
//        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
//                mBitmap.getHeight(), matrix, true);
//        image.setImageBitmap(mBitmap);
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//    @Override
//    protected BaseExpandableViewHolder getGroupViewHolder(View parent, int groupPosition, boolean isExpanded) {
//        return new DefaultViewHolder(parent);
//    }
//
//    @Override
//    protected BaseExpandableViewHolder getChildViewHolder(View parent, int groupPosition, int childPosition) {
//        return new DefaultTowViewHolder(parent);
//    }
//
//    @Override
//    public int groupViewByLayout() {
//        return R.layout.item_test_expandable;
//    }
//
//    @Override
//    public int childViewByLayout() {
//        return R.layout.item_test_expandable_tow;
//    }
//
//    private Map<Integer, Boolean> radioGroupMap = new HashMap<>();
//
//    @Override
//    public void groupItemData(BaseExpandableViewHolder baseViewHolder, final int groupPosition, boolean isExpanded) {
//        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) baseViewHolder;
//
//        defaultViewHolder.radioButtonTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                radioGroupMap.put(groupPosition, isChecked);
//            }
//        });
//
//        if (radioGroupMap.get(groupPosition) != null) {
//            defaultViewHolder.radioButtonTest.setChecked(radioGroupMap.get(groupPosition));
//        } else {
//            defaultViewHolder.radioButtonTest.setChecked(false);
//        }
//        if (isExpanded)
//            rotateImage(defaultViewHolder.groupImage, 90);
//        else
//            rotateImage(defaultViewHolder.groupImage, 0);
//        defaultViewHolder.textViewTest.setText("第" + groupPosition + "行");
//        defaultViewHolder.buttonTest.setText(groupData.get(groupPosition) + "");
//    }
//
//    @Override
//    public void onGroupItemClick(ExpandableListView expandableListView, View itemView, int groupPosition, long id) {
//        showShortToast("父目录第" + groupPosition + "行");
//    }
//
//    @Override
//    public void childItemData(BaseExpandableViewHolder baseViewHolder, int groupPosition, int childPosition) {
//        DefaultTowViewHolder defaultViewHolder = (DefaultTowViewHolder) baseViewHolder;
//        defaultViewHolder.textViewTest.setText("第" + childPosition + "行");
//        defaultViewHolder.buttonTest.setText(childData.get(groupPosition).get(childPosition) + "");
//    }
//
//    @Override
//    public void onChildItemClick(ExpandableListView expandableListView, View itemView, int groupPosition, int childPosition, long id) {
//        showShortToast("子目录第" + childPosition + "行");
//    }
//
//    @Override
//    protected void setContentView() {
//        setContentView(R.layout.activity_test_expandable_list_view);
//    }
//
//    @Override
//    protected String setTitleText() {
//        return "两级列表";
//    }
//
//    class DefaultViewHolder extends BaseExpandableViewHolder {
//
//        private CheckBox radioButtonTest;
//        private TextView textViewTest;
//        private Button buttonTest;
//        private ImageView groupImage;
//
//        public DefaultViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void initItemView(View itemView) {
//            groupImage = (ImageView) itemView.findViewById(R.id.group_image);
//            radioButtonTest = (CheckBox) itemView.findViewById(R.id.radioButtonTest);
//            textViewTest = (TextView) itemView.findViewById(R.id.textViewTest);
//            buttonTest = (Button) itemView.findViewById(R.id.buttonTest);
//        }
//    }
//
//    class DefaultTowViewHolder extends BaseExpandableViewHolder {
//
//        private RadioButton radioButtonTest;
//        private TextView textViewTest;
//        private Button buttonTest;
//        private ImageView groupImage;
//
//        public DefaultTowViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        @Override
//        public void initItemView(View itemView) {
//            groupImage = (ImageView) itemView.findViewById(R.id.group_image);
//            radioButtonTest = (RadioButton) itemView.findViewById(R.id.radioButtonTest);
//            textViewTest = (TextView) itemView.findViewById(R.id.textViewTest);
//            buttonTest = (Button) itemView.findViewById(R.id.buttonTest);
//        }
//    }
//}
