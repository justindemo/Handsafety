package com.xytsz.xytaj.ui;


import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;




/**
 * Created by admin on 2017/1/11.
 *
 * 自定义滑动
 */
public class Swipelayout extends FrameLayout {

    private View rightView;
    private View leftView;
    private int leftViewWidth;
    private int leftViewHeight;
    private int rightViewWidth;
    private ViewDragHelper helper;


    public Swipelayout(Context context) {
        this(context,null);
    }

    public Swipelayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Swipelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        helper = ViewDragHelper.create(this, callback);
    }

    private ViewDragHelper.Callback callback =  new ViewDragHelper.Callback() {
            //都抓取到
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == leftView) {
                if (left > 0) {
                    left = 0;
                } else if (left < -rightViewWidth) {
                    left = -rightViewWidth;
                }
            } else {
                if (left < leftViewWidth - rightViewWidth) {
                    left = leftViewWidth - rightViewWidth;
                } else if (left > leftViewWidth) {
                    left = leftViewWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //如果移动left,则让right跟着移动,反之亦然
            if (changedView == leftView) {
                rightView.offsetLeftAndRight(dx);
            } else {
                leftView.offsetLeftAndRight(dx);
            }
            //offsetLeftAndRight对于移动不可见的视图的时候,不能及时刷新
            invalidate();
          //当条目刚刚脱离完全关闭的时候,把那个条目记住
            if (leftView.getLeft() <0) {
                SwipeLayoutManager manager = SwipeLayoutManager.getInstance();
                manager.setSwipeLayout(Swipelayout.this);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if (leftView.getLeft() > -rightViewWidth * 0.1f) {
                close(true);
            } else {
                open();
            }
        }
    };

    private void open() {
        if (helper.smoothSlideViewTo(leftView, -rightViewWidth, 0)) {
            invalidate();
        }
    }


    public void close(boolean isSmooth) {
        if(isSmooth) {
            //只有在彻底关闭的时候清除引用
            //设置了滚动的目标点,开始执行
            if (helper.smoothSlideViewTo(leftView, 0, 0)) {
                invalidate();
                Log.i("test", "smoothSlideViewTo");
            }
        }else{
            //将条目瞬间关闭
            leftView.layout(0, 0, leftViewWidth, leftViewHeight);
            rightView.layout(leftViewWidth, 0, leftViewWidth + rightViewWidth, leftViewWidth);
            SwipeLayoutManager.getInstance().clearSwipeLayout();
        }
    }

        @Override
        public void computeScroll() {
            super.computeScroll();
            Log.i("test", "computeScroll");
            if (helper.continueSettling(true)) {
                invalidate();
            } else {
                if (leftView.getLeft() == 0) {
                    SwipeLayoutManager.getInstance().clearSwipeLayout();
                }

            }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //
                //判断有没有条目被打开,如果有,则关闭那个条目,自身不能被滑动
                //1,记住那个被打开的条目
                //2,判断被打开的条目和当前DOWN的条目是否一致
                //3,如果不一致,关闭被记住的那个打开的条目
                //4,让当前的条目不能滑动
                SwipeLayoutManager manager = SwipeLayoutManager.getInstance();
                //判断是否有条目被打开
                if (manager.getSwipeLayout() != null) {
                    //有条目被打开 判断是否是当前条目
                    if (manager.getSwipeLayout() != Swipelayout.this) {
                        //不是在关闭这个条目
                        manager.getSwipeLayout().close(true);
                        return true;
                    }
                }else {
                    //没有条目被打开: 在前端操作

                }

                break;
            case MotionEvent.ACTION_MOVE:
                manager = SwipeLayoutManager.getInstance();
                if (manager.getSwipeLayout() != null) {
                    if (manager.getSwipeLayout() != Swipelayout.this) {
                        //在有条目被打开的情况下,上下滑动其他条目,请求ListView不要拦截事件
                        requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                }
                break;
        }
        helper.processTouchEvent(event);
        return true;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev);
    }

    //第二步：要将left和right做摆放处理
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        leftView.layout(0, 0, leftViewWidth, leftViewHeight);
        rightView.layout(leftViewWidth, 0, leftViewWidth + rightViewWidth, leftViewWidth);
    }

    //在所有的孩子视图加载完成之后回调这个方法
    //只有在这个方法中才能获取所有的孩子
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //代码健壮性处理
        //要求DragLayout有且仅有两个孩子
        if (getChildCount() != 2) {
            throw new RuntimeException("have you follow my suggestion?bastard?fuc?");
        }

        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new RuntimeException("Your children must be instance of ViewGroup?You know?");
        }
        //导航为右边
        rightView = getChildAt(0);
        leftView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftViewWidth = leftView.getMeasuredWidth();
        leftViewHeight = leftView.getMeasuredHeight();
        rightViewWidth = rightView.getMeasuredWidth();
    }
}
