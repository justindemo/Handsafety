package com.xytsz.xytaj.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;

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

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xytsz.xytaj.MyApplication;
import com.xytsz.xytaj.R;

import com.xytsz.xytaj.bean.DiseaseInformation;
import com.xytsz.xytaj.bean.Person;
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


import org.ksoap2.HeaderProperty;
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


/**
 * Created by admin on 2017/1/10.
 * 上报页面
 */
public class ReportActivity extends AppCompatActivity {

    private static final int AUDIO_SUCCESS = 500;
    private static final int AUDIO_FAIL = 404;
    private static final int ISMEMBER = 11143;

    private ImageView mIvphoto1;
    private ImageView mIvphoto2;
    private ImageView mIvphoto3;
    private EditText mEtlocation;

    private Button mbtReport;
    //图片的存储位置
    private static final String iconPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/";//图片的存储目录
    private static final String audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Audio/";
    private DiseaseInformation diseaseInformation;
    private String isphotoSuccess1;
    private String path;
    private List<String> fileNames = new ArrayList<>();
    private List<String> imageBase64Strings = new ArrayList<>();

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
    private static long startTime;
    private SoundUtil soundUtil;
    private String deleteTitle;
    private String deleteContent;
    private String deleteOk;
    private String deleteCancle;
    private String recordLittle;
    private String recordMax;
    private String audiosuccess;
    private String audioFail;
    private String disrecord;
    private String reportMore;
    private String reportLocation;


    private String imageerror;
    private String uperror;
    private String reportsuccess;
    private RelativeLayout rl_notonlie;
    private LinearLayout ll_report;
    private LinearLayout mprogressbar;
    private Button mbtrefresh;
    private String scanResult;
    private TextView mFacility;
    private TextView mFacilityPerson;
    private TextView mFacilityCheck;
    private TextView mFacilityProblem;
    private TextView mFacilityLoca;
    private ReportData reportData;
    /**
     * 检查项
     */
    private String[] problemitems;
    private String number;
    private String userName;
    private File file;
    private String remoteInfo;
    private List<HeaderProperty> headerList = new ArrayList<>();

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

        deleteContent = getString(R.string.report_delete_content);
        deleteOk = getString(R.string.report_delete_ok);
        deleteCancle = getString(R.string.report_delete_cancle);
        recordLittle = getString(R.string.report_record_little);
        recordMax = getString(R.string.report_record_max);

        reportLocation = getString(R.string.reportlocation);

        notifa = this.getString(R.string.report_notifica);
        notifatitle = this.getString(R.string.report_notifica_title);
        error = this.getString(R.string.report_error);
        imageerror = this.getString(R.string.report_imageerror);
        reportsuccess = this.getString(R.string.report_success);
        uperror = this.getString(R.string.report_uperror);

        audiosuccess = getString(R.string.audio_success);
        audioFail = getString(R.string.audio_fail);


        disrecord = getString(R.string.disrecord);

        reportMore = getString(R.string.reportmore);


        rl_notonlie = (RelativeLayout) findViewById(R.id.rl_notonline);
        ll_report = (LinearLayout) findViewById(R.id.ll_report);
        mprogressbar = (LinearLayout) findViewById(R.id.home_progressbar);
        mbtrefresh = (Button) findViewById(R.id.btn_refresh);

        mFacility = (TextView) findViewById(R.id.report_facility);

        mFacilityPerson = (TextView) findViewById(R.id.report_facility_person);
        mFacilityCheck = (TextView) findViewById(R.id.report_facility_check);
        mFacilityProblem = (TextView) findViewById(R.id.report_facility_problem);
        mFacilityLoca = (TextView) findViewById(R.id.report_facility_loca);


        headerList.clear();
        HeaderProperty headerPropertyObj = new HeaderProperty(GlobalContanstant.Cookie,
                SpUtils.getString(getApplicationContext(),GlobalContanstant.CookieHeader));

