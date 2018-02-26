package com.xytsz.xytaj.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by admin on 2017/1/4.
 *
 */
public class NoScrollViewpager extends ViewPager {
    public NoScrollViewpager(Context context) {
        super(context);
    }

    public NoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //内部需要滑动  把事件传递给了下面
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
    //消费当前的事件不让view pager 可以滑动
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
