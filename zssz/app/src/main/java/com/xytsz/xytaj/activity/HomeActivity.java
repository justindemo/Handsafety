package com.xytsz.xytaj.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.xytsz.xytaj.bean.UpdateStatus;
import com.xytsz.xytaj.bean.VersionInfo;
import com.xytsz.xytaj.fragment.SuperviseFragment;
import com.xytsz.xytaj.fragment.SupplyFragment;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.base.BaseFragment;
import com.xytsz.xytaj.fragment.HomeFragment;
import com.xytsz.xytaj.adapter.MainAdapter;
import com.xytsz.xytaj.fragment.MeFragment;

import com.xytsz.xytaj.net.NetUrl;
import com.xytsz.xytaj.ui.NoScrollViewpager;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.util.IntentUtil;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;
import com.xytsz.xytaj.util.UpdateVersionUtil;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 2017/1/3.
 * <p/>
 * 主页
 */
public class HomeActivity extends AppCompatActivity {


    private RadioGroup mRadiogroup;
    private NoScrollViewpager mViewpager;
    private ArrayList<Fragment> fragments;

    private RelativeLayout rl_notonlie;
    private Button mbtrefresh;
    private ProgressBar mprogressbar;
    private int role;
    private boolean isOnCreat;
    private boolean isFirst;
    private VersionInfo versionInfo;
    private RadioButton mRadiosupply;
    /**
     * 防止误触退出
     */
    private long mExitTime;
    private static boolean isUpdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 没有登陆的时候，先登陆
         */

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_home);


        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);

        /*if (loginId == null || TextUtils.isEmpty(loginId)) {
            isFirst = false;
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            HomeActivity.this.finish();
        }else {
            isFirst = true;
        }*/

        /**
         *
         * 最后去掉注释
         */

        initView();

        if (isNetworkAvailable(getApplicationContext())) {
            isOnCreat = true;
            mViewpager.setVisibility(View.VISIBLE);
            rl_notonlie.setVisibility(View.GONE);
            mprogressbar.setVisibility(View.GONE);

        }else {
            rl_notonlie.setVisibility(View.VISIBLE);
            mprogressbar.setVisibility(View.GONE);
        }

        mbtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(getApplicationContext())){

                    //修改
                    mViewpager.setVisibility(View.VISIBLE);
                    rl_notonlie.setVisibility(View.GONE);
                    mprogressbar.setVisibility(View.GONE);
                }else {
                    ToastUtil.shortToast(getApplicationContext(),"请检查网络");
                }
            }
        });

        initData();
    }

    private void initView() {
        mRadiogroup = (RadioGroup) findViewById(R.id.homeactivity_rg_radiogroup);
        mRadiosupply = (RadioButton) findViewById(R.id.homeactivity_rbtn_supply);
        mViewpager = (NoScrollViewpager) findViewById(R.id.homeactivity_vp);

        rl_notonlie = (RelativeLayout)findViewById(R.id.rl_notonline);
        mprogressbar = (ProgressBar) findViewById(R.id.home_progressbar);
        mbtrefresh = (Button) findViewById(R.id.btn_refresh);
        //默认显示home界面
        mRadiogroup.check(R.id.homeactivity_rbtn_home);
        switch (role){
            case 1:
            case 2:
            case 3:
            case 4:
                mRadiosupply.setVisibility(View.GONE);
                break;
            case 5:
            case 6:
                mRadiosupply.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initData() {

        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new HomeFragment());
        fragments.add(new SuperviseFragment());
        fragments.add(new MeFragment());
        if (role == 5 || role == 6 ) {
            fragments.add(new SupplyFragment());
        }

        //把fragment填充到viewpager

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mViewpager.setAdapter(adapter);
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //当界面切换完成的时候
            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = (BaseFragment) fragments.get(position);
                //加载的时候可能会出错
                try {
                    fragment.initData();
                } catch (Exception e) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });




        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    //加载首页的时候加载Main
                    case R.id.homeactivity_rbtn_home:
                        mViewpager.setCurrentItem(0, false);
                        break;

                    //加载更多的时候  加载 监管页面
                    case R.id.homeactivity_rbtn_more:
                        mViewpager.setCurrentItem(1, false);
                        break;

                    //我的界面
                    case R.id.homeactivity_rbtn_me:
                        mViewpager.setCurrentItem(2, false);
                        break;

                    case R.id.homeactivity_rbtn_supply:
                        mViewpager.setCurrentItem(3,false);
                        break;

                }
            }
        });


        if (getIntent() != null) {
            String backHome = getIntent().getStringExtra("backHome");
            if (backHome != null && backHome.equals(GlobalContanstant.BACKHOME)) {
                mViewpager.setCurrentItem(0, false);
            }
        }




            //添加cookie