        headerList.add(headerPropertyObj);


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
        httpTransportSE.call(soap_action, envelope,headerList);

        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();
    }

    private int[] problemId;

    private void initView() {
        mFacility.setText(reportData.getDeviceName());
        mFacilityPerson.setText(reportData.getAdministrator());
        mFacilityLoca.setText(reportData.getAddressInfo());

        mIvphoto1 = (ImageView) findViewById(R.id.iv_report_icon1);
        mIvphoto2 = (ImageView) findViewById(R.id.iv_report_icon2);
        mIvphoto3 = (ImageView) findViewById(R.id.iv_report_icon3);
        mEtlocation = (EditText) findViewById(R.id.locationDesc);
        mbtReport = (Button) findViewById(R.id.report);

        //语音录入
        mIvInput = (ImageView) findViewById(R.id.iv_input_style);
        mtvPressAudio = (TextView) findViewById(R.id.tv_press_audio);
        mtvAudio = (TextView) findViewById(R.id.tv_audio);
        mtvReset = (TextView) findViewById(R.id.tv_re_record);
        mllAudio = (LinearLayout) findViewById(R.id.ll_audio);


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


        mIvphoto1.setOnClickListener(listener);
        mIvphoto2.setOnClickListener(listener);
        mIvphoto3.setOnClickListener(listener);
        mbtReport.setOnClickListener(listener);

        mIvInput.setOnClickListener(listener);


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


    private boolean ischeckProblem[];

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.report_facility_problem:

                    //多选的dialog
                    new AlertDialog.Builder(ReportActivity.this).setTitle("问题项").
                            setMultiChoiceItems(problemitems, ischeckProblem,
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            ischeckProblem[which] = isChecked;
                                            //被选中代表不正常
                                            problemId[which] = 0;
                                        }
                                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int tatolNum = 0;
                            for (int i = 0; i < ischeckProblem.length; i++) {
                                if (!ischeckProblem[i]) {
                                    ++tatolNum;
                                }
                            }
                            //未选
                            if (tatolNum == ischeckProblem.length) {
                                ToastUtil.shortToast(getApplicationContext(), "必须填写问题项");
                                return;
                            }

                            //如果正常被选
                            if (ischeckProblem[0]) {
                                for (int i = 1; i < ischeckProblem.length; i++) {
                                    //不正常的也有选择
                                    if (ischeckProblem[i]) {
                                        ToastUtil.shortToast(getApplicationContext(), "请正确填写问题项");
                                        mFacilityProblem.setText("");
                                        return;
                                    }
                                }
                            }


                            StringBuilder selectproblem = new StringBuilder();
                            for (int i = 0; i < problemitems.length; i++) {
                                if (ischeckProblem[i]) {
                                    selectproblem.append(problemitems[i]).append(",");
                                }
                            }
                            String problemtext = selectproblem.toString().substring(0, selectproblem.toString().length() - 1);
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

                //播放

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
                            int checkSelfPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);

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
                    /*
                    初始化
                     */
                    checkId = null;
                    selectProblemId = null;
                    checkId = new StringBuilder();
                    selectProblemId = new StringBuilder();


                    diseaseInformation.taskNumber = getTaskNumber();

                    mprogressbar.setVisibility(View.VISIBLE);
                    ToastUtil.shortToast(getApplicationContext(), uploading);
                    //拿到 问题项
                    String facilityProblem = mFacilityProblem.getText().toString();

                    if (facilityProblem.isEmpty()) {
                        ToastUtil.shortToast(getApplicationContext(), "请先选择问题项");
                        mprogressbar.setVisibility(View.GONE);
                        return;
                    }


                    //判断是否正常
                    if (facilityProblem.equals("正常")) {
                        /** 1= 正常   2 = 不正常 */
                        diseaseInformation.problemTag = 1;
                        //错误信息
                        diseaseInformation.problem = "";
                        //传递ID:  id:1  正常  id：0  不正常
                        for (int i = 1; i < problemId.length; i++) {
                            selectProblemId.append(1).append(",");
                        }
                        diseaseInformation.problemID = selectProblemId.toString().substring(0, selectProblemId.toString().length() - 1);
                    } else {
                        //不正常
                        String p[] = facilityProblem.split(",");

                        for (int i = 0; i < p.length; i++) {
                            //遍历检查项
                            for (ReportData.Problem pro : reportData.getList()) {
                                if (p[i].equals(pro.getCheckInfo())) {
                                    //拿到错误Id
                                    checkId.append(pro.getCheckDic_ID()).append(",");
                                }
                            }
                        }
                        diseaseInformation.problemTag = 2;
                        //具体id
                        diseaseInformation.problem = checkId.toString().substring(0, checkId.toString().length() - 1);

                        for (int i = 1; i < problemId.length; i++) {
                            selectProblemId.append(problemId[i]).append(",");
                        }
                        //用户选择
                        diseaseInformation.problemID = selectProblemId.toString().substring(0,
                                selectProblemId.toString().length() - 1);

                    }


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


                                try {
                                    remoteInfo = getRemoteInfo(diseaseInformation);

                                } catch (Exception e) {
                                    Message message = Message.obtain();
                                    message.what = GlobalContanstant.REPORTEFAIL;
                                    handler.sendMessage(message);
                                }


                                if (remoteInfo != null) {
                                    if (remoteInfo.equals("true")) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.shortToast(getApplicationContext(), reportsuccess);
                                            }
                                        });


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
                                        Message message = Message.obtain();
                                        message.obj = isphotoSuccess1;
                                        message.what = GlobalContanstant.REPORTESUCCESS;
                                        handler.sendMessage(message);


                                    } else {
                                        Message message = Message.obtain();
                                        message.what = GlobalContanstant.REPORTEFAIL;
                                        handler.sendMessage(message);
                                    }

                                } else {
                                    Message message = Message.obtain();
                                    message.what = GlobalContanstant.REPORTEFAIL;
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

    private StringBuilder checkId = new StringBuilder();
    private StringBuilder selectProblemId = new StringBuilder();

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
        httpTransportSE.call(null, envelope,headerList);//调用

        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        return object.getProperty(0).toString();


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
        httpTranstation.call(null, envelope,headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();


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
        httpTranstation.call(null, envelope,headerList);
        SoapObject object = (SoapObject) envelope.bodyIn;

        return object.getProperty(0).toString();

    }

    /**
     * 给拍的照片命名
     */
    public String createPhotoName(int position) {
        //以系统的当前时间给图片命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return format.format(date) + "_" + position + "_" + personId + ".jpg";

    }

    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Zsaj/Image/mymy/";

    /**
     * 保存到本地
     */
    public String saveToSDCard(Bitmap bitmap, int position) {
        //先要判断SD卡是否存在并且挂载
        String photoName = createPhotoName(position);
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

        Bitmap bitmap = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                } else {
                    bitmap = BitmapUtil.getScaleBitmap(fileResult);

                }
                if (bitmap == null) {
                    return;
                }
                mIvphoto1.setImageBitmap(bitmap);
                String fileName1 = saveToSDCard(bitmap, 1);
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
                if (bitmap == null) {
                    return;
                }
                mIvphoto2.setImageBitmap(bitmap);
                String fileName2 = saveToSDCard(bitmap, 2);
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
                if (bitmap == null) {
                    return;
                }
                mIvphoto3.setImageBitmap(bitmap);
                String fileName3 = saveToSDCard(bitmap, 3);
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


    /*protected static Bitmap getBitmap(ImageView imageView, String path) {
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
    }*/

    /*private static int getBitmapDegree(String path) {
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
    }*/


   /* private static Bitmap rotateBitmap(Bitmap bm, float orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {

        }
        return null;
    }*/


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
                                initArray();
                                initView();
                                initData();
                            } else {
                                ToastUtil.shortToast(getApplicationContext(), "此设备不属于您维护");
                                goHome();

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
//                    initArray();
                    mbtReport.setVisibility(View.VISIBLE);
                    mprogressbar.setVisibility(View.GONE);
                    ToastUtil.shortToast(getApplicationContext(), uperror);
                    mFacilityProblem.setText("");

                    break;

                case GlobalContanstant.REPORTESUCCESS:

                    mprogressbar.setVisibility(View.GONE);

                    String reportSuccess = (String) msg.obj;
                    if (reportSuccess != null) {
                        if (reportSuccess.equals("true")) {
                            String userName = SpUtils.getString(getApplicationContext(), GlobalContanstant.USERNAME);
                            Bitmap largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
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

                            ToastUtil.shortToast(getApplicationContext(), imgsuccess);

                            diseaseInformation = null;

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


            }
        }
    };

    private void initArray() {
        checkId = new StringBuilder();
        selectProblemId = new StringBuilder();
        //填充空间， 检查项加+1  1：正常
        problemitems = new String[reportData.getList().size() + 1];
        ischeckProblem = new boolean[reportData.getList().size() + 1];
        problemId = new int[reportData.getList().size() + 1];

        ischeckProblem[0] = false;
        //0,代表不正常
        problemId[0] = 1;
        StringBuilder initprobleom = new StringBuilder();
        problemitems[0] = "正常";
        for (int i = 1; i < problemitems.length; i++) {
            problemitems[i] = reportData.getList().get(i - 1).getCheckInfo();
            initprobleom.append(problemitems[i]).append("\n");
            //默认都不选中
            ischeckProblem[i] = false;
            //默认都正常
            problemId[i] = 1;
        }
        String check = initprobleom.toString().substring(0, initprobleom.toString().length() - 1);
        //设置检查项
        mFacilityCheck.setText(check);
    }

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


    //得到任务单号的方法
    private String getTaskNumber() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }

    //得到上穿时间
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String str = formatter.format(date);
        return str;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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
