package com.xytsz.xytaj.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.ReportData;
import com.xytsz.xytaj.global.GlobalContanstant;
import com.xytsz.xytaj.net.NetUrl;

import com.xytsz.xytaj.util.BitmapUtil;
import com.xytsz.xytaj.util.FileBase64Util;
import com.xytsz.xytaj.util.FileUtils;
import com.xytsz.xytaj.util.JsonUtil;
import com.xytsz.xytaj.util.PermissionUtils;
import com.xytsz.xytaj.util.SoundUtil;
import com.xytsz.xytaj.util.SpUtils;
import com.xytsz.xytaj.util.ToastUtil;


import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


/**
 * Created by admin on 2017/1/10.
 * 上报页面
 */
public class ReportActivity extends AppCompatActivity {

    private static final int AUDIO_SUCCESS = 500;
    private static final int AUDIO_FAIL = 404;
    private static final int VIDEO_SUCCESS = 200;
    private static final int VIDEO_FAIL = 303;
    private LocationClient locationClient;
    public BDAbstractLocationListener myListener = new MyListener();
    private ImageView mIvphoto1;
    private ImageView mIvphoto2;
    private ImageView mIvphoto3;
    private EditText mEtDesc;
    private EditText mEtlocation;

    private Button mbtReport;
    //图片的存储位置
    private static final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";//图片的存储目录
    private static final String audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Audio/";
    private DiseaseInformation diseaseInformation;
    private String reportResult;
    private String isphotoSuccess1;
    private String path;
    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();
    private String taskNumber;
    private Uri fileUri;
    private int personId;
    private String dialogtitle;
    private String uploading;
    private String upimg;
    private String imgsuccess;
    private String request;
    private String notifa;
    private String notifatitle;
    private String error;
    private ImageView mIvInput;
    private TextView mtvPressAudio;
    private TextView mtvAudio;
    private TextView mtvReset;
    private LinearLayout mllAudio;

