package com.yehui.utils.view.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yehui.utils.R;
import com.yehui.utils.utils.EmptyUtil;

/**
 * Created by yehuijifeng
 * on 2015/12/30.
 * 信息提示层
 */
public class MyLoadingView extends LinearLayout {

    private LayoutInflater inflater;
    private View root;
    private ImageView loading_img;
    private ProgressBar loading_bar;
    private TextView loading_text, loading_empty_text, loading_empty_click_text;
    private Button loading_btn;

    public MyLoadingView(Context context) {
        super(context);
        initView(context);
    }

    public MyLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        root = inflater.inflate(R.layout.layout_default_loading, this);
        loading_img = (ImageView) root.findViewById(R.id.loading_img);
        loading_bar = (ProgressBar) root.findViewById(R.id.loading_bar);
        loading_text = (TextView) root.findViewById(R.id.loading_text);
        loading_btn = (Button) root.findViewById(R.id.loading_btn);
        loading_empty_click_text = (TextView) root.findViewById(R.id.loading_empty_click_text);
        loading_empty_text = (TextView) root.findViewById(R.id.loading_empty_text);
        root.setVisibility(GONE);
    }

    /**
     * 隐藏遮罩层
     */
    public void loadingGONE() {
        root.setVisibility(GONE);
    }

    /**
     * 显示遮罩层
     */
    public void loadingVISIBLE() {
        root.setVisibility(VISIBLE);
        loading_img.setVisibility(GONE);
        loading_empty_click_text.setVisibility(GONE);
        loading_empty_text.setVisibility(GONE);
        loading_btn.setVisibility(GONE);
    }

    /**
     * 加载失败的点击事件处理
     *
     * @param l 点击事件
     */
    public void loadingFailOnClick(OnClickListener l) {
        loading_btn.setOnClickListener(l);
    }
    /**
     * 加载空视图的点击事件处理
     *
     * @param l 点击事件
     */
    public void loadingEmptyOnClick(OnClickListener l) {
        root.setOnClickListener(l);
    }


    /**
     * 加载失败，提示语
     */
    public void loadingFail() {
        loadingFail("", "");
    }
    public void loadingFail(String textStr) {
        loadingFail(textStr, "");
    }
    /**
     * 加载失败，提示语
     *
     * @param failTextStr 文本提示
     * @param fialBtnStr  按钮上的文字
     */
    public void loadingFail(String failTextStr, String fialBtnStr) {
        root.setVisibility(VISIBLE);
        loading_empty_text.setVisibility(GONE);
        loading_img.setVisibility(GONE);
        loading_bar.setVisibility(GONE);
        loading_text.setVisibility(VISIBLE);
        loading_btn.setVisibility(VISIBLE);
        loading_empty_click_text.setVisibility(GONE);
        if (EmptyUtil.isStringEmpty(failTextStr))
            loading_text.setText(getResources().getString(R.string.lading_fail));
        else
            loading_text.setText(failTextStr + "");

        if (!EmptyUtil.isStringEmpty(fialBtnStr))
            loading_btn.setText(fialBtnStr + "");
        else
            loading_btn.setText(getResources().getString(R.string.loading_refresh));
    }


    /**
     * 加载出空数据的时候
     */
    public void loadingEmpty() {
        loadingEmpty("", "");
    }

    /**
     * 加载出空数据的时候
     *
     * @param emptyStr 空数据提示语
     */
    public void loadingEmpty(String emptyStr, String fialBtnStr) {
        root.setVisibility(VISIBLE);
        loading_empty_text.setVisibility(VISIBLE);
        loading_img.setVisibility(VISIBLE);
        loading_bar.setVisibility(GONE);
        loading_text.setVisibility(GONE);
        loading_btn.setVisibility(GONE);
        loading_empty_click_text.setVisibility(VISIBLE);
        if (!EmptyUtil.isStringEmpty(emptyStr))
            loading_empty_text.setText(emptyStr + "");
        else
            loading_empty_text.setText(getResources().getString(R.string.lading_empty));

        if (!EmptyUtil.isStringEmpty(fialBtnStr))
            loading_empty_click_text.setText(fialBtnStr + "");
        else 
            loading_empty_click_text.setText(getResources().getString(R.string.click_refresh));

    }

    /**
     * 正在加载中
     */
    public void loadingView() {
        loadingView("");
    }

    /**
     * 正在加载中
     *
     * @param loadingStr 正在加载中提示语
     */
    public void loadingView(String loadingStr) {
        root.setVisibility(VISIBLE);
        loading_empty_text.setVisibility(GONE);
        loading_img.setVisibility(GONE);
        loading_bar.setVisibility(VISIBLE);
        loading_text.setVisibility(VISIBLE);
        loading_btn.setVisibility(GONE);
        loading_empty_click_text.setVisibility(GONE);
        if (!EmptyUtil.isStringEmpty(loadingStr))
            loading_text.setText(loadingStr + "");
        else
            loading_text.setText(getResources().getString(R.string.header_hint_loading));
    }
}
