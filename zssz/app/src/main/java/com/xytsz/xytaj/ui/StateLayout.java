package com.xytsz.xytaj.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xytsz.xytaj.R;

/**
 * Created by admin on 2017/7/11.
 *
 *
 */
public class StateLayout extends FrameLayout {

    private View loadingView;
    private View failView;
    private View contentView;

    public StateLayout(Context context) {
        super(context);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public static StateLayout newInstance(Context context,Object viewOrLayoutId) {
        StateLayout stateLayout = (StateLayout) View.inflate(context, R.layout.state_layout, null);
        stateLayout.setContentView(viewOrLayoutId);
        return stateLayout;
    }

    public void setContentView(Object viewOrLayoutId) {
        if (viewOrLayoutId == null) {
            throw new IllegalArgumentException("viewOrLayoutId参数不能为空，必须设置，可以是一个View，也可以是一个布局id");
        }

        if (viewOrLayoutId instanceof Integer) {	// 如果viewOrLayoutId参数是一个Integer对象
            int layoutId = (Integer) viewOrLayoutId;
            contentView = View.inflate(getContext(), layoutId, null);
        } else {	// 如果viewOrLayoutId参数是一个View
            contentView = (View) viewOrLayoutId;
        }

        contentView.setVisibility(View.GONE);	// 默认显示的是LoadingView，所以这里先隐藏
        addView(contentView);
    }

    /** 显示“正在加载”的View */
    public void showLoadingView() {
        showView(loadingView);
    }

    /** 显示“加载失败”的View */
    public void showFailView() {
        showView(failView);
    }

    /** 显示“正常界面”的View */
    public void showContentView() {
        showView(contentView);
    }

    private void showView(View view) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(child == view ? View.VISIBLE:View.GONE);

        }
    }
}
