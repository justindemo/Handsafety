package com.xytsz.xytaj.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.activity.MemberCompanyShowActivity;
import com.xytsz.xytaj.bean.Scroller;
import com.xytsz.xytaj.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/7.
 *
 */
public class ScrollViewPager extends ViewPager {

    private List<Scroller> scrollers = new ArrayList<>();

    private Context context;
    private List<View> mllDots = new ArrayList<>();
    private MyAdapter myAdapter;
    private int currentPosition = 0;
    private RunnableTask runnableTask;
    private int downX;
    private int downY;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 实现界面切换
            ScrollViewPager.this.setCurrentItem(currentPosition);
            // 一次切换完成,接着执行第二次切换
            roll();
        };
    };
    private List<String> imageUrls;
    private int mTouchSlop;

    public ScrollViewPager(Context context, List<View> llDots) {
        super(context);
        this.context = context;
        this.mllDots = llDots;

        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

        runnableTask = new RunnableTask();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //mllDots.get(position).setBackgroundColor(Color.BLUE);
                mllDots.get(position).setBackgroundResource(R.mipmap.dot_focus);
                for (int i = 0; i < mllDots.size(); i++) {
                    if (i != position){
                        mllDots.get(i).setBackgroundResource(R.mipmap.dot_normal);
                    }
                }

                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initImage(List<String> imageUrls){
//        this.scrollers = imageUrls;
        this.imageUrls = imageUrls;
    }

    public void roll(){

        if (myAdapter == null){
            myAdapter = new MyAdapter();
            setAdapter(myAdapter);
        }else {
            myAdapter.notifyDataSetChanged();
        }

        handler.postDelayed(runnableTask, 3000);
    }




    class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View view = View.inflate(getContext(), R.layout.viewpager_imageview,null);
            ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
            Glide.with(getContext()).load(imageUrls.get(position)).into(imageview);




            container.addView(view);

            //设置图片按下事件
            view.setOnTouchListener(new OnTouchListener() {
                // 当触摸着控件的时候调用
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //不可自动滚动
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            //可以重新滚动
                            roll();
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            //可以重新滚动
                            roll();
                            break;

                    }
                    return true;
                }
            });



            return view;
        }
    }

    // 出现点文本和图片错乱的原因:因为在创建viewpager的时候设置handler发送延迟消息,但是在销毁的时候并没有去除handler操作,所以会造成当再次创建viewpager的时候会再次创建一个handler发送
    // 延迟消息,加上之前创建的,就有两个在发送延迟消息
    // 当viewpager销毁的时候调用的方法
    @Override
    protected void onDetachedFromWindow() {
        // 去除延迟消息
        handler.removeCallbacksAndMessages(null);// 去除handler延迟消息操作
        super.onDetachedFromWindow();
    }

    class RunnableTask implements Runnable{
        @Override
        public void run() {
            //设置无线滚动
            currentPosition = (currentPosition + 1) % imageUrls.size();
            // 进行滚动操作
            handler.obtainMessage().sendToTarget();// 将延迟消息转发到handler中
        }
    }
    // 滑动viewpager要实现的效果
    // 从右往左:如果是最后一个界面父控件viewpager切换界面,如果不是最后一个界面当前的viewpager切换图片
    // 从左往右:如果是第一个界面父控件viewpager打开侧拉菜单,如果不是第一个界面当前的viewpager切换到上一个界面

    int touchFlag = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 1.判断是横向滑动还是竖向滑动
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                touchFlag = 0;
                // getParent : 获取父控件
                getParent().requestDisallowInterceptTouchEvent(true);// 请求父控件是否传递事件,true:不拦截,false:拦截事件,这里的事件指的是下一个事件

                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();




                // 判断是横向滑动还是竖向滑动
                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    // 横向滑动
                    // 从右往左:如果是最后一个界面父控件viewpager切换界面,如果不是最后一个界面当前的viewpager切换图片
                    if (moveX - downX < 0
                            && currentPosition == getAdapter().getCount() - 1) {
                        // 父控件viewpager切换界面
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (moveX - downX < 0
                            && currentPosition < getAdapter().getCount() - 1) {
                        // 当前的viewpager切换图片
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else if (moveX - downX > 0 && currentPosition == 0) {
                        // 从左往右:如果是第一个界面父控件viewpager打开侧拉菜单,如果不是第一个界面当前的viewpager切换到上一个界面
                        // 父控件viewpager打开侧拉菜单
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else if (moveX - downX > 0 && currentPosition > 0) {
                        // 当前的viewpager切换到上一个界面
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    if (Math.abs(moveX - downX) <mTouchSlop){
                        touchFlag= 0;
                    }else {
                        touchFlag =-1;
                    }

                } else {
                    // 竖向滑动处理的时候listview
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                if (touchFlag == 0){
                    int currentItem = getCurrentItem();
                    Intent intent = new Intent(context,MemberCompanyShowActivity.class);
                    intent.putExtra("id", currentItem);
                    intent.putExtra("companyName", "公司名称");
                    context.startActivity(intent);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