    private boolean btn_vocie = false;
    private boolean btn_road = false;
    private boolean videoflg = false;
    private static long startTime;
    private SoundUtil soundUtil;
    private String deleteTitle;
    private String deleteContent;
    private String deleteOk;
    private String deleteCancle;
    private String recordLittle;
    private String recordMax;
    private String audiosuccess;
    private String videosuccess;
    private String audioFail;
    private String disrecord;
    private String reportMore;
    private String reportLocation;
    private ImageView mIvAdd;
    private ImageView mIvPlay;
    private ImageView mIvVideoDelete;
    private ImageView mIvAddVideo;
    private FrameLayout mFlvideo;
    private RelativeLayout mllplay;
    private LinearLayout mllAddVideo;
    private String videopath;
    private String videoFail;
    private String videoName;
    private String deletevideoContent;
    private String deleteVideoTitle;
    private String imageerror;
    private String uperror;
    private String reportsuccess;
    private RelativeLayout rl_notonlie;
    private LinearLayout ll_report;
    private LinearLayout mprogressbar;
    private Button mbtrefresh;
    private String scanResult;
    private TextView mFacility;
    private TextView mFacilityTeam;
    private TextView mFacilityPerson;
    private TextView mFacilityCheck;
    private TextView mFacilityProblem;
    private TextView mFacilityLoca;
    private ReportData reportData;
    private String[] problemitems;
    private String number;
    private String userName;
    private File file;
    private Bitmap scaledBitmap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fileResult = savedInstanceState.getString("file_path");
        }
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            scanResult = getIntent().getStringExtra("scan");
            number = getIntent().getStringExtra("number");

        }
        setContentView(R.layout.activity_report);

        initAcitionbar();

        personId = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);
        userName = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);

        dialogtitle = this.getString(R.string.report_dialog_title);
        uploading = this.getString(R.string.report_uploading);
        upimg = this.getString(R.string.report_upimg);
        imgsuccess = this.getString(R.string.report_imgsuccess);
        request = this.getString(R.string.report_request);
        deleteTitle = getString(R.string.report_delete_title);
        deleteVideoTitle = getString(R.string.report_delete_video_title);
        deleteContent = getString(R.string.report_delete_content);
        deletevideoContent = getString(R.string.report_delete_video_content);
        deleteOk = getString(R.string.report_delete_ok);
        deleteCancle = getString(R.string.report_delete_cancle);
        recordLittle = getString(R.string.report_record_little);
        recordMax = getString(R.string.report_record_max);


        notifa = this.getString(R.string.report_notifica);
        notifatitle = this.getString(R.string.report_notifica_title);
        error = this.getString(R.string.report_error);
        imageerror = this.getString(R.string.report_imageerror);
        reportsuccess = this.getString(R.string.report_success);
        uperror = this.getString(R.string.report_uperror);

        audiosuccess = getString(R.string.audio_success);
        videosuccess = getString(R.string.video_success);
        audioFail = getString(R.string.audio_fail);
        videoFail = getString(R.string.video_fail);

        disrecord = getString(R.string.disrecord);

        reportMore = getString(R.string.reportmore);

        reportLocation = getString(R.string.reportlocation);

        rl_notonlie = (RelativeLayout) findViewById(R.id.rl_notonline);
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        mprogressbar = (LinearLayout) findViewById(R.id.home_progressbar);
        mbtrefresh = (Button) findViewById(R.id.btn_refresh);

        mFacility = (TextView) findViewById(R.id.report_facility);
        mFacilityTeam = (TextView) findViewById(R.id.report_facility_team);
        mFacilityPerson = (TextView) findViewById(R.id.report_facility_person);
        mFacilityCheck = (TextView) findViewById(R.id.report_facility_check);
        mFacilityProblem = (TextView) findViewById(R.id.report_facility_problem);
        mFacilityLoca = (TextView) findViewById(R.id.report_facility_loca);

        mbtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.isNetworkAvailable(getApplicationContext())) {
                    ll_report.setVisibility(View.INVISIBLE);
                    rl_notonlie.setVisibility(View.GONE);
                    mprogressbar.setVisibility(View.VISIBLE);
                    getData();

                } else {
                    ToastUtil.shortToast(getApplicationContext(), "请检查网络");
                }
            }
        });


        refreshData();

    }


    private void refreshData() {
        if (HomeActivity.isNetworkAvailable(getApplicationContext())) {
            mprogressbar.setVisibility(View.VISIBLE);
            ll_report.setVisibility(View.INVISIBLE);
            getData();
        } else {
            rl_notonlie.setVisibility(View.VISIBLE);
            mprogressbar.setVisibility(View.GONE);
        }


    }

    private static final int DATA_REPORT = 155552;
    private static final int DATA_SUCCESS = 1166666;

    private void getData() {
        new Thread() {
            @Override
            public void run() {

                try {
                    // 获取传的值，得到json
                    String reportData = getJson(NetUrl.getReportDataMethodname, NetUrl.getReport_SOAP_ACTION);
                    if (reportData != null) {
                        ReportData data = JsonUtil.jsonToBean(reportData, ReportData.class);
                        Message message = Message.obtain();
                        message.what = DATA_SUCCESS;
                        message.obj = data;
                        handler.sendMessage(message);

                    } else {
                        Message message = Message.obtain();
                        message.what = DATA_REPORT;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    Message message = Message.obtain();
                    message.what = DATA_REPORT;
                    handler.sendMessage(message);

                }
            }
        }.start();

    }


    private String getJson(String method, String soap_action) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, method);
        if (scanResult != null) {
            soapObject.addProperty("DeviceNum", scanResult);
        } else {
            soapObject.addProperty("DeviceNum", number);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(soap_action, envelope);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }

    private int[] problemId;

    private void initView() {

        problemitems = new String[reportData.getList().size() + 1];
        b = new boolean[reportData.getList().size() + 1];
        problemId = new int[reportData.getList().size() + 1];

        b[0] = false;
        problemId[0] = 0;
        StringBuilder sb = new StringBuilder();
        problemitems[0] = "正常";
        for (int i = 1; i < problemitems.length; i++) {
            problemitems[i] = reportData.getList().get(i - 1).getCheckInfo();
            sb.append(problemitems[i]).append("\n");
            b[i] = false;
            problemId[i] = 1;
        }
        String check = sb.toString().substring(0, sb.toString().length() - 1);

        mFacility.setText(reportData.getDeviceName());
        mFacilityPerson.setText(reportData.getAdministrator());

        mFacilityCheck.setText(check);

        mFacilityLoca.setText(reportData.getAddressInfo());

        mIvphoto1 = (ImageView) findViewById(R.id.iv_report_icon1);
        mIvphoto2 = (ImageView) findViewById(R.id.iv_report_icon2);
        mIvphoto3 = (ImageView) findViewById(R.id.iv_report_icon3);
        mEtDesc = (EditText) findViewById(R.id.problemDesc);
        mEtlocation = (EditText) findViewById(R.id.locationDesc);
        mbtReport = (Button) findViewById(R.id.report);

        //语音录入
        mIvInput = (ImageView) findViewById(R.id.iv_input_style);
        mtvPressAudio = (TextView) findViewById(R.id.tv_press_audio);
        mtvAudio = (TextView) findViewById(R.id.tv_audio);
        mtvReset = (TextView) findViewById(R.id.tv_re_record);
        mllAudio = (LinearLayout) findViewById(R.id.ll_audio);


        //視頻输入
        mIvAdd = (ImageView) findViewById(R.id.iv_add_video);
        mIvPlay = (ImageView) findViewById(R.id.iv_play_video);
        mIvVideoDelete = (ImageView) findViewById(R.id.iv_delete);
        mIvAddVideo = (ImageView) findViewById(R.id.iv_video_des);
        mFlvideo = (FrameLayout) findViewById(R.id.fl_video);

        mllAddVideo = (LinearLayout) findViewById(R.id.ll_add_video);
        mllplay = (RelativeLayout) findViewById(R.id.ll_play_video);


    }


    private void initData() {
        soundUtil = new SoundUtil();
        if (diseaseInformation == null) {
            diseaseInformation = new DiseaseInformation();
        }
        //初始化上传图片列表

        if (scanResult != null) {
            diseaseInformation.diviceNum = scanResult;
        } else {
            diseaseInformation.diviceNum = number;
        }

        PermissionUtils.requestPermission(this, PermissionUtils.CODE_READ_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
        PermissionUtils.requestPermission(this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);

        //locat();


        mIvphoto1.setOnClickListener(listener);
        mIvphoto2.setOnClickListener(listener);
        mIvphoto3.setOnClickListener(listener);
        mbtReport.setOnClickListener(listener);


        mIvInput.setOnClickListener(listener);

        mIvAdd.setOnClickListener(listener);
        mIvAddVideo.setOnClickListener(listener);
        mIvVideoDelete.setOnClickListener(listener);
        mIvPlay.setOnClickListener(listener);
        mFacilityProblem.setOnClickListener(listener);


        mtvPressAudio.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (!Environment.getExternalStorageDirectory().exists()) {
                    ToastUtil.shortToast(getApplicationContext(), "No SDcard");
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mtvPressAudio.setBackgroundResource(R.drawable.shape_tv_audio_press);
                        //开始录音，开始倒计时计时
                        audioName = createAudioName();
                        audioNamepath = audioPath + audioName;
                        start(audioName);
                        startTime = System.currentTimeMillis();
                        startTimer.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        mtvPressAudio.setBackgroundResource(R.drawable.shape_tv_audio);
                        //先判断录音是否小于一秒,
                        //不小于：让重录显示出来，。
                        // 点击重录的时候。弹窗确定后time为0，删除文件
                        //
                        startTimer.cancel();
                        long endtime = System.currentTimeMillis();
                        if (endtime - startTime < MAX_TIME) {
                            finishRecord(endtime);
                        } else {  //当手指向上滑，会cancel
                            cancelRecord();
                        }

                        if (endtime - startTime < MIN_INTERVAL_TIME) {
                            mtvPressAudio.setEnabled(true);
                            mtvPressAudio.setFocusable(true);

                            mtvAudio.setVisibility(View.GONE);
                        } else {
                            mtvPressAudio.setEnabled(false);
                            mtvPressAudio.setFocusable(false);
                            mtvAudio.setVisibility(View.VISIBLE);
                        }


                        break;

                }

                return true;
            }
        });

        //点击播放按钮
        mtvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioNamepath == null) {
                    ToastUtil.shortToast(getApplicationContext(), disrecord);
                    return;
                } else {
                    final Drawable drawableleft = getResources().getDrawable(R.mipmap.iv_audio_play);
                    final Drawable drawable = getResources().getDrawable(R.mipmap.iv_audio_pause);
                    mtvAudio.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);


                    soundUtil.setOnFinishListener(new SoundUtil.OnFinishListener() {
                        @Override
                        public void onFinish() {
                            mtvAudio.setCompoundDrawablesWithIntrinsicBounds(drawableleft, null, null, null);
                        }

                        @Override
                        public void onError() {
                            ToastUtil.shortToast(getApplicationContext(), reportMore);
                        }
                    });

                    paly(audioNamepath);
                }
            }
        });

        mtvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReportActivity.this).setTitle(deleteTitle).setMessage(deleteContent).setPositiveButton(deleteOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(audioNamepath);
                        if (file.isFile() && file.exists()) {
                            file.delete();

                        }
                        audioNamepath = null;
                        mtvReset.setVisibility(View.GONE);
                        mtvAudio.setText("");
                        mtvPressAudio.setEnabled(true);
                        mtvPressAudio.setFocusable(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton(deleteCancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

    }

    private void paly(String name) {

        soundUtil.play(name);
    }

    private void cancelRecord() {
        stopRecording();
        File file = new File(audioNamepath);

        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    private String audioName;
    private String audioNamepath;

    private static final int MIN_INTERVAL_TIME = 1500;// 1s 最短
    public final static int MAX_TIME = 60 * 1000 + 500;// 1分钟，最长

    /**
     * 录音开始计时器，允许的最大时长进入倒计时
     */
    private CountDownTimer startTimer = new CountDownTimer(MAX_TIME - 500, 1000) { // 50秒后开始倒计时
        @Override
        public void onFinish() {
            ToastUtil.shortToast(getApplicationContext(), recordMax);
            long endtime = System.currentTimeMillis();
            finishRecord(endtime);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    };

    private void finishRecord(long endtime) {
        long intervalTime = endtime - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            ToastUtil.shortToast(getApplicationContext(), recordLittle);
            stopRecording();
            File file = new File(audioNamepath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
            return;
        } else {
            int time = (int) (intervalTime / 1000);
            mtvAudio.setText(time + "s");
            mtvReset.setVisibility(View.VISIBLE);
            stopRecording();
        }
    }

    private void stopRecording() {
        soundUtil.stop();
    }


    private String createAudioName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String name = simpleDateFormat.format(new Date()) + ".aac";
        return name;
    }

    private void start(String name) {
        soundUtil = new SoundUtil();
        soundUtil.start(name);
    }


    private void locat() {
        //进入上报页面的 时候 开始定位
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5 * 1000;
        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        locationClient.setLocOption(option);
        locationClient.start();
    }

    private String person_id;
    private static final String Tag = "com.xytsz.xytaj.fileprovider";

    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {

                case PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE:
                    File filePath = new File(pathUrl);
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    break;

                case PermissionUtils.CODE_CAMERA:
                    camera(1);
                    break;

            }
        }
    };

    private String fileResult;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.CODE_RECORD_AUDIO) {
            PermissionUtils.openSettingActivity(ReportActivity.this, "请打开录音权限");
            return;
        }

        PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isVideo;
    private int checkSelfPermission;
    private List<Integer> problemlist = new ArrayList<>();
    private boolean b[];

    private int type;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.report_facility_problem:

                    problemlist.clear();
                    //多选的dialog
                    new AlertDialog.Builder(ReportActivity.this).setTitle("问题项").setMultiChoiceItems(problemitems, b, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            b[which] = isChecked;
                            problemId[which] = 0;
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int tatolNum = 0;
                            for (int i = 0; i < b.length; i++) {
                                if (!b[i]) {
                                    ++tatolNum;
                                }
                            }

                            if (tatolNum == b.length) {
                                ToastUtil.shortToast(getApplicationContext(), "必须填写问题项");
                                return;
                            }


                            if (b[0]) {
                                for (int i = 1; i < b.length; i++) {
                                    if (b[i]) {
                                        ToastUtil.shortToast(getApplicationContext(), "请正确填写问题项");
                                        mFacilityProblem.setText("");
                                        return;
                                    }
                                }
                            }


                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < problemitems.length; i++) {
                                if (b[i]) {
                                    sb.append(problemitems[i]).append(",");
                                }
                            }
                            String problemtext = sb.toString().substring(0, sb.toString().length() - 1);
                            mFacilityProblem.setText(problemtext);
                            dialog.dismiss();

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();


                    break;

                case R.id.iv_add_video:
                    //加号
                    if (videoflg) {
                        mFlvideo.setVisibility(View.GONE);
                        videoflg = false;
                    } else {
                        mFlvideo.setVisibility(View.VISIBLE);
                        //是否有视频
                        if (isVideo) {
                            mllplay.setVisibility(View.VISIBLE);
                            mllAddVideo.setVisibility(View.GONE);
                        } else {
                            mllAddVideo.setVisibility(View.VISIBLE);
                            mllplay.setVisibility(View.GONE);
                        }
                        videoflg = true;
                    }
                    break;

                //添加视频
                case R.id.iv_video_des:
                    Intent intent1 = new Intent(ReportActivity.this, RecordVideoActivity.class);
                    startActivityForResult(intent1, 300);
                    break;
                //删除
                case R.id.iv_delete:

                    new AlertDialog.Builder(ReportActivity.this).setTitle(deleteVideoTitle).setMessage(deletevideoContent).setPositiveButton(deleteOk, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(videopath);
                            file.delete();
                            videopath = null;
                            mllAddVideo.setVisibility(View.VISIBLE);
                            mllplay.setVisibility(View.GONE);

                            dialog.dismiss();
                        }
                    }).setNegativeButton(deleteCancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

                    break;
                //播放
                case R.id.iv_play_video:
                    Intent intent = new Intent(ReportActivity.this, PlayVideoActivity.class);
                    intent.putExtra("videoPath", videopath);
                    startActivity(intent);
                    break;

                case R.id.iv_input_style:
                    if (btn_vocie) {
                        mllAudio.setVisibility(View.GONE);
                        mEtlocation.setVisibility(View.VISIBLE);
                        mIvInput.setImageResource(R.mipmap.iv_audio);
                        btn_vocie = false;
                    } else {
                        PermissionUtils.requestPermission(ReportActivity.this, PermissionUtils.CODE_RECORD_AUDIO, mPermissionGrant);

                        //判断是否有权限
                        try {
                            checkSelfPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);

                            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(ReportActivity.this, android.Manifest.permission.RECORD_AUDIO)) {
                                    new AlertDialog.Builder(ReportActivity.this).setMessage("需要录音权限，请允许").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                                } else {
                                    //23以下 不提示弹窗
                                    ActivityCompat.requestPermissions(ReportActivity.this, new String[]{android.Manifest.permission.RECORD_AUDIO}, PermissionUtils.CODE_RECORD_AUDIO);
                                }
                            }
                        } catch (RuntimeException e) {
                            ToastUtil.shortToast(getApplicationContext(), "请打开录音权限");
                        }


                        mIvInput.setImageResource(R.mipmap.iv_normal_text);
                        mllAudio.setVisibility(View.VISIBLE);
                        mEtlocation.setVisibility(View.GONE);
                        btn_vocie = true;
                    }

                    break;
                case R.id.iv_report_icon1:
                    //拍照
                    PermissionUtils.requestPermission(ReportActivity.this, PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE, mPermissionGrant);
                    PermissionUtils.requestPermission(ReportActivity.this, PermissionUtils.CODE_CAMERA, mPermissionGrant);


                    break;
                case R.id.iv_report_icon2:
                    //拍照
                    camera(2);
                    break;
                case R.id.iv_report_icon3:
                    //拍照
                    camera(3);
                    break;
                case R.id.report:

                    diseaseInformation.taskNumber = getTaskNumber();

                    mprogressbar.setVisibility(View.VISIBLE);
                    ToastUtil.shortToast(getApplicationContext(), uploading);

                    String facilityProblem = mFacilityProblem.getText().toString();

                    if (facilityProblem.isEmpty()) {
                        ToastUtil.shortToast(getApplicationContext(), "请先选择问题项");
                        mprogressbar.setVisibility(View.GONE);
                        return;
                    }

                    //判断是否正常
                    if (facilityProblem.equals("正常")) {
                        diseaseInformation.problemTag = 1;
                        diseaseInformation.problem = "";
                        for (int i = 1; i < problemId.length; i++) {
                            sb1.append(1).append(",");
                        }
                        diseaseInformation.problemID = sb1.toString().substring(0, sb1.toString().length() - 1);
                    } else {
                        String p[] = facilityProblem.split(",");

                        for (int i = 0; i < p.length; i++) {
                            for (ReportData.Problem pro : reportData.getList()) {
                                if (p[i].equals(pro.getCheckInfo())) {
                                    sb.append(pro.getCheckDic_ID()).append(",");
                                }
                            }
                        }
                        diseaseInformation.problemTag = 2;
                        diseaseInformation.problem = sb.toString().substring(0, sb.toString().length() - 1);

                        for (int i = 1; i < problemId.length; i++) {
                            sb1.append(problemId[i]).append(",");
                        }
                        diseaseInformation.problemID = sb1.toString().substring(0, sb1.toString().length() - 1);

                    }


                    diseaseInformation.diseaseDescription = mEtDesc.getText().toString();
                    diseaseInformation.audioTime = mtvAudio.getText().toString();
                    //保存到服务器  弹吐司
                    if (mEtlocation != null) {
                        diseaseInformation.locationDesc = mEtlocation.getText().toString();
                        if (diseaseInformation.locationDesc.isEmpty() && audioNamepath == null) {
                            ToastUtil.shortToast(getApplicationContext(), reportLocation);
                            mprogressbar.setVisibility(View.GONE);
                            return;
                        }

                    }

                    mprogressbar.setVisibility(View.VISIBLE);
                    diseaseInformation.uploadTime = getCurrentTime();
                    diseaseInformation.photoName = createPhotoName();

                    diseaseInformation.upload_Person_ID = SpUtils.getInt(getApplicationContext(), GlobalContanstant.PERSONID);

                    person_id = diseaseInformation.upload_Person_ID + "";


                    PermissionUtils.requestPermission(ReportActivity.this, PermissionUtils.CODE_ACCESS_COARSE_LOCATION, mPermissionGrant);

                    /**
                     * 上报信息
                     */
                    if (imageBase64Strings.size() != 0) {
                        if (audioNamepath != null) {
                            diseaseInformation.locationDesc = "";
                        }

                        mbtReport.setVisibility(View.GONE);

                        new Thread() {
                            @Override
                            public void run() {
                                if (audioNamepath != null) {
                                    File file = new File(audioNamepath);
                                    //如果文件存在
                                    if (file.exists()) {
                                        try {
                                            String encodeBase64File = FileBase64Util.encodeBase64File(audioNamepath);
                                            diseaseInformation.encodeBase64File = encodeBase64File;
                                            diseaseInformation.audioName = audioName;
                                            String result = updateAudio(diseaseInformation);
                                            Message message = Message.obtain();
                                            message.what = AUDIO_SUCCESS;
                                            message.obj = result;
                                            handler.sendMessage(message);
                                        } catch (Exception e) {
                                            Message message = Message.obtain();
                                            message.what = AUDIO_FAIL;
                                            handler.sendMessage(message);
                                            return;
                                        }


                                    }
                                }

                                if (videopath != null) {

                                    File file = new File(videopath);
                                    if (file.exists()) {
                                        try {
                                            String encodeBase64File = FileBase64Util.encodeBase64File(videopath);

                                            diseaseInformation.encodeBase64File = encodeBase64File;
                                            diseaseInformation.videoName = videoName;
                                            String result = updateVideo(diseaseInformation);
                                            Message message = Message.obtain();
                                            message.what = VIDEO_SUCCESS;
                                            message.obj = result;
                                            handler.sendMessage(message);
                                        } catch (Exception e) {
                                            Message message = Message.obtain();
                                            message.what = VIDEO_FAIL;
                                            handler.sendMessage(message);
                                        }
                                    }
                                }


                                for (int i = 0; i < fileNames.size(); i++) {
                                    diseaseInformation.photoName = fileNames.get(i);
                                    diseaseInformation.encode = imageBase64Strings.get(i);

                                    try {
                                        isphotoSuccess1 = connectWebService(diseaseInformation);
                                    } catch (Exception e) {
                                        Message message = Message.obtain();
                                        message.what = GlobalContanstant.IMAGEFAIL;
                                        handler.sendMessage(message);
                                    }

                                }

                                if (isphotoSuccess1 != null) {
                                    if (isphotoSuccess1.equals("true")) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.shortToast(getApplicationContext(), imgsuccess);
                                            }
                                        });

                                        try {

                                            String remoteInfo = getRemoteInfo(diseaseInformation);
                                            Message message = Message.obtain();
                                            message.obj = remoteInfo;
                                            message.what = GlobalContanstant.REPORTESUCCESS;
                                            handler.sendMessage(message);
                                        } catch (Exception e) {
                                            Message message = Message.obtain();
                                            message.what = GlobalContanstant.REPORTEFAIL;
                                            handler.sendMessage(message);
                                        }

                                    } else {
                                        Message message = Message.obtain();
                                        message.what = GlobalContanstant.IMAGEFAIL;
                                        handler.sendMessage(message);
                                    }

                                } else {
                                    Message message = Message.obtain();
                                    message.what = GlobalContanstant.IMAGEFAIL;
                                    handler.sendMessage(message);
                                }

                            }
                        }.start();
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), request);
                        mprogressbar.setVisibility(View.GONE);
                        mbtReport.setVisibility(View.VISIBLE);
                    }

                    break;
            }
        }
    };

    private void camera(int position) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (Build.VERSION.SDK_INT >= 24) {
            file = new File(getExternalCacheDir(), "reviewimg" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = FileProvider.getUriForFile(ReportActivity.this, Tag, file);
            fileResult = file.getAbsolutePath();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            file = new File(pathUrl, "imageTest" + position + ".jpg");
            FileUtils.deletFile(file);
            fileUri = Uri.fromFile(file);
            fileResult = fileUri.getPath();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, position);
    }

    private StringBuilder sb = new StringBuilder();
    private StringBuilder sb1 = new StringBuilder();

    //上传所有的数据
    public String getRemoteInfo(DiseaseInformation diseaseInformation) throws Exception {

        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.reportmethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("DeviceNum", diseaseInformation.diviceNum);
        //tag
        soapObject.addProperty("CheckInfo", diseaseInformation.problemTag);
        //问题string
        soapObject.addProperty("error", diseaseInformation.problem);
        //具体的描述
        soapObject.addProperty("Remarks", diseaseInformation.locationDesc);
        //设备问题描述标签
        soapObject.addProperty("Info", diseaseInformation.problemID);
        soapObject.addProperty("personId", personId);

        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(NetUrl.SERVERURL);
        httpTransportSE.call(null, envelope);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        reportResult = object.getProperty(0).toString();
        return reportResult;

    }


    private String updateAudio(DiseaseInformation diseaseInformation) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.audiomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.audioName);  //文件类型
        soapObject.addProperty("AudioBase64", diseaseInformation.encodeBase64File);   //参数2  图片字符串
        soapObject.addProperty("time", diseaseInformation.audioTime);   //参数2  图片字符串

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String isphotoSuccess = object.getProperty(0).toString();
        return isphotoSuccess;

    }

    private String updateVideo(DiseaseInformation diseaseInformation) throws Exception {
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.videomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.videoName);  //文件类型
        soapObject.addProperty("Mp4Base64", diseaseInformation.encodeBase64File);   //参数2  图片字符串
        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String isphotoSuccess = object.getProperty(0).toString();
        return isphotoSuccess;

    }


    private String connectWebService(DiseaseInformation diseaseInformation) throws Exception {
        //构建初始化soapObject
        SoapObject soapObject = new SoapObject(NetUrl.nameSpace, NetUrl.photomethodName);
        //传递的参数
        soapObject.addProperty("DeciceCheckNum", diseaseInformation.taskNumber);
        soapObject.addProperty("FileName", diseaseInformation.photoName);  //文件类型
        soapObject.addProperty("ImgBase64String", diseaseInformation.encode);   //参数2  图片字符串
        soapObject.addProperty("PhaseId", GlobalContanstant.GETREVIEW);

        //设置访问地址 和 超时时间
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        HttpTransportSE httpTranstation = new HttpTransportSE(NetUrl.SERVERURL);
        //链接后执行的回调
        httpTranstation.call(null, envelope);
        SoapObject object = (SoapObject) envelope.bodyIn;

        String isphotoSuccess = object.getProperty(0).toString();
        return isphotoSuccess;
    }

    /**
     * 给拍的照片命名
     */
    public String createPhotoName() {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String fileName = format.format(date) + ".jpg";
        return fileName;
    }

    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/mymy/";


    /**
     * 保存到本地
     */
    public String saveToSDCard(Bitmap bitmap) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName();
        path = iconPath + photoName;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File pictureFile = new File(path);
                //压缩图片
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, bos);
                byte[] bytes = bos.toByteArray();
                //将图片封装成File对象
                FileOutputStream outputStream = new FileOutputStream(pictureFile);
                outputStream.write(bytes);
                outputStream.close();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.shortToast(getApplicationContext(), "SD卡不存在");
        }

        return photoName;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (Build.VERSION.SDK_INT >= 24) {
            outState.putString("file_path", fileResult);
        } else {
            outState.putString("file_path", fileResult);

        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 显示图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == 300) {
            if (resultCode == 301) {
                videopath = data.getStringExtra("videoPath");
                if (videopath != null) {
                    mllplay.setVisibility(View.VISIBLE);
                    mllAddVideo.setVisibility(View.GONE);
                } else {
                    mllplay.setVisibility(View.GONE);
                    mllAddVideo.setVisibility(View.VISIBLE);
                }
                videoName = data.getStringExtra("videoName");
            }
        }


        Bitmap bitmap = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

                }
                if (bitmap == null){
                    return;
                }
                mIvphoto1.setImageBitmap(bitmap);
                String fileName1 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto1.setClickable(false);
                String encode1 = photo2Base64(path);
                fileNames.add(fileName1);
                imageBase64Strings.add(encode1);
            } else if (requestCode == 2) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

