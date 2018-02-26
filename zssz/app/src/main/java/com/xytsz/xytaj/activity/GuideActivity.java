package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.util.DensityUtil;
import com.xytsz.xytaj.util.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/3/8.
 * 引导页
 *
 */
public class GuideActivity extends AppCompatActivity {
    protected static final String TAG = "GuideActivity";

    private ViewPager mViewPager;

    private int[] mImageIds = new int[] { R.mipmap.guide_1,
            R.mipmap.guide_2, R.mipmap.guide_3 };
    private List<ImageView> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
        mStart = (Button) findViewById(R.id.guide_btn_start);
        mLLDots = (LinearLayout) findViewById(R.id.guide_ll_dots);
        mRedPoint = (ImageView) findViewById(R.id.guide_iv_redponit);

        // 根据图片的张数,创建iamgeview,然后将imageview放到viewpager中展示
        initData();

        // 当切换到第三个界面的时候显示button按钮,当切换到第二个或者是第一个界面的时候隐藏button按钮
        // 设置viewpager的界面切换监听
        mViewPager.setOnPageChangeListener(onPageChangeListener);

        // 设置viewpager的adapter
        mViewPager.setAdapter(new Myadapter());

        //设置开始体验的按钮的点击事件
        mStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //保存不是第一次进入的状态
                SpUtils.saveBoolean(GuideActivity.this, GlobalContanstant.ISFIRSTENTER, false);
                //跳转到首页,并且将当前页面移出
                startActivity(new Intent(GuideActivity.this,HomeActivity.class));
                //移除当前页面
                finish();
            }
        });
    }

    /** viewpager的界面切换监听 **/
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        // 当界面切换完成调用
        // position : 切换完成界面的位置
        @Override
        public void onPageSelected(int position) {
            // 判断当前切换的界面是否是第三个界面,是显示button按钮,不是隐藏button按钮
            if (position == list.size() - 1) {
                // 显示button按钮
                mStart.setVisibility(View.VISIBLE);
            } else {
                // 隐藏button按钮
                mStart.setVisibility(View.INVISIBLE);
            }
        }

        // 当界面切换调用
        //position : 条目的位置
        //positionOffset : 移动的偏移的百分比,在每个界面中是有0%到100%,如果切换界面,会从0%重新开始的
        //positionOffsetPixels : 移动的偏移的像素
        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

            Log.i(TAG, "条目的位置:"+position+" 偏移百分比:"+positionOffset);

            //当界面切换的时候,移动红色的点,移动的距离是20
            //红色点的实时的移动距离 = 20 * 移动的偏移的百分比;
            //position : 0,1,2
            //第一个界面的时候不加,当界面是第二个界面的时候+20,第三个界面的时候+2*20
            int leftmargin = (int) (DensityUtil.dip2px(GuideActivity.this, 20) * positionOffset)+position * DensityUtil.dip2px(GuideActivity.this, 20);
            //可以通过不断的修改红色点距离左边的距离,实现红色点的移动操作
            //通过代码设置红色点距离左边的距离的属性了
            //获取红色点的属性
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPoint.getLayoutParams();//获取红色点的属性
            //设置红色点距离左边的距离
            params.leftMargin = leftmargin;
            //更改了红色点的属性,还要重新设置给红色点,属性才会生效
            mRedPoint.setLayoutParams(params);
        }

        // 当界面切换状态改变调用
        @Override
        public void onPageScrollStateChanged(int state) {


        }
    };

    private Button mStart;

    private LinearLayout mLLDots;

    private ImageView mRedPoint;

    /**
     * 初始化数据操作
     */
    private void initData() {
        list = new ArrayList<>();
        list.clear();// 清空数据,保证每次加载的是新的数据
        // 根据图片的张数,创建iamgeview
        for (int i = 0; i < mImageIds.length; i++) {
            // 创建imageView;
            createImageView(i);
            // 根据图片的张数创建相应个数的点
            createDot();
        }
    }

    /**
     * 创建点
     */
    private void createDot() {
        ImageView ponit = new ImageView(this);
        // 设置点的背景
        ponit.setBackgroundResource(R.drawable.shape_guide_ponit_gray);
        // 通过代码设置点与点之间的距离
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置点与点之间的距离
        params.rightMargin=DensityUtil.dip2px(this, 20);
        //将属性设置给点,让属性生效
        ponit.setLayoutParams(params);//通过代码设置点的属性
        //将点存放到布局文件的linearlayout中显示
        mLLDots.addView(ponit);
    }

    /**
     * 创建imageView
     */
    private void createImageView(int i) {
        ImageView imageView = new ImageView(this);
        // 设置imageview显示图片
        imageView.setBackgroundResource(mImageIds[i]);
        // 添加到list集合保存
        list.add(imageView);
    }

    /** viewpager的adapter **/
    private class Myadapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 根据条目的位置,获取要添加的view对象
            ImageView imageView = list.get(position);
            // 添加到viewpager中
            container.addView(imageView);
            // 添加什么view对象,返回什么view对象,进行显示
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