//        private List<HeaderProperty> headerList = new ArrayList<>();
            headerList.clear();
            HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                    SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

            headerList.add(headerPropertyObj);


            if (!isUpdata) {
                //先判断有没有现版本
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String versionInfo = UpdateVersionUtil.getVersionInfo(headerList);
                            Message message = Message.obtain();
                            message.obj = versionInfo;
                            message.what = VERSIONINFO;
                            handler.sendMessage(message);
                        } catch (Exception e) {

                        }

                    }
                }.start();
            }




    }
    private  List<HeaderProperty> headerList = new ArrayList<>();

    /**
     * @param context： 上下文
     * @return 网络是否可用
     */

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    private static final int DATA_SUCCESS = 1166666;
    private static final int VERSIONINFO = 144211;
    private static final int DATA_REPORT = 155552;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DATA_SUCCESS:
                    mViewpager.setVisibility(View.VISIBLE);
                    rl_notonlie.setVisibility(View.GONE);
                    mprogressbar.setVisibility(View.GONE);
                    break;
                case VERSIONINFO:
                    String  info = (String) msg.obj;

                    if (info !=null){
                        //检查更新
                        UpdateVersionUtil.localCheckedVersion(getApplicationContext(),new UpdateVersionUtil.UpdateListener() {

                            @Override
                            public void onUpdateReturned(int updateStatus, final VersionInfo versionInfo) {
                                //判断回调过来的版本检测状态
                                switch (updateStatus) {
                                    case UpdateStatus.YES:
                                        //弹出更新提示
                                        isUpdata= UpdateVersionUtil.showDialog(HomeActivity.this,versionInfo,false);
                                        break;
                                    case UpdateStatus.NO:
                                        //没有新版本
                                        //ToastUtil.shortToast(getApplicationContext(), "已经是最新版本了!");
                                        break;
                                    case UpdateStatus.NOWIFI:
                                        //当前是非wifi网络
                                        //UpdateVersionUtil.showDialog(getContext(),versionInfo);
                                        isUpdata = UpdateVersionUtil.showDialog(HomeActivity.this,versionInfo,true);

                                        break;
                                    case UpdateStatus.ERROR:
                                        //检测失败
                                        ToastUtil.shortToast(getApplicationContext(), "检测失败，请稍后重试！");
                                        break;
                                    case UpdateStatus.TIMEOUT:
                                        //链接超时
                                        ToastUtil.shortToast(getApplicationContext(), "链接超时，请检查网络设置!");
                                        break;
                                }
                            }
                        },info);
                    }
                    break;

                case DATA_REPORT:
                    mprogressbar.setVisibility(View.GONE);
                    rl_notonlie.setVisibility(View.VISIBLE);
                    ToastUtil.shortToast(getApplicationContext(),"网络异常,未获取数据,请刷新");
                    break;
            }
        }
    };



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.shortToast(HomeActivity.this,"再按一次退出程序");
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }





    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //判断是否有网络
        //做判断 如果是只走 onResume 那就请求  ,如果走onCreate 就不请求
        //isOncreat  =false
       if (!isOnCreat) {
            if (isNetworkAvailable(getApplicationContext())) {

                mViewpager.setVisibility(View.VISIBLE);
                rl_notonlie.setVisibility(View.GONE);
                mprogressbar.setVisibility(View.GONE);
            } else {
                ToastUtil.shortToast(getApplicationContext(), "未连接网络");
                rl_notonlie.setVisibility(View.VISIBLE);
                mViewpager.setVisibility(View.GONE);

            }
        }
        isOnCreat = false;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