//                    bitmap = getBitmap(mIvphoto2, fileResult);
                }
                if (bitmap == null){
                    return;
                }
                    mIvphoto2.setImageBitmap(bitmap);
                String fileName2 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto2.setClickable(false);
                String encode2 = photo2Base64(path);
                fileNames.add(fileName2);
                imageBase64Strings.add(encode2);

            } else if (requestCode == 3) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");

                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

//                    bitmap = getBitmap(mIvphoto1, fileResult);
                }
                if (bitmap == null){
                    return;
                }
                mIvphoto3.setImageBitmap(bitmap);
                String fileName3 = saveToSDCard(bitmap);
                //将选择的图片设置到控件上
                mIvphoto3.setClickable(false);
                String encode3 = photo2Base64(path);
                fileNames.add(fileName3);
                imageBase64Strings.add(encode3);

            }
        }
        //新加的
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected static Bitmap getBitmap(ImageView imageView, String path) {
        Bitmap bitmap;
        int width = 200;

        int height = 200;

        BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

        factoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, factoryOptions);

        int imageWidth = factoryOptions.outWidth;
        int imageHeight = factoryOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(imageWidth / width, imageHeight
                / height);

        // Decode the image file into a Bitmap sized to fill the
        // View
        factoryOptions.inJustDecodeBounds = false;
        factoryOptions.inSampleSize = scaleFactor;
        factoryOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(path,
                factoryOptions);

        int bitmapDegree = getBitmapDegree(path);
        if (bitmap != null) {
            bitmap = rotateBitmap(bitmap, bitmapDegree);
        }
        return bitmap;
    }

    private static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            //从指定路径读取图片，获取exif信息
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }

            return degree;

        } catch (IOException e) {
            //e.printStackTrace();
        }

        return degree;
    }


    private static Bitmap rotateBitmap(Bitmap bm, float orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {

        }
        return null;
    }


    private Bitmap largeBitmap = null;

    public static String photo2Base64(String path) {

        try {
            FileInputStream fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            byte[] encode = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
            String uploadBuffer = new String(encode);
            fis.close();
            return uploadBuffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int id;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case DATA_SUCCESS:
                    reportData = (ReportData) msg.obj;
                    if (reportData != null) {
                        if (reportData.getList() != null) {
                            if (reportData.getAdministrator().equals(userName) || reportData.getChargeperson2().equals(userName)) {
                                ll_report.setVisibility(View.VISIBLE);
                                rl_notonlie.setVisibility(View.GONE);
                                mprogressbar.setVisibility(View.GONE);
                                initView();
                                initData();
                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "此设备不属于您维护");
                                goHome();
                                return;
                            }


                        } else {
                            ToastUtil.shortToast(getApplicationContext(), "未获取数据");
                        }

                    }

                    break;
                case DATA_REPORT:
                    mprogressbar.setVisibility(View.GONE);
                    rl_notonlie.setVisibility(View.VISIBLE);
                    ll_report.setVisibility(View.INVISIBLE);
                    ToastUtil.shortToast(getApplicationContext(), "未获取数据,请刷新");
                    break;

                case GlobalContanstant.REPORTEFAIL:
                    mbtReport.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), uperror);
                    break;

                case GlobalContanstant.REPORTESUCCESS:

                    mprogressbar.setVisibility(View.GONE);

                    String reportSuccess = (String) msg.obj;
                    if (reportSuccess != null) {
                        if (reportSuccess.equals("true")) {

                            //弹出通知：并提示音
                            List<String> personNamelist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONNAMELIST);
                            List<String> personIDlist = SpUtils.getStrListValue(getApplicationContext(), GlobalContanstant.PERSONIDLIST);

                            for (int i = 0; i < personIDlist.size(); i++) {
                                if (person_id.equals(personIDlist.get(i))) {
                                    id = i;
                                }
                            }

                            if (personNamelist.size() != 0) {
                                String userName = personNamelist.get(id);
                                largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                NotificationManager nm = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
                                //Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Notification noti = new NotificationCompat.Builder(getApplicationContext())
                                        .setTicker(userName + notifatitle)
                                        .setContentTitle(userName)
                                        .setContentText(notifa)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setLargeIcon(largeBitmap)
                                        .setContentIntent(getContentIntent())
                                        .setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH)//高优先级
                                        .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE)
                                        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                                        //自动隐藏
                                        .setAutoCancel(true)
                                        .build();
                                //id =0 =  用来定义取消的id
                                nm.notify(0, noti);

                                ToastUtil.shortToast(getApplicationContext(), reportsuccess);

                                diseaseInformation = null;
                            }
                            goHome();
                        } else {
                            ToastUtil.shortToast(getApplicationContext(), uperror);
                            mbtReport.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtil.shortToast(getApplicationContext(), uperror);
                        mbtReport.setVisibility(View.VISIBLE);
                    }
                    break;

                case GlobalContanstant.IMAGEFAIL:
                    ToastUtil.shortToast(getApplicationContext(), imageerror);
                    mbtReport.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    return;


                case AUDIO_FAIL:
                    mbtReport.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), audioFail);
                    return;
                case VIDEO_FAIL:
                    mbtReport.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), videoFail);
                    break;
                case AUDIO_SUCCESS:
                    String audioSuccess = (String) msg.obj;
                    if (audioSuccess != null) {
                        if (audioSuccess.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), audiosuccess);
                        }
                    } else {
                        mbtReport.setVisibility(View.VISIBLE);
                        mprogressbar.setVisibility(View.GONE);
                    }

                    break;
                case VIDEO_SUCCESS:
                    String videoSuccess = (String) msg.obj;
                    if (videoSuccess != null) {
                        if (videoSuccess.equals("true")) {
                            ToastUtil.shortToast(getApplicationContext(), videosuccess);
                        }
                    } else {
                        mbtReport.setVisibility(View.VISIBLE);
                        mprogressbar.setVisibility(View.GONE);
                    }

                    break;


            }
        }
    };

    private void goHome() {
        Intent intent = new Intent(ReportActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        startActivity(intent);
        finish();
    }


    private PendingIntent getContentIntent() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("backHome", GlobalContanstant.BACKHOME);
        return PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }


    private class MyListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //经度
            if (diseaseInformation != null) {
                diseaseInformation.longitude = bdLocation.getLongitude() + "";
                //维度
                diseaseInformation.latitude = bdLocation.getLatitude() + "";
            }


        }
    }

    //得到任务单号的方法
    private String getTaskNumber() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }

    //得到上穿时间
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }

    @Override
    protected void onStart() {
        if (locationClient != null) {
            locationClient.start();
        }
        super.onStart();


    }

    @Override
    protected void onPause() {
        if (locationClient != null) {
            locationClient.unRegisterLocationListener(myListener);
            locationClient.stop();

        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.stop();
            locationClient.unRegisterLocationListener(myListener);
            locationClient = null;
        }
        diseaseInformation = null;
        finish();
    }

    private void initAcitionbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.reprote);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
