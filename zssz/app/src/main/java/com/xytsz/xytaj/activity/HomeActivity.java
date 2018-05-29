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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 2017/1/3.
 * <p/>
 * 主页
 */
public class HomeActivity extends AppCompatActivity {


    private RadioGroup mRadiogroup;
    private NoScrollViewpager mViewpager;
    private ArrayList<Fragment> fragments;
    private Boolean isFive;
    private RelativeLayout rl_notonlie;
    private Button mbtrefresh;
    private ProgressBar mprogressbar;
    private int role;
    private boolean isOnCreat;
    private boolean isFirst;
    private VersionInfo versionInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 没有登陆的时候，先登陆
         */

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_home);
        String loginId = SpUtils.getString(getApplicationContext(), GlobalContanstant.LOGINID);

        role = SpUtils.getInt(getApplicationContext(), GlobalContanstant.ROLE);

        if (loginId == null || TextUtils.isEmpty(loginId)) {
            isFirst = false;
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            HomeActivity.this.finish();
        }else {
            isFirst = true;
        }

        /**
         *
         * 最后去掉注释
         */

        initView();

        if (isNetworkAvailable(getApplicationContext())) {
            isOnCreat = true;
            getData();

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
                    getData();
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

    private void getData() {
        new  Thread(){
            @Override
            public void run() {
                try {
                    int allUserCount = getAllUserCount(NetUrl.getAllUserCountMethodName, NetUrl.getAllUserCount_SOAP_ACITION);
                    SpUtils.saveInt(getApplicationContext(), GlobalContanstant.ALLUSERCOUNT,allUserCount);

                }catch (Exception e){

                }

            }
        }.start();

    }

    public int getAllUserCount(String method,String soap_action) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(soap_action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return Integer.valueOf(object.getProperty(0).toString());

    }




    private void initView() {
        mRadiogroup = (RadioGroup) findViewById(R.id.homeactivity_rg_radiogroup);
        mViewpager = (NoScrollViewpager) findViewById(R.id.homeactivity_vp);

        rl_notonlie = (RelativeLayout)findViewById(R.id.rl_notonline);
        mprogressbar = (ProgressBar) findViewById(R.id.home_progressbar);
        mbtrefresh = (Button) findViewById(R.id.btn_refresh);
        //默认显示home界面
        mRadiogroup.check(R.id.homeactivity_rbtn_home);
    }

    private void initData() {

        fragments = new ArrayList<>();
        fragments.clear();
        fragments.add(new HomeFragment());
        fragments.add(new SuperviseFragment());
        fragments.add(new MeFragment());
        fragments.add(new SupplyFragment());
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



        if (isFirst){
            //先判断有没有现版本
            new Thread(){
                @Override
                public void run() {
                    try {
                        String versionInfo = UpdateVersionUtil.getVersionInfo();
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



    private Boolean isweekfive() {

        final long time = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(time);

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        int hour = calendar.get(Calendar.HOUR);

        int minute = calendar.get(Calendar.MINUTE);
        if (week == 6 && hour == 17 ){
            return true;
        }

        return false;

    }




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
                                        UpdateVersionUtil.showDialog(HomeActivity.this,versionInfo,false);
                                        break;
                                    case UpdateStatus.NO:
                                        //没有新版本
                                        //ToastUtil.shortToast(getApplicationContext(), "已经是最新版本了!");
                                        break;
                                    case UpdateStatus.NOWIFI:
                                        //当前是非wifi网络
                                        //UpdateVersionUtil.showDialog(getContext(),versionInfo);
                                        UpdateVersionUtil.showDialog(HomeActivity.this,versionInfo,true);

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

        if (keyCode == KeyEvent.KEYCODE_BACK)

        {

            // 创建退出对话框

            AlertDialog.Builder isExit = new AlertDialog.Builder(this);

            // 设置对话框标题

            isExit.setTitle("系统提示");

            // 设置对话框消息

            isExit.setMessage("确定要退出吗");

            // 添加选择按钮并注册监听

            isExit.setPositiveButton("确定", listener);

            isExit.setNegativeButton("取消", listener);

            // 显示对话框

            isExit.show();


        }

        return false;
    }


    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()

    {

        public void onClick(DialogInterface dialog, int which)

        {

            switch (which)

            {

                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

                    finish();

                    break;

                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框

                    break;

                default:

                    break;

            }

        }

    };


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
                getData();
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

        isFive =  isweekfive();
        if (isFive){
            String content ="主人,您已经使用一周的掌上市政了,花一分钟的时间对他评价一下吧！";

            new AlertDialog.Builder(this).setTitle("掌上市政评价").setMessage(content).setNegativeButton("别烦我", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isFive = false;
                }
            }).setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    IntentUtil.startActivity(HomeActivity.this,AppraiseActivity.class);
                    dialog.dismiss();
                    isFive = false;
                }
            }).create().show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}
