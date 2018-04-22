package com.xytsz.xytaj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.TopicAdapter;
import com.xytsz.xytaj.bean.AnwerInfo;
import com.xytsz.xytaj.fragment.ReadFragment;
import com.xytsz.xytaj.ui.ReaderViewPager;
import com.xytsz.xytaj.ui.SlidingUpPanelLayout;
import com.xytsz.xytaj.util.CallBackUtil;
import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by admin on 2018/3/9.
 * <p/>
 * 考试
 */
public class TestActivity extends AppCompatActivity implements ReadFragment.OnReadFragmentListener {


    private SlidingUpPanelLayout mLayout; // 最外层布局
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private ImageView shadowView;
    private ReaderViewPager readerViewPager;
    private List<AnwerInfo.DataBean.SubDataBean> datas;
    private int score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initActionbar();
        initSlidingUoPanel();

        AnwerInfo anwerInfo = getAnwer();
        datas = anwerInfo.getData().getData();

        initList();

        if (topicAdapter != null) {
            topicAdapter.setDataNum(datas.size());
        }

        initReadViewPager();

        CallBackUtil.setReadFragementListener(this);
        Button bt_pre = (Button) findViewById(R.id.bt_pre);
        Button bt_next = (Button) findViewById(R.id.bt_next);
        TextView tv_up_answer  = (TextView) findViewById(R.id.tv_up_anwer);

        // 上一题按钮监听
        bt_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = readerViewPager.getCurrentItem();
                currentItem = currentItem - 1;
                if (currentItem > datas.size() - 1) {
                    currentItem = datas.size() - 1;
                }
                readerViewPager.setCurrentItem(currentItem, true);
            }
        });

        // 下一题按钮监听
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = readerViewPager.getCurrentItem();

                currentItem = currentItem + 1;
                if (currentItem < 0) {
                    currentItem = 0;
                }
                readerViewPager.setCurrentItem(currentItem, true);

            }
        });

        /**
         * 交卷的点击事件。
         */
        tv_up_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到分数界面。---还是什么界面。

                //判断是否做完题，

                for (int i = 0; i <datas.size();i++) {
                    score = score + datas.get(i).getScore();

                    if (datas.get(i).getUserAnswer() != null){
                        ToastUtil.shortToast(getApplicationContext(),"未做完题，不能交卷");
                        return;
                    }


                }

                ToastUtil.shortToast(getApplicationContext(),"分数是："+score);

                Intent intent = new Intent(TestActivity.this,TestSultActivity.class);
                intent.putExtra("score",score);
                startActivity(intent);
                finish();

            }
        });
    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.traintest);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private int prePosition2;
    private int curPosition2;

    // 初始化中间ViewPager并关联数据
    private void initReadViewPager() {
        shadowView = (ImageView) findViewById(R.id.shadowView);
        readerViewPager = (ReaderViewPager) findViewById(R.id.readerViewPager);



        // ViewPager适配器
        readerViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                AnwerInfo.DataBean.SubDataBean subDataBean = datas.get(position);
                return ReadFragment.newInstance(subDataBean);
            }

            @Override
            public int getCount() {
                return datas.size();
            }
        });

        // ViewPager滑动监听
        readerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shadowView.setTranslationX(readerViewPager.getWidth() - positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                curPosition2 = position;

                topicAdapter.notifyCurPosition(curPosition2);
                topicAdapter.notifyPrePosition(prePosition2);
                prePosition2 = curPosition2;
                if (position == datas.size()-1){
                    //提示交卷
                    ToastUtil.shortToast(TestActivity.this,"最后一道题了");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int prePosition;
    private int curPosition;

    // 初始化底部弹出的RecyclerView和数据源
    private void initList() {
        recyclerView = (RecyclerView) findViewById(R.id.list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        topicAdapter = new TopicAdapter(this,datas);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(topicAdapter);

        // RecyclerView的点击监听
        topicAdapter.setOnTopicClickListener(new TopicAdapter.OnTopicClickListener() {
            @Override
            public void onClick(TopicAdapter.TopicViewHolder holder, int position) {
                curPosition = position;
                // 点击后自动收回
                if (mLayout != null &&
                        (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                                mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

                readerViewPager.setCurrentItem(position);

                topicAdapter.notifyCurPosition(curPosition);
                topicAdapter.notifyPrePosition(prePosition);

                prePosition = curPosition;
            }
        });
    }

    // 初始化SlidingUpPanelLayout
    private void initSlidingUoPanel() {
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        // 屏幕高度
        int height = getWindowManager().getDefaultDisplay().getHeight();

        LinearLayout dragView = (LinearLayout) findViewById(R.id.dragView);
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * 0.8f));
        dragView.setLayoutParams(params);

        // 左右滑动监听
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
            }
        });

        // 点击关闭底部弹出的"查看"
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    // 返回键监听：把当前mlayout状态置为收缩
    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED ||
                        mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    // 读取本地json数据
    public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    // 解析json封装实体类
    private AnwerInfo getAnwer() {
        try {
            InputStream in = getAssets().open("anwer.json");
            AnwerInfo anwerInfo = JSON.parseObject(inputStream2String(in), AnwerInfo.class);

            return anwerInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onNext(final int position) {

        //延时滑动到下一题
        if (position <= datas.size() -1){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readerViewPager.setCurrentItem(position);
                }
            },500);

        }
    }
}
