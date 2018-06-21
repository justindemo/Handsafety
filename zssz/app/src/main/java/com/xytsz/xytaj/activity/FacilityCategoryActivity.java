package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.FacilityHeadAdapter;
import com.xytsz.xytaj.adapter.LightFragmentAdapter;
import com.xytsz.xytaj.bean.Category;
import com.xytsz.xytaj.bean.CategoryCallback;
import com.xytsz.xytaj.bean.FacilityHead;
import com.xytsz.xytaj.bean.FacilityHeadCallback;
import com.xytsz.xytaj.fragment.FacilityCategroyFragment;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.FixedSpeedScroller;
import com.xytsz.xytaj.ui.ZoomOutPageTransformer;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by admin on 2018/5/28.
 * <p/>
 * 产品分类
 */
public class FacilityCategoryActivity extends AppCompatActivity {

    private static final int VIEWPAGER_CHANGE = 0;
    @Bind(R.id.facility_tab)
    TabLayout facilityTab;
    @Bind(R.id.facility_viewpager)
    ViewPager facilityViewpager;
    @Bind(R.id.facility_head_vp)
    ViewPager facilityHeadVp;
    @Bind(R.id.rl)
    RelativeLayout rl;

    private String categroy;

    private String title;
    private ArrayList<Fragment> fragments;
    private List<Category.Detail> categroyDatas;
    private int parentId;
    private List<String> mtitles = new ArrayList<>();
    private int produceID;
    private int mTouchSlop;
    private List<FacilityHead.DataBean> headDatas;
    private int personId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            categroy = getIntent().getStringExtra("category");

        }
        setContentView(R.layout.activity_facilitycategroy);
        ButterKnife.bind(this);
        if (categroy != null) {
            switch (categroy) {
                case GlobalContanstant.BOOM:
                    title = "粉尘防爆";
                    parentId = 3;
                    break;
                case GlobalContanstant.FIRE:
                    title = "消防安全";
                    parentId = 2;
                    break;
                case GlobalContanstant.HEALTH:
                    title = "职业卫生";
                    parentId = 4;
                    break;
                case GlobalContanstant.ENVIR:
                    title = "环境保护";
                    parentId = 5;
                    break;
            }
            initActionbar(title);
        }
        initData();

    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.clear();
        mtitles.clear();

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        getHeadList();
        getCategoryList(parentId);

    }

    private int maxPager;
    private void getHeadList() {
        OkHttpUtils.get().url(NetUrl.SERVERURL2 + NetUrl.getScrollerData +NetUrl.tag +personId )
                .build().execute(new FacilityHeadCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.shortToast(getApplicationContext(), "noting");
            }

            @Override
            public void onResponse(FacilityHead response, int id) {
                if (response != null) {
                    headDatas =   response.getData();
                    if (headDatas != null && headDatas.size() != 0) {

//                        maxPager = headDatas.size() * 1000*100;
//                        facilityHeadVp.setPageMargin(DensityUtil.dip2px(getApplicationContext(), 10));
                        facilityHeadVp.setPageTransformer(true, new ZoomOutPageTransformer());
                        facilityHeadVp.setAdapter(new FacilityHeadAdapter(FacilityCategoryActivity.this, headDatas,parentId));
                        if (headDatas.size() >=3) {
                            facilityHeadVp.setCurrentItem(1);
                            facilityHeadVp.setOffscreenPageLimit(3);
                        }


                        facilityHeadVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                if (rl != null){
                                    rl.invalidate();
                                }


                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                if (state == ViewPager.SCROLL_STATE_IDLE) {
                                    handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGE, 3000);
                                }else{
                                    handler.removeMessages(VIEWPAGER_CHANGE);//停止给handler发送消息
                                }
                            }
                        });

                        //触摸事件交给子view
                        rl.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return facilityHeadVp.dispatchTouchEvent(event);
                            }
                        });

                        ViewConfiguration configuration = ViewConfiguration.get(getApplicationContext());
                        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

                        facilityHeadVp.setOnTouchListener(new View.OnTouchListener() {
                            int touchFlag = 0;
                            int downX;
                            int downY;

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()){
                                    case MotionEvent.ACTION_DOWN:

                                        touchFlag = 0;
                                        downX = (int) event.getX();
                                        downY = (int) event.getY();

                                        break;
                                    case MotionEvent.ACTION_MOVE:
                                        int moveX = (int) event.getX();

                                        if (Math.abs(moveX - downX) < mTouchSlop){
                                                touchFlag= 0;
                                            }else {
                                                touchFlag =-1;
                                            }


                                        break;
                                    case MotionEvent.ACTION_UP:

                                        if (touchFlag == 0){
                                            ToastUtil.shortToast(getApplicationContext(),"点击了");
                                            int currentItem = facilityHeadVp.getCurrentItem();
//                                            currentItem = currentItem% headDatas.size();
                                            Intent intent = new Intent(FacilityCategoryActivity.this,MemberCompanyShowActivity.class);
                                            intent.putExtra("companyID", headDatas.get(currentItem).getCompanyID());
                                            intent.putExtra("fromID", GlobalContanstant.fromList);

                                            startActivity(intent);
                                        }
                                        break;
                                }
                                return false;
                            }
                        });


                    }
                }
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

                if (headDatas.size()>1) {
                    switchPage();
                }
        }
    };

    //viewPager 平滑
    private void setScrollerDuration() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(FacilityCategoryActivity.this,
                    new DecelerateInterpolator());
            scroller.setmDuration(1000);
            field.set(facilityHeadVp, scroller);
        } catch (Exception e) {
            Log.e("@", "", e);
        }
    }
    private void switchPage() {

        int currentItem = facilityHeadVp.getCurrentItem();//获取当前显示界面的位置
        //2.根据当前界面的位置获取下一个界面的位置
        //如果滑倒了最后一个界面,再往下滑,滑动到第一个界面
        //getAdapter : 获取viewpager设置的adapter
        //getCount : 获取adapter的getcount的值
        /*if (currentItem == facilityHeadVp.getAdapter().getCount()) {
            currentItem=0;
        }else{
            currentItem++;
        }*/

        currentItem = (currentItem +1)% headDatas.size();

        setScrollerDuration();
        //3.将viwepager设置成显示下一个界面
        facilityHeadVp.setCurrentItem(currentItem,true);
        //4.在发送一个延迟消息,进行下一次的界面切换
        handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGE, 3000);
    }


    private void getCategoryList(final int parentId) {
        OkHttpUtils.get().url(NetUrl.SERVERURL2 + NetUrl.getCategoryList)
                .build().execute(new CategoryCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtil.shortToast(getApplicationContext(), "nothing");
            }

            @Override
            public void onResponse(Category response, int id) {
                if (response != null) {
                    categroyDatas = response.getData();
                    for (Category.Detail detail : categroyDatas) {
                        if (parentId == detail.getParentID()) {
                            mtitles.add(detail.getClassName());
                        }
                    }
                    for (int i = 0; i < mtitles.size(); i++) {

                        facilityTab.addTab(facilityTab.newTab().setText(mtitles.get(i)));
                        for (Category.Detail detail : categroyDatas) {
                            if (TextUtils.equals(mtitles.get(i), detail.getClassName())) {
                                produceID = detail.getID();
                            }
                        }
                        fragments.add(FacilityCategroyFragment.newInstance(produceID));

                    }


                    //添加viewpager的adapter
                    LightFragmentAdapter lightFragmentAdapter = new LightFragmentAdapter(getSupportFragmentManager(),
                            fragments, mtitles);
                    facilityViewpager.setAdapter(lightFragmentAdapter);

                    //让标签 跟着viewpager 滑动
                    facilityTab.setupWithViewPager(facilityViewpager);

                    facilityTab.setTabsFromPagerAdapter(lightFragmentAdapter);
                } else {
                    ToastUtil.shortToast(getApplicationContext(), "respone = null");
                }
            }
        });
    }

    private void initActionbar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        //delayMillis : 延迟时间
        handler.sendEmptyMessageDelayed(VIEWPAGER_CHANGE, 3000);//给handler发送延迟消息
        super.onStart();
    }

    @Override
    protected void onStop() {
        //停止发送消息
        handler.removeMessages(VIEWPAGER_CHANGE);//停止给handler发送消息
        handler.removeCallbacksAndMessages(null);
        super.onStop();
    }

}
