package com.xytsz.xytaj.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xytsz.xytaj.R;
import com.xytsz.xytaj.adapter.TopicAdapter;
import com.xytsz.xytaj.bean.AnwerInfo;
import com.xytsz.xytaj.fragment.ReadFragment;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.ReaderViewPager;
import com.xytsz.xytaj.ui.SlidingUpPanelLayout;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/3/9.
 * <p/>
 * 考试
 */
public class TestActivity extends AppCompatActivity  {


    private SlidingUpPanelLayout mLayout; // 最外层布局
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private ImageView shadowView;
    private ReaderViewPager readerViewPager;
    private List<AnwerInfo.SubDataBean> datas;
    private int score = 0;
    private int trainId;
    private AnwerInfo anwerInfo;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalContanstant.MYSENDSUCCESS:
                    String json = (String) msg.obj;
                    anwerInfo = JsonUtil.jsonToBean(json, AnwerInfo.class);
                    datas = anwerInfo.getList();
                    initList();
                    initData();
                    break;
                case GlobalContanstant.FAIL:
                    ToastUtil.shortToast(getApplicationContext(), "数据未获取");
                    break;
                case GlobalContanstant.CHECKFAIL:
                    ToastUtil.shortToast(getApplicationContext(), "上传失败");
                    break;
                case GlobalContanstant.CHECKPASS:
                    ToastUtil.shortToast(getApplicationContext(), "上传成功");
                    finish();
                    break;
            }
        }
    };
    private String result;
    private int personId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (getIntent() != null) {
            trainId = getIntent().getIntExtra("trainId", -1);
        }
        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        initActionbar();
        initSlidingUoPanel();

        getAnwer();

    }

    private void initData() {
        if (topicAdapter != null) {
            topicAdapter.setDataNum(datas.size());
        }

        initReadViewPager();

        Button bt_pre = (Button) findViewById(R.id.bt_pre);
        Button bt_next = (Button) findViewById(R.id.bt_next);
        TextView tv_up_answer = (TextView) findViewById(R.id.tv_up_anwer);

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

                //判断是否做完题，

                score = 0;

                for (int i = 0; i < datas.size(); i++) {

                    if (datas.get(i).getUserAnswer() == null) {
                        ToastUtil.shortToast(getApplicationContext(), "未做完题，不能交卷");
                        return;
                    }

                    score = score + datas.get(i).getScore();

                }

                new AlertDialog.Builder(TestActivity.this).setTitle("您的分数").
                        setMessage(score +"").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ToastUtil.shortToast(getApplicationContext(), "正在提交...");
                        //提交成绩
                        upData();
                    }
                }).create().show();


            }
        });
    }

    private void upData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    for (AnwerInfo.SubDataBean detail :
                            datas) {

                        result = up2service(detail);

                    }
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKPASS;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.CHECKFAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String up2service(AnwerInfo.SubDataBean detail) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.uploadtestmethod);
        soapObject.addProperty("TrainID", trainId);
        soapObject.addProperty("personId", personId);
        soapObject.addProperty("ChoiceQstId", detail.getId());
        soapObject.addProperty("StudentAnsw", detail.getUserAnswer());


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();
        return result;
    }

    private void initActionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.traintest);
        }
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
                AnwerInfo.SubDataBean subDataBean = datas.get(position);
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
                if (position == datas.size() - 1) {
                    //提示交卷
                    ToastUtil.shortToast(TestActivity.this, "最后一道题了");
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
        topicAdapter = new TopicAdapter(this, datas);
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

    private List<HeaderProperty> headerList = new ArrayList<>();
    // 解析json封装实体类
    private void getAnwer() {
        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);


        new Thread() {
            @Override
            public void run() {
                try {
                    String data = getData();
                    if (data != null) {
                        Message message = Message.obtain();
                        message.what = GlobalContanstant.MYSENDSUCCESS;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = GlobalContanstant.FAIL;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    private String getData() throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.gettestmethod);
        soapObject.addProperty("TrainId", trainId);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;
        String result = object.getProperty(0).toString();


        return result;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            return false;
//        }
        return false;
    }
}
